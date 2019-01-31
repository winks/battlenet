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

(def standings
  ["hated" "hostile" "unfriendly" "neutral" "friendly" "honored" "revered" "exalted"])

(def all-reps {
  :bfa [
    2103 "Zandalari Empire"
    2156 "Talanji's Expedition"
    2157 "The Honorbound"
    2158 "Voldunai"
    2159 "7th Legion"
    2160 "Proudmoore's Admiralty"
    2161 "Order of Embers"
    2162 "Storm's Wake"
    2163 "Tortollan Seekers"
    2164 "Champions of Azeroth"]
  :legion [
    1828 "Highmountain Tribe"
    1859 "The Nightfallen"
    1883 "Dreamweavers"
    1894 "The Wardens"
    1900 "Court of Farondis"
    2165 "Army of the Light"
    2170 "Argussian Reach"]
  :legion2 [
    1947 "Illidari"
    1888 "Jandvik Vrykul"
    1899 "Moonguard"
    2018 "Talon's Vengeance"
    1984 "The First Responders"]
  :wod [
    1515 "Arakkoa Outcasts"
    1445 "Frostwolf Orcs"
    1708 "Laughing Skull Orcs"
    1849 "Order of the Awakened"
    1850 "The Saberstalkers"
    1848 "Vol'jin's Headhunters"
    1681 "Vol'jin's Spear"
    1711 "Steamwheedle Preservation Society"
    1520 "Shadowmoon Exiles"
    1735 "Barracks Bodyguards"
]})

(defn guild-link [realm guild character]
  (let [g (slugify-guild guild)
        r (string/lower-case realm)
        gurl (tools/create-url-guild config/current-region "wow" defs/bn-path-guild r g)]
    (str "<a href=\""  (string/replace gurl ".api.blizzard.com" ".battle.net") "/roster?character=" character "\">" guild "</a>")))

(defn professions-secondary2 [c k]
  (into (get defs/bn-professions-secondary k)
    (map (juxt (comp keyword slugify-class :name) identity) (get (:secondary (:professions c)) k))))

(defn professions-primary [c]
  (:primary (:professions c)))

(defn professions-secondary [c]
  (:secondary (:professions c)))

(defn append-to-key [k suffix]
  (let [n (if (nil? k) "" (name k))
        s (str n suffix)
        kn (keyword s)]
    kn))

(defn index-list [x]
  (into {} (for [v x]
    [(:id v) v])))

(defn sorted-prof [m k a]
  (into {}
    (for [kx a]
      (let [kn (append-to-key kx (name k))]
        [kn (get-in m [k kn]) ]))))

(defn format-single [[k m]]
  (str "<img src=\"/img/" (:icon m) ".png\" alt=\"" (:name m) "\" title=\"" (:name m) "\"width=\"16\" height=\"16\">" (:rank m) " " ))

(defn format-profs [base new-data]
  (into {} (for [[k v] base]
    [k (into {} (for [[k v] v]
      (let [id (:id v)
            datakey (get new-data id {})]
          [k (merge v datakey)])))])))

(defn show-profession [m k]
;  (.println *err* m)
;  (.println *err* k)
  (.println *err* (get-in m [k k :rank] 0))
  (get-in m [k k :rank] 0))

(defn show-profession2 [m k]
  (map show-profession m k))

(defn show-profession3 [m k]
  "?")

(defn show-faction [k]
  (let [html "<img src=\"/img/FACTION.png\" width=\"16\" height=\"16\" alt=\"FACTION\" />"]
    (string/replace html #"FACTION" (name k))))

(defn show-primary [m i]
  "?")
  (comment
  (.println *err* (first (get m 0)))
  (.println *err* (get m 0))
  (if-let [y (get m i)]
    (if-let [x (first y)]
      (str "<img src=\"/img/" (slugify-class (:name x)) ".png\" width=\"16\" height=\"16\" alt=\"" (:name x) "\"> " (:rank x)))))

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
        psf (format-profs defs/bn-professions-secondary (index-list ps))
        sorted-fi (sorted-prof psf :fishing defs/bn-professions-order)
        sorted-co (sorted-prof psf :cooking defs/bn-professions-order)
        sorted-ar (sorted-prof psf :archaeology defs/bn-professions-order)
        faction (faction (:faction c))]
        (.println *err* "---")
        (.println *err* pp)
        (.println *err* "---")
        (comment
        (.println *err* p-s-a)
        (.println *err* "---")
        (.println *err* c)
        (.println *err* "---")
        (.println *err* (slugify-class cls)))
  (str
   "<tr>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\"><a href=\"{{purl}}\">" (:name c) "</a></td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" (:level c) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" guild-x "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" ilvl "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" ilvl-eq "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" (clojure.string/join (map format-single sorted-ar)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" (clojure.string/join (map format-single sorted-co)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" (clojure.string/join (map format-single sorted-fi)) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " t-left\">" (show-primary pp 0) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " t-left\">" (show-primary pp 1) "</td>\n"
   "  <td class=\"cls-3d-" (name faction) " t-center\">" (show-faction faction) "</td>\n"
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
    (let [standing (get standings (:standing m))
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
        bfa-reps (keep-indexed #(if (even? %1) %2) (:bfa all-reps))
        legion-reps (keep-indexed #(if (even? %1) %2) (:legion all-reps))
        wod-reps (keep-indexed #(if (even? %1) %2) (:wod all-reps))
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
