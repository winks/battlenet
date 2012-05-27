(ns battlenet.d3.network
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:use [battlenet.d3.defs])
  (:use [battlenet.d3.tools]))

(defn read-remote-account
  "Returns account data as a map."
  [region account number]
  (read-json
    (read-url
      (create-url-account region bn-game-d3 bn-path-account account number))))

(defn read-remote-hero
  "Returns hero data as a map."
  [region hero]
  (read-json
    (read-url
      (create-url-hero region bn-game-d3 bn-path-hero hero))))