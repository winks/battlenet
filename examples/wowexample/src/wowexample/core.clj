(ns wowexample.core
  (:require [clojure.string :as string])
  (:require [battlenet.wow.defs :as defs])
  (:require [battlenet.wow.network :as network])
  (:require [battlenet.wow.tools :as tools])
  (:require [battlenet.tools :as wto])
  (:require [wowexample.config :as config]))

(defn slugify-guild [s]
  (string/lower-case (string/replace (string/replace s "'" "") " " "_")))

(defn slugify-realm [s]
  (string/lower-case (string/replace s " " "-")))

(defn slugify-class [s]
  (string/lower-case (string/replace s " " "")))

(def standings
  ["hated" "hostile" "unfriendly" "neutral" "friendly" "honored" "revered" "exalted"])

(def all-reps {
  :legion [
    1900 "Court of Farondis"
    1883 "Dreamweavers"
    1828 "Highmountain Tribe"
    1894 "The Wardens"]
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
    (str "<a href=\"" gurl "/roster?character=" character "\">" guild "</a>")))

(defn professions-secondary [c]
  (into defs/bn-professions-secondary
    (map (juxt (comp keyword slugify-class :name) identity) (:secondary (:professions c)))))

(defn professions-primary [c]
  (:primary (:professions c)))

(defn show-profession [m k]
  (get-in m [k :rank] 0))

(defn show-faction [k]
  (let [html "<img src=\"/img/FACTION.png\" width=\"16\" height=\"16\" alt=\"FACTION\" />"]
    (string/replace html #"FACTION" (name k))))

(defn show-primary [m i]
  (if-let [x (get m i)]
   (str "<img src=\"/img/" (slugify-class (:name x)) ".png\" width=\"16\" height=\"16\" alt=\"" (:name x) "\"> " (:rank x))))

(defn faction [s]
  (let [x (str s)]
    (if (= "0" x) :alliance (if (= "1" x) :horde :unknown))))

(defn fmt-char [c]
  (let [cls-id (:class c)
        cls (get defs/bn-classes cls-id)
        guild (:name (:guild c))
        ilvl (:averageItemLevel (:items c))
        ilvl-eq (:averageItemLevelEquipped (:items c))
        guild-x (if (seq guild) (guild-link (:realm c) guild (:name c)) guild)
        p-p (professions-primary c)
        p-s (professions-secondary c)
        faction (faction (:faction c))]
  (str
   "<tr>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\"><a href=\"{{purl}}\">" (:name c) "</a></td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" (:level c) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" guild-x "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" ilvl "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" ilvl-eq "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" (show-profession p-s :firstaid) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" (show-profession p-s :cooking) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" (show-profession p-s :fishing) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) "\">" (show-profession p-s :archaeology) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " t-left\">" (show-primary p-p 0) "</td>\n"
   "  <td class=\"cls-3d-" (slugify-class cls) " t-left\">" (show-primary p-p 1) "</td>\n"
   "  <td class=\"cls-3d-" (name faction) " t-center\">" (show-faction faction) "</td>\n"
   "</tr>\n")))

(defn show-char [cname]
  (.println *err* (str "Next: " cname))
  (if (= \# (first cname))
      "<tr><td></td></tr>"
      (let [[realm name] (string/split cname #";")
             params (str "fields=guild,items,professions&" config/current-params)
             purl (tools/create-url-character config/current-region "wow" defs/bn-path-character (slugify-realm realm) name)]
        (if-let [chara (network/read-remote-character config/current-region (slugify-realm realm) name params)]
          (->
            (fmt-char chara)
            (string/replace "{{purl}}" (str purl "/advanced"))
            (string/replace ".api.battle.net" ".battle.net"))))))

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
        legion-reps (keep-indexed #(if (even? %1) %2) (:legion all-reps))
        wod-reps (keep-indexed #(if (even? %1) %2) (:wod all-reps))
        div "<tr><td colspan=4></td></tr>\n"
        r (take 13 reps)]
    (apply str
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
             purl (tools/create-url-character config/current-region "wow" defs/bn-path-character (slugify-realm realm) name)]
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
