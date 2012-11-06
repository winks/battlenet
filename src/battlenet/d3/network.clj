(ns battlenet.d3.network
  (:use [clojure.data.json :only (read-str)])
  (:use [battlenet.d3.defs])
  (:use [battlenet.d3.tools]))

(defn read-remote-account
  "Returns account data as a map."
  [region account number]
  (read-str
    (read-url
      (create-url-account region bn-game-d3 bn-path-account account number))
    :key-fn keyword))

(defn read-remote-hero
  "Returns hero data as a map."
  [region hero]
  (read-str
    (read-url
      (create-url-hero region bn-game-d3 bn-path-hero hero))
    :key-fn keyword))
