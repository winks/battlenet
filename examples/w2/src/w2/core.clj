(ns w2.core
  (:require [clojure.string :as string])
  (:require [w2.config :as config])
  (:require [w2.defs :as defs])
  (:require [w2.utils :as utils]))

(def current-max-level 120)
(def last-max-level 110)
(def exp-max-values {:kultiran 150 :legion 100
                     :draenor 100  :pandaria 75 :cataclysm 75
                     :northrend 75 :outland 75  :classic 300})
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
  [url name]
  (if (empty? url)
    ""
    (str "<a href=\"" url "\">" name "</a>")))

(defn format-level [lvl]
  (if (= current-max-level lvl)
    (str "<span class=\"prof-max\">" lvl "</span>")
    (if (= last-max-level lvl)
      (str "<span class=\"prof-last-max\">" lvl "</span>")
      (str "<span>" lvl "</span>"))))

(defn format-char
  [json]
  (let [char-name (:name json)
        level (:level json)
        realm-slug (:slug (:realm json))
        ilvl-avg (:average_item_level json)
        ilvl-eq (:equipped_item_level json)
        gender (:name (:gender json))
        cls (:name (:character_class json))
        cls-slug (utils/slugify-class cls)
        faction (:name (:faction json))
        guild-json (or (:guild json) {:name ""})
        guild-name (:name guild-json)
        guild-u (guild-url json)
        race (:name (:race json))
        faction-slug (string/lower-case faction)
  ]
  (utils/write-cache realm-slug (utils/slugify-guild-char char-name) {:character_class (dissoc (:character_class json) :key)})
  (str
   "<tr>\n"
   "  <td class=\"cls-3d-" cls-slug "\"><a href=\"" (char-url realm-slug char-name) "\">" char-name "</a></td>\n"
   "  <td class=\"cls-3d-" cls-slug "\">" (format-level level) "</td>\n"
   "  <td class=\"cls-3d-" cls-slug "\">" (guild-link guild-u guild-name) "</td>\n"
   "  <td class=\"cls-3d-" cls-slug "\">" ilvl-avg "</td>\n"
   "  <td class=\"cls-3d-" cls-slug "\">" ilvl-eq "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " t-center divider-r\">" " " "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny divider-l\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny divider-l\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny divider-l\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny divider-l\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
   "  <td class=\"cls-3d-" cls-slug " tiny\">" "." "</td>\n"
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
  [json]
  (let [char (:character json)
        char-name (:name char)
        realm (:slug (:realm char))
        reps (:reputations json)
        cache (assoc (utils/read-cache realm char-name) :name char-name)
        bfa-reps      (keep-indexed #(if (even? %1) %2) (:bfa defs/bn-reputations))
        legion-reps   (keep-indexed #(if (even? %1) %2) (:legion defs/bn-reputations))
        wod-reps      (keep-indexed #(if (even? %1) %2) (:wod defs/bn-reputations))
        horde-reps    (keep-indexed #(if (even? %1) %2) (:horde defs/bn-reputations))
        alliance-reps (keep-indexed #(if (even? %1) %2) (:alliance defs/bn-reputations))
        check-reps    (map #(:id (:faction %)) reps)
        ; 72 is Stormwind
        faction-reps  (if (some #{72} check-reps) alliance-reps horde-reps)
        div "<tr><td colspan=\"4\"></td></tr>\n"]
    (apply str
      (apply str (map #(fmt-rep %1 bfa-reps "bfa" cache) reps))
      div
      (apply str (map #(fmt-rep %1 legion-reps "legion" cache) reps))
      div
      (apply str (map #(fmt-rep %1 wod-reps "wod" cache) reps))
      div
      (apply str (map #(fmt-rep %1 faction-reps "vanilla" cache) reps))
      div)))

(defn wrapper
  [cname read-fn format-fn]
  "### = blank line, # = ignore line"
  (if (string/starts-with? cname "###")
    "<tr><td></td></tr>"
    (if (string/starts-with? cname "#")
      ""
      (let [[realm charname] (string/split cname #";")
             r-s (utils/slugify-realm realm)
             c-s (utils/slugify-guild-char charname)]
        (pp (str "Next: " cname))
        (try
          (if-let [json (read-fn config/current-region r-s c-s config/current-params)]
            (format-fn json))
          (catch Exception ex (.println *err* ex) "x"))))))

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

(defn -main [& m]
  (if (.equals "d3" (first m))
    (print (apply str (map run-d3 config/current-tags)))
    (let [read-fn   (if (.equals "rep" (first m)) utils/read-char-reputations utils/read-char-profile)
          format-fn (if (.equals "rep" (first m)) format-reps format-char)]
      (if-let [xs (System/getenv "FILENAME")]
        (print (apply str (map #(wrapper % read-fn format-fn) (get config/current-chars (keyword (string/replace xs ".html" ""))))))
        (print (apply str (map #(wrapper % read-fn format-fn) (get config/current-chars :test))))))))