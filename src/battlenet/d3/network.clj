(ns battlenet.d3.network
  (:use [clojure.data.json :only (read-str)])
  (:use [battlenet.defs])
  (:use [battlenet.network])
  (:use [battlenet.d3.defs])
  (:use [battlenet.d3.tools]))

(defn read-remote-profile
  "Returns profile data as a map."
  [region profile number]
  (let [url (create-url-profile region bn-game-d3 bn-path-profile profile number)]
    (read-str (read-url url) :key-fn keyword)))

(defn read-remote-hero
  "Returns hero data as a map."
  [region hero]
  (let [url (create-url-hero region bn-game-d3 bn-path-hero hero)]
    (read-str (read-url url) :key-fn keyword)))
