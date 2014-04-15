(ns d3example.core
  (:require [clojure.string :as string])
  (:use [battlenet.d3.tools])
  (:use [battlenet.d3.network]))

(def current-region "eu")

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
  (let [bt (:battleTag profile)
        parts (pts bt)
        purl (create-url-profile current-region "d3" "/en/profile/{profile}-{number}/" (first parts) (second parts))]
  (->
    (apply str (map fmt-char (:heroes profile)))
    (string/replace "{{purl}}" purl)
    (string/replace "/api/" "/")
    (string/replace "{{battleTag}}" bt))))

(defn show-chars [bt]
  (let [parts (pts bt)
        profile (read-remote-profile current-region (first parts) (second parts))]
    (fmt-profile profile)))

(defn -main [& m]
  (let [profiles ["Straton#1"]]
    (print (apply str (map show-chars profiles)))))
