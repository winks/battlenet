(ns d3example.core
  (:require [clojure.string :as string])
  (:require [battlenet.d3.defs :as defs])
  (:require [battlenet.d3.network :as network])
  (:require [battlenet.d3.tools :as tools]))

(def current-region "eu")
(def current-params "locale=en_GB&apikey=XXX")
(def current-tags ["Tyler#2306"])

(defn pts [bt]
  (let [parts (string/split bt #"#")
        pname (string/lower-case (first parts))
        pnum  (second parts)]
    [pname pnum]))

(defn fmt-char [c]
  (str
   "<tr class=\"" (:class c) "\">\n  <td>"
   "<a href=\"{{purl}}hero/" (:id c) "\">"
   (:name c)
   "</a>"
   "</td>\n  <td>"
   (:level c) " (" (:paragonLevel c)  ")"
   "</td>\n  <td>"
   (let [p (string/split (:class c) #"-")] (string/join " " (map (fn [x] (str (string/upper-case (first x)) (apply str (rest x)) )) p)))
   "</td>\n  <td>"
   (if (= 0 (:gender c)) "male" "female")
   "</td>\n  <td>"
   "<a href=\"{{purl}}\">"
   "{{battleTag}}"
   "</a>"
   "</td>\n  <td>"
   (if (true? (:hardcore c)) "HC" "")
   (if (true? (:dead c)) " †" "")
   "</td>\n</tr>\n"))

(defn fmt-profile [profile]
  (if-let [bt (:battleTag profile)]
    (let [parts (pts bt)
          purl (tools/create-url-profile current-region "d3" defs/bn-path-profile  (first parts) (second parts) "")]
  (->
    (apply str (map fmt-char (:heroes profile)))
    (string/replace "{{purl}}" purl)
    (string/replace ".api.battle.net" ".battle.net")
    (string/replace "{{battleTag}}" bt)))))

(defn show-chars [bt]
  (let [parts (pts bt)
        profile (network/read-remote-profile current-region (first parts) (second parts) current-params)]
    (fmt-profile profile)))

(defn -main [& m]
  (print (apply str (map show-chars current-tags))))
