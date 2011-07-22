(ns battlenet.core
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:use [clojure.contrib.duck-streams :only (slurp*)])
  (:import (java.net URL URLConnection HttpURLConnection UnknownHostException)
           (java.io FileNotFoundException)))

(def bn-baseurl "https://{region}.battle.net/api/{game}{path}?{params}")
(def bn-path-realm-status "/realm/status")

(defn create-url
  "Builds a request URL."
  [region game path params]
    (.replace
      (.replace
        (.replace
          (.replace bn-baseurl "{region}" region)
          "{game}" game)
        "{path}" path)
    "{params}" params))

(defn join-params
  "Joins URL parameters."
  [params]
  (str (first params) "=" (apply str (interpose \, (nthnext params 1)))))

(defn check-url
  "Checks if an URL is readable."
  [url]
  (try
    (let [c (cast HttpURLConnection
                  (.openConnection (URL. url)))]
      (if (= 200 (.getResponseCode c))
        1
        0))
    (catch UnknownHostException _
      0)))

(defn read-url
  "Reads from an URL."
  [url]
  (try
    (slurp* url)
    (catch UnknownHostException _
      0)
    (catch FileNotFoundException _
      0)))

(defn get-realm-map
  "Returns realm info as a map."
  [region realm]
  (read-json
    (read-url
      (create-url region "wow" bn-path-realm-status (join-params ["realms" realm])))))

(defn realm-is-online
  "Shows whether a realm is online."
  [region realm]
  (let [data (get-realm-map region realm)]
    (get (first (get data :realms)) :status)))

(defn realm-is-offline
  "Shows whether a realm is offline."
  [region realm]
  (not (realm-is-online region realm)))

(defn realm-has-queue
  "Shows whether a realm has a queue."
  [region realm]
  (let [data (get-realm-map region realm)]
    (get (first (get data :realms)) :queue)))
;(println
;  (has-status
;    (read-json
;      (read-url "https://eu.battle.net/api/wow/realm/status?realms=aegwynn"))))
(println ( realm-is-online "eu" "aegwynn"))
(println ( realm-is-offline "eu" "aegwynn"))
