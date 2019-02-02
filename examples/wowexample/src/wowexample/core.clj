(ns wowexample.core
  (:require [clojure.string :as string])
  (:require [battlenet.wow.defs :as defs])
  (:require [battlenet.wow.network :as network])
  (:require [battlenet.wow.tools :as tools])
  (:require [battlenet.tools :as wto])
  (:require [wowexample.config :as config]))

(defn slugify-guild [s]
  (string/replace (string/replace s "'" "") " " "_"))

(defn slugify-realm [s]
  (string/lower-case (string/replace s " " "-")))

(defn slugify-class [s]
  (string/lower-case (string/replace s " " "")))

(defn guild-link [realm guild character]
  (let [g (slugify-guild guild)
        r (string/lower-case realm)
        gurl (tools/create-url-guild config/current-region "wow" defs/bn-path-guild r g)]
    (str "<a href=\""  (string/replace gurl ".api.blizzard.com" ".battle.net") "/roster?character=" character "\">" guild "</a>")))

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

(defn format-prof-name [m]
  (if
    (empty? m)
    ""
    (let [n (:icon m)
          nx (if (empty? n) (string/lower-case (:name m)) n)]
    (str "<img src=\"/img/" nx ".png\" alt=\"" (:name m) "\" title=\"" (:name m) "\" width=\"16\" height=\"16\">"))))

(defn format-single [[k m]]
  (if-let [n (:name m)]
  (str "<span title=\"" n "\">" (:rank m) "</span>")))

(defn format-profs [base new-data]
  (into {} (for [[k v] base]
    [k (into {} (for [[k v] v]
      (let [id (:id v)
            datakey (get new-data id {})]
          [k (merge v datakey)])))])))

(defn show-faction [k]
  (let [html "<img src=\"/img/FACTION.png\" width=\"16\" height=\"16\" alt=\"FACTION\" />"]
    (string/replace html #"FACTION" (name k))))

(defn faction [s]
  (let [x (str s)]
    (if (= "0" x) :alliance (if (= "1" x) :horde :unknown))))

(defn fmt-char [c]
  (let [cls-id (:class c)
        cls (get defs/bn-classes cls-id)
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
        tdx (str "</td><td class=\"cls-3d-" (slugify-class cls) " tiny\">")
        faction (faction (:faction c))]
        (comment
        (.println *err* c)
        (.println *err* "---"))
  (str
   "<tr>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\"><a href=\"{{purl}}\">" (:name c) "</a></td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" (:level c) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" guild-x "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" ilvl "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" ilvl-eq "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " t-center\">" (string/join (map format-single sorted-ar)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " tiny divider\">" (string/join tdx (map format-single sorted-co)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " tiny divider\">" (string/join tdx (map format-single sorted-fi)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " tiny divider\">" (format-prof-name (get sorted-pp1 (first  prim-keys))) "</td><td class=\"cls-3d-" (slugify-class cls) " tiny\">" (string/join tdx (map format-single sorted-pp1)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " tiny divider\">" (format-prof-name (get sorted-pp2 (second prim-keys))) "</td><td class=\"cls-3d-" (slugify-class cls) " tiny\">" (string/join tdx (map format-single sorted-pp2)) "</td>\n"
   "  <td class=\"cls-3d-" (name faction) " tiny t-center\">" (show-faction faction) "</td>\n"
   "</tr>\n")))

(defn show-char [cname]
  (.println *err* (str "Next: " cname))
  (if (= \# (first cname))
      "<tr><td></td></tr>"
      (let [[realm name] (string/split cname #";")
             params (str "fields=guild,items,professions&" config/current-params)
             purl (tools/create-url-character2 defs/bn-wow-char-url config/current-locale (slugify-realm realm) name)
             purl2 (tools/create-url-character config/current-region "wow" defs/bn-path-character (slugify-realm realm) name)]
        (if-let [chara (network/read-remote-character config/current-region (slugify-realm realm) name params)]
          (->
            (fmt-char chara)
            (string/replace "{{purl}}" purl))))))

(defn fmt-rep [m reps c]
  (if (some #{(:id m)} reps)
    (let [standings (map string/lower-case (vals defs/bn-reputation-standing))
          standing (get standings (:standing m))
          standing-cap (str (string/upper-case (subs standing 0 1)) (subs standing 1))
          cls-id (:class c)
          cls (get defs/bn-classes cls-id)]
      (str "<tr class=\"rep-" standing "\" data-id=\"" (:id m) "\" data-value=\"" (:name m) "\">"
           "<td class=\"cls-3d-" (slugify-class cls) "\">" (:name c) "</td>"
           "<td>" (:name m) "</td>"
           "<td>" standing-cap "</td>"
           "<td>" (:value m) "/" (:max m) "</td>"
           "</tr>\n"))
    ""))

(defn fmt-reps [c]
  (let [reps (:reputation c)
        bfa-reps (keep-indexed #(if (even? %1) %2) (:bfa defs/bn-reputations))
        legion-reps (keep-indexed #(if (even? %1) %2) (:legion defs/bn-reputations))
        wod-reps (keep-indexed #(if (even? %1) %2) (:wod defs/bn-reputations))
        div "<tr><td colspan=4></td></tr>\n"
        r (take 13 reps)]
    (apply str
      (apply str (map #(fmt-rep %1 bfa-reps c) reps))
      div
      (apply str (map #(fmt-rep %1 legion-reps c) reps))
      div
      (apply str (map #(fmt-rep %1 wod-reps c) reps))
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
