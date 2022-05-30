(ns w2.core
  (:gen-class)
  (:require [clojure.string :as string])
  (:require [clojure.stacktrace :as st])
  (:require [w2.config :as config])
  (:require [w2.defs :as defs])
  (:require [w2.utils :as utils]))

(def current-max-level 60)
(def last-max-level 50)
(def exp-max-values-all {:kultiran 175 :legion 100
                         :draenor 100  :pandaria 75 :cataclysm 75
                         :northrend 75 :outland 75  :classic 300})
(def exp-max-values-sl {:alchemy 175 :fishing 200 :cooking 75
                        :mining 150 :herbalism 150 :skinning 150
                        :enchanting 100 :inscription 100 :leatherworking 100
                        :blacksmithing 100 :engineering 100 :jewelcrafting 100
                        :tailoring 100})
(def default-cache {:character_class {:name "Warrior", :id 1}})

(defn ppe [s]
  (.println *err* (str "#ERR# " s)))

(defn pp [s]
  (.println *err* s))

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

(defn format-prof-tpl [xx tpl1 tpl2 y]
  (string/replace (string/replace (string/replace tpl2 "XXLEVEL" (str (:skill_points xx))) "XXNAME" (:name xx)) "XXHIDDEN" y))

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
    (apply str (map #(format-prof-tpl % tpl1 tpl2 "") [prof-latest]))
    (apply str (map #(format-prof-tpl % tpl1 tpl2 (str "hidden-prof " css-class)) prof-rest)))))

(defn format-empty-prof [tpl css-class]
  (let [rest (string/replace tpl "XXHIDDEN" (str "hidden-prof " css-class))]
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
    (str "&nbsp;<img src=\"/img/covenant_" (:id cov) ".png\" alt=\"" (:name cov) "\" title=\"" (:name cov) "\" width=\"16\" height=\"16\" class=\"cov-icon\">" (:level cov))))

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
        tpl-prof-icon (str "  <td class=\"cls-3d-" cls-slug " tiny divider-l\"><img src=\"/img/XXIMG.png\" alt=\"XXNAME\" width=\"16\" height=\"16\"></td>\n")
        tpl-prof-tier (str "  <td class=\"cls-3d-" cls-slug " tiny XXHIDDEN\" title=\"XXNAME\">XXLEVEL</td>\n")
        tpl-prof-none (str "  <td class=\"cls-3d-" cls-slug " tiny XXHIDDEN\"></td>\n")
  ]
  (utils/write-cache realm-slug (utils/slugify-guild-char char-name) {:character_class (dissoc (:character_class json) :key)})
  (str
   "<tr>\n"
   "  <td class=\"cls-3d-" cls-slug "\"><a href=\"" (char-url realm-slug char-name) "\" data-char-id=\"" (:id json) "\">" char-name "</a></td>\n"
   "  <td class=\"cls-3d-" cls-slug " t-left\">" (format-level level) (format-covenant cov) "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " smaller\">" (guild-link guild-u guild-name guild-id realm-id) "</td>\n"
   "  <td class=\"cls-3d-" cls-slug "\">" ilvl-avg "</td>\n"
   "  <td class=\"cls-3d-" cls-slug "\">" ilvl-eq "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " t-center divider-r\">" (if (some? ps-arch) (:skill_points ps-arch) "") "</td>\n"
   (if (some? ps-cooking) (format-prof ps-cooking tpl-prof-icon tpl-prof-tier "prof-cooking") (format-empty-prof tpl-prof-none "prof-cooking"))
   (if (some? ps-fishing) (format-prof ps-fishing tpl-prof-icon tpl-prof-tier "prof-fishing") (format-empty-prof tpl-prof-none "prof-fishing"))
   (if (some? pp1) (format-prof pp1 tpl-prof-icon tpl-prof-tier "prof-1") (format-empty-prof tpl-prof-none "prof-1"))
   (if (some? pp2) (format-prof pp2 tpl-prof-icon tpl-prof-tier "prof-2") (format-empty-prof tpl-prof-none "prof-2"))
   "  <td class=\"cls-3d-" cls-slug " tiny\">" (show-icons cls race gender) "</td>\n"
   "  <td class=\"cls-3d-" faction-slug " tiny t-center\">" (show-faction faction) "</td>\n"
   "</tr>\n")))

(comment
   "  <td class=\"cls-3d-" cls-slug " t-center divider-r\">" (string/join (map format-profession-rank sorted-ar)) "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny divider-l\">" (replace-last (string/join tdx (map format-profession-rank sorted-co)) (:orig tdx-fix-divider) (:new tdx-fix-divider)) "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny divider-l\">" (replace-last (string/join tdx (map format-profession-rank sorted-fi)) (:orig tdx-fix-divider) (:new tdx-fix-divider)) "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny divider-l\">" (show-profession-icon (get sorted-pp1 (first  prim-keys))) tdx (replace-last (string/join tdx (map format-profession-rank sorted-pp1)) (:orig tdx-fix-divider) (:new tdx-fix-divider)) "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny divider-l\">" (show-profession-icon (get sorted-pp2 (second prim-keys))) tdx (string/join tdx (map format-profession-rank sorted-pp2)) "</td>\n"
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
        (str "<tr class=\"rep-" standing "\" data-id=\"" faction-id "\" data-value=\"" faction-name "\" data-tag=\"" tag "\">"
             "<td class=\"cls-3d-" (utils/slugify-class (:name cls)) "\">" char-name "</td>"
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
        sl-reps       (keep-indexed #(if (even? %1) %2) (:sl defs/bn-reputations))
        bfa-reps      (keep-indexed #(if (even? %1) %2) (:bfa defs/bn-reputations))
        legion-reps   (keep-indexed #(if (even? %1) %2) (:legion defs/bn-reputations))
        wod-reps      (keep-indexed #(if (even? %1) %2) (:wod defs/bn-reputations))
        horde-reps    (keep-indexed #(if (even? %1) %2) (:horde defs/bn-reputations))
        alliance-reps (keep-indexed #(if (even? %1) %2) (:alliance defs/bn-reputations))
        check-reps    (map #(:id (:faction %)) reps)
        ; 72 is Stormwind
        faction-reps  (if (some #{72} check-reps) alliance-reps horde-reps)
        div-rep "<tr class=\"rep-expansion\"><td colspan=\"5\"><hr></td></tr>\n"
        div-chr "<tr><td colspan=\"5\"><hr></td></tr>\n"]
    (apply str
      (apply str (map #(fmt-rep %1 sl-reps "sl" cache) reps))
      div-rep
      (apply str (map #(fmt-rep %1 bfa-reps "bfa" cache) reps))
      div-rep
      (apply str (map #(fmt-rep %1 legion-reps "legion" cache) reps))
      div-rep
      (apply str (map #(fmt-rep %1 wod-reps "wod" cache) reps))
      div-rep
      (apply str (map #(fmt-rep %1 faction-reps "vanilla" cache) reps))
      div-chr)))

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
        (pp (str "Next: " cname))
        (let [json2 (try (read-fn2 config/current-region r-s c-s config/current-params) (catch Exception ex {}))]
        (try
          (let [json1 (read-fn1 config/current-region r-s c-s config/current-params)]
            ;(.println *err* json1)
            (format-fn json1 json2))
          (catch Exception ex (do (.println *err* ex) (comment (st/print-stack-trace ex))) "x")))))))

(defn format-d3-char
  [c tag guild]
  (let [url (-> defs/bn-d3-profile-url
                (string/replace "{region}" config/current-region)
                (string/replace "{account}" (utils/slugify-d3-profile tag)))]
    (str
      "<tr class=\"" (:class c) "\">\n  <td>"
      "<a href=\"" url "hero/" (:id c) "\">"
      (:name c)
      "</a>"
      "</td>\n  <td>"
      (:level c) " (" (:paragonLevel c)  ")"
      "</td>\n  <td>"
      (let [p (string/split (:class c) #"-")]
        (string/join " " (map (fn [x] (str (string/upper-case (first x)) (apply str (rest x)) )) p)))
      "</td>\n  <td>"
      (if (= 0 (:gender c)) "male" "female")
      "</td>\n  <td>"
      "<a href=\"" url "\">"
      tag
      "</a>"
      "</td>\n  <td>"
      (if (true? (:hardcore c)) "HC" "")
      (if (true? (:dead c)) " †" "")
      "</td>\n</tr>\n")))

(defn format-d3-profile
  [json]
  (apply str (map #(format-d3-char % (:battleTag json) (:guildName json)) (:heroes json))))

(defn run-d3
  [tag]
  (if-let [json (utils/read-d3-profile config/current-region (string/replace tag "#" "%23") config/current-params)]
    (format-d3-profile json)))

(defn run-all-d3 []
  (apply str (map run-d3 config/current-tags)))

(defn dummy [_ _ _ _] {})

(defn run-wow [m]
  (let [read-fn1  (if (.equals "wow-rep" (first m)) utils/read-char-reputations utils/read-char-profile)
        read-fn2  (if (.equals "wow-rep" (first m)) dummy utils/read-char-professions)
        format-fn (if (.equals "wow-rep" (first m)) format-reps format-char)
        xkey      (if-let [xs (second m)] xs config/current-list)]
    (apply str (map #(wrapper % read-fn1 read-fn2 format-fn) (get config/current-chars xkey)))))

(defn reader [name]
  (let [x (slurp (str "resources/" name ".html"))]
    x))

(defn writer [f s]
  (spit (str config/output-dir "/" (name f) ".html") s))

(defn ext-wow [m]
  (let [xkey   (if-let [xs (second m)] xs config/current-list)
        hname  (if (.equals "wow-rep" (first m)) "h1r" "head")
        fname  (if (.equals "wow-rep" (first m)) "f1r" "foot")
        data   (run-wow m)
        now    (.format (java.text.SimpleDateFormat. "yyyy-MM-dd HH:mm:ss zzz") (java.util.Date.))
        footer (string/replace (reader fname) #"<div class=\"updated\"></div>" (str "<div class=\"updated\">Last update: " now "</div>"))
        all    (str (reader hname) data footer)]
    (writer xkey all)))

(defn -main [& m]
  (cond
    (.equals "d3"  (first m)) (print (run-all-d3))
    (.equals "foo" (first m)) (ext-wow m)
    :else          (print (run-wow m))))
