(ns battlenet.wow
  (:gen-class)
  (:require [clojure.string :as string])
  (:require [clojure.stacktrace :as st])
  (:require [battlenet.config :as config])
  (:require [battlenet.defs :as defs])
  (:require [battlenet.utils :as utils]))

(def current-max-level 80)
(def last-max-level 70)
(def exp-max-values-all {:kultiran 175 :legion 100
                         :draenor 100  :pandaria 75 :cataclysm 75
                         :northrend 75 :outland 75  :classic 300})
(def exp-max-values-sl {:alchemy 175 :fishing 200 :cooking 75
                        :mining 150 :herbalism 150 :skinning 150
                        :enchanting 115 :inscription 100 :leatherworking 100
                        :blacksmithing 100 :engineering 100 :jewelcrafting 100
                        :tailoring 100})
(def exp-max-values-df 100)
(def exp-max-values-tww 100)
(def default-cache {:character_class {:name "Warrior", :id 1}})

(defn- ppe [s]
  (.println *err* s))

(defn- ppex [s]
  (ppe (str "#ERR# " s)))

;### name level guild realm ilvl1 ivl2
;# :name
;# :level
;# :guild(:id :slug :name :realm(:name))
;# :realm(:id :slug :name)
;# :average_item_level
;# :equipped_item_level
;### Cooking Fishing Arch P1 P2
;### race gender class faction
;# :race(:name :id)
;# :gender(:type :name)
;# :character_class(:name :id)
;# :faction(:type :name)

(defn show-faction [k]
  (let [html "<img src=\"/img/FACTION.png\" width=\"16\" height=\"16\" alt=\"FACTION\" />"]
    (string/replace html #"FACTION" (string/lower-case k))))

(defn show-icons [cls race gender]
  (let [class-ico (utils/slugify-class cls)
        class-ico-full (str "IconLarge_" (utils/uc-first class-ico) ".png")
        race-icon (str "IconLarge_" (utils/uc-first (utils/slugify-class race)) "_" gender ".png")
        html-class (str "<img src=\"/img/" class-ico-full "\" width=\"16\" height=\"16\" alt=\"" cls "\" />")
        html-race (str "<img src=\"/img/" race-icon "\" width=\"16\" height=\"16\" alt=\"" race " " gender "\" />")]
    (str html-race html-class)))

(defn char-url
  [realm-slug name]
  (->
    defs/bn-wow-char-url
    (string/replace "{locale}" (utils/slugify-locale config/current-locale))
    (string/replace "{region}" config/current-region)
    (string/replace "{realm}" realm-slug)
    (string/replace "{name}" (string/lower-case name))))

(defn guild-url
  [json]
  (if (seq (:guild json))
    (let [gjson (:guild json)
          slug (utils/slugify-guild-char (:name gjson))
          rslug (:slug (:realm gjson))]
      (->
        defs/bn-wow-guild-url
        (string/replace "{locale}" (utils/slugify-locale config/current-locale))
        (string/replace "{region}" config/current-region)
        (string/replace "{realm}" rslug)
        (string/replace "{name}" slug )))
    ""))

(defn guild-link
  [url name guild-id realm-id]
  (if (empty? url)
    ""
    (str "<a href=\"" url "\" data-guild-id=\"" guild-id "\" data-realm-id=\"" realm-id "\">" name "</a>")))

(defn format-level [lvl]
  (if (= current-max-level lvl)
    (str "<span class=\"prof-max\">" lvl "</span>")
    (if (= last-max-level lvl)
      (str "<span class=\"prof-last-max\">" lvl "</span>")
      (str "<span>" lvl "</span>"))))

(defn foo-tiers [input destination]
  (let [idx (zipmap (map (comp :id :tier) input)
                       input)]
    (into {} (for [[k v] destination]
               [k (-> v
                      (conj (idx (:id v)))
                      (dissoc :known_recipes)
                      (dissoc :tier))]))))

(defn prof-get [json idx k lookup]
  (try
    (if-let [pname (or (:name (:profession (nth (get json k) idx))) nil)]
      (let [kwp (utils/kw-prof pname)
            base (get lookup kwp)
            tmp (nth (get json k) idx)]
            {:name pname :skills (foo-tiers (:tiers tmp) base) :skill_points (:skill_points tmp) :max_skill_points (:max_skill_points tmp)}))
    (catch Exception ex nil)))

(defn prof-primary [json idx]
  (prof-get json idx :primaries defs/bn-professions-primary))

(defn prof-secondary [json idx]
  (prof-get json idx :secondaries defs/bn-professions-secondary))

(defn format-prof-tpl [pname xx tpl2 y]
  (let [name (:name xx)
        sp (or (:skill_points xx) 0)
        exp (utils/slugify-class (string/replace name (str " " pname) ""))
        s-level (string/replace tpl2 "XXLEVEL" (if (= 0 sp) "&nbsp;" (str sp)))
        s-name (string/replace s-level "XXNAME" name)]
    (if (empty? y)
      (let [kw (utils/slugify-class pname)
            val (or (get exp-max-values-sl (keyword kw)) 0)]
        (if (>= sp val)
          (string/replace s-name "XXHIDDEN" (str y " is-max"))
          (string/replace s-name "XXHIDDEN" (str y " not-max"))))
      (let [kw (if (= exp (string/lower-case pname)) :classic (keyword exp))
            val (or (get exp-max-values-all kw) 0)]
        (if (>= sp val)
          (string/replace s-name "XXHIDDEN" (str y " is-max"))
          (string/replace s-name "XXHIDDEN" (str y " not-max")))))))

(defn format-prof [profs tpl1 tpl2 css-class]
  ; @TODO if profs empty -> <td></td>
  (let [pname (:name profs)
        img (utils/slugify-class pname)
        s1 (string/replace tpl1 "XXNAME" pname)
        ordered-prof-names (map #(keyword (if (nil? %) img (str (name %) img))) defs/bn-professions-order)
        sorted (for [x ordered-prof-names] (get (:skills profs) x))
        prof-latest (first sorted)
        prof-rest (drop 1 sorted)]
  (str (string/replace s1 "XXIMG" img)
    (apply str (map #(format-prof-tpl pname % tpl2 "") [prof-latest]))
    (apply str (map #(format-prof-tpl pname % tpl2 (str "hide " css-class)) prof-rest)))))

(defn format-empty-prof [tpl css-class]
  (let [rest (string/replace tpl "XXHIDDEN" (str "hide " css-class))]
    (str (string/replace tpl "XXHIDDEN" "divider-l")
      (string/replace tpl "XXHIDDEN" "")
      (apply str (take (dec (count defs/bn-professions-order)) (repeat rest))))))

(defn sort-secondaries [pname p1 p2 p3]
  (if (= pname (:name p1)) p1
    (if (= pname (:name p2)) p2
      (if (= pname (:name p3)) p3 nil))))

(defn get-covenant [json]
  (let [cov (:covenant_progress json)]
    {:level (:renown_level cov)
     :id (:id (:chosen_covenant cov))
     :name (:name (:chosen_covenant cov))}))

(defn format-covenant [cov]
  (if (or (nil? cov) (nil? (:id cov)))
    ""
    (str "&nbsp;<img src=\"/img/covenant_" (:id cov) ".png\" alt=\"" (:name cov) "\" title=\"" (:name cov) "\" width=\"16\" height=\"16\" class=\"cov-icon\">")))

(defn format-char
  [json prof-json]
  (let [char-name (:name json)
        level (:level json)
        realm-slug (:slug (:realm json))
        realm-id (:id (:realm json))
        ilvl-avg (:average_item_level json)
        ilvl-eq (:equipped_item_level json)
        gender (:name (:gender json))
        cls (:name (:character_class json))
        cls-slug (utils/slugify-class cls)
        faction (:name (:faction json))
        guild-json (or (:guild json) {:name "" :id 0})
        guild-name (:name guild-json)
        guild-id (:id guild-json)
        guild-u (guild-url json)
        race (:name (:race json))
        pp1 (prof-primary prof-json 0)
        pp2 (prof-primary prof-json 1)
        ps1 (prof-secondary prof-json 0)
        ps2 (prof-secondary prof-json 1)
        ps3 (prof-secondary prof-json 2)
        cov (get-covenant json)
        faction-slug (string/lower-case faction)
        ps-cooking (sort-secondaries "Cooking" ps1 ps2 ps3)
        ps-fishing (sort-secondaries "Fishing" ps1 ps2 ps3)
        ps-arch (sort-secondaries "Archaeology" ps1 ps2 ps3)
        tpl-prof-icon (str "  <td class=\"c3d-" cls-slug " tiny divider-l\"><img src=\"/img/XXIMG.png\" alt=\"XXNAME\" width=\"16\" height=\"16\"></td>\n")
        tpl-prof-tier (str "  <td class=\"c3d-" cls-slug " tiny XXHIDDEN\" title=\"XXNAME\">XXLEVEL</td>\n")
        tpl-prof-none (str "  <td class=\"c3d-" cls-slug " tiny XXHIDDEN\"></td>\n")
  ]
  (utils/write-cache realm-slug (utils/slugify-guild-char char-name) {:character_class (dissoc (:character_class json) :key)})
  (str
   "<tr>\n"
   "  <td class=\"c3d-" cls-slug "\"><a href=\"" (char-url realm-slug char-name) "\" data-char-id=\"" (:id json) "\">" char-name "</a></td>\n"
   "  <td class=\"c3d-" cls-slug " t-left\">" (format-level level) "</td>\n"
   "  <td class=\"c3d-" cls-slug " smaller\">" (guild-link guild-u guild-name guild-id realm-id) "</td>\n"
   "  <td class=\"c3d-" cls-slug "\">" ilvl-avg "</td>\n"
   "  <td class=\"c3d-" cls-slug "\">" ilvl-eq "</td>\n"
   "  <td class=\"c3d-" cls-slug " t-center divider-r\">" (if (some? ps-arch) (:skill_points ps-arch) "") "</td>\n"
   (if (some? ps-cooking) (format-prof ps-cooking tpl-prof-icon tpl-prof-tier "prof-cooking") (format-empty-prof tpl-prof-none "prof-cooking"))
   (if (some? ps-fishing) (format-prof ps-fishing tpl-prof-icon tpl-prof-tier "prof-fishing") (format-empty-prof tpl-prof-none "prof-fishing"))
   (if (some? pp1) (format-prof pp1 tpl-prof-icon tpl-prof-tier "prof-1") (format-empty-prof tpl-prof-none "prof-1"))
   (if (some? pp2) (format-prof pp2 tpl-prof-icon tpl-prof-tier "prof-2") (format-empty-prof tpl-prof-none "prof-2"))
   "  <td class=\"c3d-" cls-slug " tiny\">" (format-covenant cov) "</td>\n"
   "  <td class=\"c3d-" cls-slug " tiny\">" (:level cov) "</td>\n"
   "  <td class=\"c3d-" cls-slug " tiny\">" (show-icons cls race gender) "</td>\n"
   "  <td class=\"c3d-" faction-slug " tiny t-center\">" (show-faction faction) "</td>\n"
   "</tr>\n")))

(comment
   "  <td class=\"c3d-" cls-slug " t-center divider-r\">" (string/join (map format-profession-rank sorted-ar)) "</td>\n"
   "  <td class=\"c3d-" cls-slug " tiny divider-l\">" (replace-last (string/join tdx (map format-profession-rank sorted-co)) (:orig tdx-fix-divider) (:new tdx-fix-divider)) "</td>\n"
   "  <td class=\"c3d-" cls-slug " tiny divider-l\">" (replace-last (string/join tdx (map format-profession-rank sorted-fi)) (:orig tdx-fix-divider) (:new tdx-fix-divider)) "</td>\n"
   "  <td class=\"c3d-" cls-slug " tiny divider-l\">" (show-profession-icon (get sorted-pp1 (first  prim-keys))) tdx (replace-last (string/join tdx (map format-profession-rank sorted-pp1)) (:orig tdx-fix-divider) (:new tdx-fix-divider)) "</td>\n"
   "  <td class=\"c3d-" cls-slug " tiny divider-l\">" (show-profession-icon (get sorted-pp2 (second prim-keys))) tdx (string/join tdx (map format-profession-rank sorted-pp2)) "</td>\n"
   )

(defn fmt-rep
  [faction-rep all-reps tag cache]
  (let [faction-id (:id (:faction faction-rep))
        faction-name (:name (:faction faction-rep))]
    (if (some #{faction-id} all-reps)
      (let [standings (map string/lower-case (vals defs/bn-reputation-standing))
            svals (:standing faction-rep)
            standing (nth standings (:tier svals))
            standing-cap (utils/uc-first standing)
            char-name (:name cache)
            cls (or (:character_class cache) default-cache)]
        (str "<tr class=\"rep rep-" standing "\" data-id=\"" faction-id "\" data-value=\"" faction-name "\" data-tag=\"" tag "\">"
             "<td class=\"c3d-" (utils/slugify-class (:name cls)) "\">" char-name "</td>"
             "<td>" faction-name "</td>"
             "<td>" tag "</td>"
             "<td>" standing-cap "</td>"
             "<td>" (:value svals) "/" (:max svals) "</td>"
             "</tr>\n"))
      "")))

(defn format-reps
  [json _]
  (let [char (:character json)
        char-name (:name char)
        realm (:slug (:realm char))
        reps (:reputations json)
        cache (assoc (utils/read-cache realm char-name) :name char-name)
        tww-reps      (keep-indexed #(if (even? %1) %2) (:tww defs/bn-reputations))
        df-reps       (keep-indexed #(if (even? %1) %2) (:df defs/bn-reputations))
        sl-reps       (keep-indexed #(if (even? %1) %2) (:sl defs/bn-reputations))
        bfa-reps      (keep-indexed #(if (even? %1) %2) (:bfa defs/bn-reputations))
        legion-reps   (keep-indexed #(if (even? %1) %2) (:legion defs/bn-reputations))
        wod-reps      (keep-indexed #(if (even? %1) %2) (:wod defs/bn-reputations))
        horde-reps    (keep-indexed #(if (even? %1) %2) (:horde defs/bn-reputations))
        alliance-reps (keep-indexed #(if (even? %1) %2) (:alliance defs/bn-reputations))
        check-reps    (map #(:id (:faction %)) reps)
        ; 72 is Stormwind
        faction-reps  (if (some #{72} check-reps) alliance-reps horde-reps)
        div-rep "<tr class=\"rep rep-expansion\"><td colspan=\"5\"><hr></td></tr>\n"
        div-chr "<tr class=\"rep\"><td colspan=\"5\"><hr></td></tr>\n"]
    (apply str
      (apply str (map #(fmt-rep %1 tww-reps "TWW" cache) reps))
      div-rep
      ;(apply str (map #(fmt-rep %1 df-reps "DF" cache) reps))
      div-rep
      (apply str (map #(fmt-rep %1 sl-reps "SL" cache) reps))
      div-rep
      (apply str (map #(fmt-rep %1 bfa-reps "BFA" cache) reps))
      div-rep
      (apply str (map #(fmt-rep %1 legion-reps "Legion" cache) reps))
      div-rep
      (apply str (map #(fmt-rep %1 wod-reps "WOD" cache) reps))
      div-rep
      (apply str (map #(fmt-rep %1 faction-reps "Vanilla" cache) reps))
      div-chr)))

(defn format-achi
  [json _]
  (ppe json))

(defn wrapper
  [cname read-fn1 read-fn2 format-fn]
  "### = blank line, # = ignore line"
  (if (string/starts-with? cname "###")
    "<tr><td></td></tr>"
    (if (string/starts-with? cname "#")
      ""
      (let [[realm charname] (string/split cname #";")
             r-s (utils/slugify-realm realm)
             c-s (utils/slugify-guild-char charname)]
        (ppe (str "Next: " cname))
        (let [json2 (try (read-fn2 config/current-region r-s c-s (config/current-params)) (catch Exception ex (do (ppe (str "Wx2" ex)) {})))]
        (try
          (let [json1 (read-fn1 config/current-region r-s c-s (config/current-params))]
            ;(ppe json1)
            (format-fn json1 json2))
          (catch Exception ex (do (ppe (str "Wx1" ex)) (comment (st/print-stack-trace ex))) "x")))))))

(defn dummy [_ _ _ _] {})

(defn read-list [s]
  (let [file (str config/data-dir "/input/" (name s) ".txt")]
    (ppe (str "# read from " file))
    (try
      (with-open [rdr (clojure.java.io/reader file)] (doall (line-seq rdr)))
      (catch Exception _ []))))

(defn run-wow-chars [name]
  (let [read-fn1  utils/read-char-profile
        read-fn2  utils/read-char-professions
        format-fn format-char
        list (read-list name)]
    (ppe (str "# " (string/join " " list)))
    (apply str (map #(wrapper % read-fn1 read-fn2 format-fn) list))))

(defn run-wow-reps [name]
  (let [read-fn1  utils/read-char-reputations
        read-fn2  dummy
        format-fn format-reps
        list (read-list name)]
    (apply str (map #(wrapper % read-fn1 read-fn2 format-fn) list))))

(defn run-wow-achi [name]
  (let [read-fn1  utils/read-char-achievements
        read-fn2  dummy
        format-fn format-achi
        list (read-list name)]
    (apply str (map #(wrapper % read-fn1 read-fn2 format-fn) list))))

(defn reader [name]
  (slurp (str config/data-dir "/templates/" name ".html")))

(defn writer [f fnx s]
  (if (empty? s)
    (let [err "List empty, nothing written."]
      (ppe err)
      err)
    (let [ok (str "Ok: " (name f))
          add (if (= :reps fnx) "-rep" "")
          out-dir (let [o config/output-dir] (do (.mkdirs (java.io.File. o)) o))]
      (do (spit (str out-dir "/" (name f) add ".html") s) ok))))

(defn full-wow [name fnx hname fname]
  (let [data   (if (= :reps fnx) (run-wow-reps name) (if (= :achi fnx) (run-wow-achi name) (run-wow-chars name)))
        now    (.format (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm:ss zzz") (java.util.Date.))
        footer (string/replace (reader fname) #"<div class=\"updated\"></div>" (str "<div class=\"updated\">Last update: " now "</div>"))
        all    (if (empty? data) "" (str (reader hname) data footer))]
    (writer name fnx all)))
