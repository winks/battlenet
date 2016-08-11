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
  (let [[realm name] (string/split cname #";")
        params (str "fields=guild,items,professions&" config/current-params)
        purl (tools/create-url-character config/current-region "wow" defs/bn-path-character (slugify-realm realm) name)]
    (if-let [chara (network/read-remote-character config/current-region (slugify-realm realm) name params)]
      (->
        (fmt-char chara)
        (string/replace "{{purl}}" (str purl "/advanced"))
        (string/replace ".api.battle.net" ".battle.net")))))

(defn -main [& m]
 (if-let [xs (System/getenv "FILENAME")]
   (print (apply str (map show-char (get config/current-chars (keyword (string/replace xs ".html" ""))))))
   (print (apply str (map show-char (get config/current-chars :test))))))
