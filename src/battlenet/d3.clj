(ns battlenet.d3
  (:gen-class)
  (:require [clojure.string :as string])
  (:require [clojure.stacktrace :as st])
  (:require [battlenet.config :as config])
  (:require [battlenet.defs :as defs])
  (:require [battlenet.utils :as utils]))


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
  (if-let [json (utils/read-d3-profile config/current-region (string/replace tag "#" "%23") (config/current-params))]
    (format-d3-profile json)))

(defn run-all-d3 []
  (apply str (map run-d3 config/current-tags)))
