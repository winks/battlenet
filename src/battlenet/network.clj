(ns battlenet.network
  (:use [clojure.data.json :only (json-str write-json read-json)])
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
      0)
    (catch FileNotFoundException _
      0)))

(defn read-remote-rmap
  "Returns realm info as a map."
  [region realm]
  (read-json
    (read-url
      (create-url region bn-game-wow bn-path-realm (join-params ["realms" realm])))))

(defn read-remote-field
  "Read a single field from remote."
  [region realm field]
  (let [data (read-remote-rmap region realm)]
    (access-rmap data field)))

(defn read-remote-races
  "Returns race info as a map."
  [region]
  (read-json
    (read-url
      (create-url region bn-game-wow bn-path-races ""))))

(defn read-remote-classes
  "Returns class info as a map."
  [region]
  (read-json
    (read-url
      (create-url region bn-game-wow bn-path-classes ""))))

(defn read-remote-perks
  "Returns guild perks info as a map."
  [region]
  (read-json
    (read-url
      (create-url region bn-game-wow bn-path-perks ""))))

(defn read-remote-rewards
  "Returns guild rewards info as a map."
  [region]
  (read-json
    (read-url
      (create-url region bn-game-wow bn-path-rewards ""))))

(defn read-remote-perks
  "Returns guild perks info as a map."
  [region]
  (read-json
    (read-url
      (create-url region bn-game-wow bn-path-perks ""))))

(defn read-remote-item
  "Returns item info as a map."
  [region itemid]
  (read-json
    (read-url
      (create-url-item region bn-game-wow bn-path-item itemid))))

(defn read-remote-character
  "Returns character data as a map."
  ([region realm charname]
  (read-json
    (read-url
      (create-url-character region bn-game-wow bn-path-character realm charname))))
  ([region realm charname params]
  (read-json
    (read-url
      (create-url-character region bn-game-wow bn-path-character realm charname params)))))

(defn read-remote-guild
  "Returns guild data as a map."
  [region realm guildname]
  (read-json
    (read-url
      (create-url-guild region bn-game-wow bn-path-guild realm guildname))))