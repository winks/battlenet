(ns wowexample.core
  (:require [clojure.string :as string])
  (:require [battlenet.wow.defs :as defs])
  (:require [battlenet.wow.network :as network])
  (:require [battlenet.wow.tools :as tools])
  (:require [battlenet.tools :as wto])
  (:require [wowexample.config :as config]))

(def current-max-level 120)
(def last-max-level 110)
(def exp-max-values {:kultiran 150 :legion 100 :draenor 100 :pandaria 75 :cataclysm 75 :northrend 75 :outland 75 :classic 300})

(defn uc-first [s]
  (if (empty? s)
  ""
  (str (string/upper-case (subs s 0 1)) (string/lower-case (subs s 1)))))

(defn slugify-guild [s]
  (string/replace (string/replace s "'" "") " " "_"))

(defn slugify-realm [s]
  (string/lower-case (string/replace s " " "-")))

(defn slugify-class [s]
(if (empty? s)
  ""
  (string/lower-case (string/replace (string/replace s " " "") "'" ""))))

(defn guild-link [realm guild character]
  (let [g (slugify-guild guild)
        r (string/lower-case realm)
        gurl (tools/create-url-guild config/current-region "wow" defs/bn-path-guild r g)]
    (str "<a href=\""  (string/replace gurl ".api.blizzard.com" ".battle.net") "/roster?character=" character "\">" guild "</a>")))

(defn replace-last [s match replacement]
  "I regret nothing."
  (string/reverse (string/replace-first (string/reverse s) (string/reverse match) (string/reverse replacement))))

(defn professions-primary [c]
  (:primary (:professions c)))

(defn professions-secondary [c]
  (:secondary (:professions c)))

(defn find-primary-profs [x]
  (into {} (filter #(> (second %) 0)
    (into {} (for [[k v] x]
      (let [r (get (get-in x [k k]) :rank 0)]
        [k r]))))))

(defn append-to-key [k suffix]
  ".e.g (append-to-key :foo 'bar') -> :foobar"
  (let [n (if (nil? k) "" (name k))
        s (str n suffix)
        kn (keyword s)]
    kn))

(defn index-list [x]
  (into {} (for [v x]
    [(:id v) (dissoc v :recipes)])))

(defn sorted-prof [m k a]
  (into {}
    (for [kx a]
      (let [kn (append-to-key kx (name k))]
        [kn (get-in m [k kn]) ]))))

(defn show-profession-icon [m]
  "e.g. {:id 1 :icon 'foo' :name 'X'} -> <img..>"
  (if
    (empty? m)
    ""
    (let [n (:icon m)
          nx (if (empty? n) (string/lower-case (:name m)) n)]
    (str "<img src=\"/img/" nx ".png\" alt=\"" (:name m) "\" title=\"" (:name m) "\" width=\"16\" height=\"16\">"))))

(defn format-profession-rank [[k m]]
  "e.g. {:id 1 :icon 'foo' :rank 23 :name 'X'} -> <span title=X>23</span>"
  (if (empty? m)
  ""
  (if-let [n (:name m)]
  (if-let [r (:rank m)]
  (if-let [mx (:max m)]
    (let [is-max (if (< (int r) (int mx)) "" " class=\"prof-max\"")]
      (str "<span title=\"" n "\"" is-max ">" (if (< (int r) 1) "" r) "</span>")))))))

(defn format-level [lvl]
  (if (= current-max-level lvl)
    (str "<span class=\"prof-max\">" lvl "</span>")
    (if (= last-max-level lvl)
      (str "<span class=\"prof-last-max\">" lvl "</span>")
      (str "<span>" lvl "</span>"))))

(defn format-profs [base new-data]
  (into {} (for [[k v] base]
    [k (into {} (for [[k v] v]
      (let [id (:id v)
            datakey (get new-data id {})]
          [k (merge v datakey)])))])))

(defn show-faction [k]
  (let [html "<img src=\"/img/FACTION.png\" width=\"16\" height=\"16\" alt=\"FACTION\" />"]
    (string/replace html #"FACTION" (name k))))

(defn show-icons [cls c]
  (let [class-ico (slugify-class cls)
        class-ico-full (str "IconLarge_" (uc-first class-ico) ".png")
        race (uc-first (slugify-class (get defs/bn-races (or (:race c) 0))))
        gender (if (< 0 (:gender c)) "Female" "Male")
        race-icon (str "IconLarge_" race "_" gender ".png")
        html-class (str "<img src=\"/img/" class-ico-full "\" width=\"16\" height=\"16\" alt=\"" cls "\" />")
        html-race (str "<img src=\"/img/" race-icon "\" width=\"16\" height=\"16\" alt=\"" race " " gender "\" />")]
    (str html-race html-class)))

(defn faction [s]
  (let [x (str s)]
    (if (= "0" x) :alliance (if (= "1" x) :horde :unknown))))

(defn fmt-char [c]
  (if-let [cls-id (:class c)]
  (let [cls (get defs/bn-classes cls-id)
        guild (:name (:guild c))
        ilvl (:averageItemLevel (:items c))
        ilvl-eq (:averageItemLevelEquipped (:items c))
        guild-x (if (seq guild) (guild-link (:realm (:guild c)) guild (:name c)) guild)
        pp (professions-primary c)
        ps (professions-secondary c)
        ppf (format-profs defs/bn-professions-primary (index-list pp))
        psf (format-profs defs/bn-professions-secondary (index-list ps))
        sorted-ar (sorted-prof psf :archaeology defs/bn-professions-order)
        sorted-co (sorted-prof psf :cooking defs/bn-professions-order)
        sorted-fi (sorted-prof psf :fishing defs/bn-professions-order)
        ffpp (find-primary-profs ppf)
        prim-keys (or (keys ffpp) [:x :y])
        sorted-pp1 (sorted-prof ppf (first  prim-keys) defs/bn-professions-order)
        sorted-pp2 (sorted-prof ppf (second prim-keys) defs/bn-professions-order)
        tdx-fix-divider {:orig " tiny\">", :new " tiny divider-r\">"}
        tdx (str "</td><td class=\"cls-3d-" (slugify-class cls) (:orig tdx-fix-divider))
        faction (faction (:faction c))]
        (comment
        (.println *err* c)
        (.println *err* "---"))
  (str
   "<tr>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\"><a href=\"{{purl}}\">" (:name c) "</a></td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" (format-level (:level c)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" guild-x "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" ilvl "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" ilvl-eq "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " t-center divider-r\">" (string/join (map format-profession-rank sorted-ar)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " tiny divider-l\">" (replace-last (string/join tdx (map format-profession-rank sorted-co)) (:orig tdx-fix-divider) (:new tdx-fix-divider)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " tiny divider-l\">" (replace-last (string/join tdx (map format-profession-rank sorted-fi)) (:orig tdx-fix-divider) (:new tdx-fix-divider)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " tiny divider-l\">" (show-profession-icon (get sorted-pp1 (first  prim-keys))) tdx (replace-last (string/join tdx (map format-profession-rank sorted-pp1)) (:orig tdx-fix-divider) (:new tdx-fix-divider)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " tiny divider-l\">" (show-profession-icon (get sorted-pp2 (second prim-keys))) tdx (string/join tdx (map format-profession-rank sorted-pp2)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " tiny\">" (show-icons cls c) "</td>\n"
   "  <td class=\"cls-3d-" (name faction) " tiny t-center\">" (show-faction faction) "</td>\n"
   "</tr>\n"))))

(defn show-char [cname]
  "### = blank line, # = ignore line"
  (.println *err* (str "Next: " cname))
  (if (string/starts-with? cname "###")
    "<tr><td></td></tr>"
    (if (string/starts-with? cname "#")
      ""
      (let [[realm name] (string/split cname #";")
             params (str "fields=guild,items,professions&" config/current-params)
             purl (tools/create-url-character2 defs/bn-wow-char-url config/current-locale (slugify-realm realm) name)]
        (try
          (if-let [chara (network/read-remote-character config/current-region (slugify-realm realm) name params)]
            (->
              (fmt-char chara)
              (string/replace "{{purl}}" purl)))
          (catch Exception e ""))))))

(defn fmt-rep [m reps c bl tag]
  (.println *err* (str (:id m) bl))
  (if (and (some #{(:id m)} reps) (not (some #(= (:id m) %) bl)))
    (let [standings (map string/lower-case (vals defs/bn-reputation-standing))
          sval (:standing m)
          standing (nth standings (int sval))
          standing-cap (uc-first standing)
          cls-id (:class c)
          cls (get defs/bn-classes cls-id)]
(comment
(.println *err* (str (vals defs/bn-reputation-standing)))
(.println *err* (str (keys defs/bn-reputation-standing)))
(.println *err* (first standings))
(.println *err* sval)
(.println *err* standing)
(.println *err* (vals  m))
(.println *err* (keys  m)))
      (str "<tr class=\"rep-" standing "\" data-id=\"" (:id m) "\" data-value=\"" (:name m) "\" data-tag=\"" tag "\">"
           "<td class=\"cls-3d-" (slugify-class cls) "\">" (:name c) "</td>"
           "<td>" (:name m) "</td>"
           "<td>" tag "</td>"
           "<td>" standing-cap "</td>"
           "<td>" (:value m) "/" (:max m) "</td>"
           "</tr>\n"))
    ""))

(defn fmt-reps [c]
  (let [reps (:reputation c)
        bfa-reps      (keep-indexed #(if (even? %1) %2) (:bfa defs/bn-reputations))
        legion-reps   (keep-indexed #(if (even? %1) %2) (:legion defs/bn-reputations))
        wod-reps      (keep-indexed #(if (even? %1) %2) (:wod defs/bn-reputations))
        horde-reps    (keep-indexed #(if (even? %1) %2) (:horde defs/bn-reputations))
        alliance-reps (keep-indexed #(if (even? %1) %2) (:alliance defs/bn-reputations))
        faction-reps  (if (= 0 (:faction c)) alliance-reps horde-reps)
        blacklist     (if (= 0 (:faction c)) (:horde-only defs/bn-reputations) (:alliance-only defs/bn-reputations))
        div "<tr><td colspan=4></td></tr>\n"
        r (take 13 reps)]
  (.println *err* reps)
    (apply str
      (apply str (map #(fmt-rep %1 bfa-reps c blacklist "bfa") reps))
      div
      (apply str (map #(fmt-rep %1 legion-reps c blacklist "legion") reps))
      div
      (apply str (map #(fmt-rep %1 wod-reps c blacklist "wod") reps))
      div
      (apply str (map #(fmt-rep %1 faction-reps c blacklist "vanilla") reps))
      div)))

(defn show-rep [cname]
  (.println *err* (str "Next: " cname))
  (if (= \# (first cname))
      "<tr><td></td></tr>"
      (let [[realm name] (string/split cname #";")
             params (str "fields=reputation&" config/current-params)
             purl (tools/create-url-character2 defs/bn-wow-char-url config/current-locale (slugify-realm realm) name)
             purl2 (tools/create-url-character config/current-region "wow" defs/bn-path-character (slugify-realm realm) name)]
        (if-let [chara (network/read-remote-character config/current-region (slugify-realm realm) name params)]
          (->
            (fmt-reps chara)
)))))

(defn -main [& m]
  (if (.equals "rep" (first m))
      (if-let [xs (System/getenv "FILENAME")]
        (print (apply str (map show-rep (get config/current-chars (keyword (string/replace xs ".html" ""))))))
        (print (apply str (map show-rep (get config/current-chars :test)))))
      (if-let [xs (System/getenv "FILENAME")]
        (print (apply str (map show-char (get config/current-chars (keyword (string/replace xs ".html" ""))))))
        (print (apply str (map show-char (get config/current-chars :test)))))))
