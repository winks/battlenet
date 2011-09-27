(ns battlenet.network
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:use [clojure.contrib.duck-streams :only (slurp*)])
  (:use [battlenet.defs])
  (:use [battlenet.tools])
  (:import [battlenet.model BRealm BCharacter])
  (:import (java.net URL URLConnection HttpURLConnection UnknownHostException)
           (java.io FileNotFoundException)))

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
      (create-url region "wow" bn-path-realm (join-params ["realms" realm])))))

(defn read-remote-field
  "read from remote"
  [region realm field]
  (let [data (get-realm-map region realm)]
    (access-realm-map data field)))