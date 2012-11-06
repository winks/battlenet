(ns battlenet.network
  (:use [clojure.data.json :only (read-str)])
  (:use [battlenet.defs])
  (:use [battlenet.tools])
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
    (slurp url)
    (catch UnknownHostException _
      "{}")
    (catch FileNotFoundException _
      "{}")))

(defn read-remote-realms
  "Returns realm info as a map."
  [region realm]
  (read-str
    (read-url
      (create-url region bn-game-wow bn-path-realm (join-params ["realms" realm])))
    :key-fn keyword))

(defn read-remote-field
  "Read a single field from remote."
  [region realm field]
  (let [data (read-remote-realms region realm)]
    (access-rmap data field)))

(defn read-remote-races
  "Returns race info as a map."
  [region]
  (read-str
    (read-url
      (create-url region bn-game-wow bn-path-races ""))
    :key-fn keyword))

(defn read-remote-classes
  "Returns class info as a map."
  [region]
  (read-str
    (read-url
      (create-url region bn-game-wow bn-path-classes ""))
    :key-fn keyword))

(defn read-remote-perks
  "Returns guild perks info as a map."
  [region]
  (read-str
    (read-url
      (create-url region bn-game-wow bn-path-perks ""))
    :key-fn keyword))

(defn read-remote-rewards
  "Returns guild rewards info as a map."
  [region]
  (read-str
    (read-url
      (create-url region bn-game-wow bn-path-rewards ""))
    :key-fn keyword))

(defn read-remote-perks
  "Returns guild perks info as a map."
  [region]
  (read-str
    (read-url
      (create-url region bn-game-wow bn-path-perks ""))
    :key-fn keyword))

(defn read-remote-item
  "Returns item info as a map."
  [region itemid]
  (read-str
    (read-url
      (create-url-item region bn-game-wow bn-path-item itemid))
    :key-fn keyword))

(defn read-remote-character
  "Returns character data as a map."
  ([region realm charname]
  (read-str
    (read-url
      (create-url-character region bn-game-wow bn-path-character realm charname))
    :key-fn keyword))
  ([region realm charname params]
  (read-str
    (read-url
      (create-url-character region bn-game-wow bn-path-character realm charname params))
    :key-fn keyword)))

(defn read-remote-guild
  "Returns guild data as a map."
  [region realm guildname]
  (read-str
    (read-url
      (create-url-guild region bn-game-wow bn-path-guild realm guildname))
    :key-fn keyword))
