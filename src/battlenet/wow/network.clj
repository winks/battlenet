(ns battlenet.wow.network
  (:use [clojure.data.json :only (read-str)])
  (:use [battlenet.defs])
  (:use [battlenet.wow.defs])
  (:require [battlenet.network :as net])
  (:require [battlenet.tools :as to])
  (:require [battlenet.wow.tools :as wto]))

(defn read-remote-realms
  "Returns realm info as a map."
  [region realm]
  (read-str
    (net/read-url
      (to/create-url region bn-game-wow bn-path-realm (wto/join-params-comma ["realms" realm])))
    :key-fn keyword))

(defn read-remote-field
  "Read a single field from remote."
  [region realm field]
  (let [data (read-remote-realms region realm)]
    (wto/access-rmap data field)))

(defn read-remote-races
  "Returns race info as a map."
  [region]
  (read-str
    (net/read-url
      (to/create-url region bn-game-wow bn-path-races ""))
    :key-fn keyword))

(defn read-remote-classes
  "Returns class info as a map."
  [region]
  (read-str
    (net/read-url
      (to/create-url region bn-game-wow bn-path-classes ""))
    :key-fn keyword))

(defn read-remote-perks
  "Returns guild perks info as a map."
  [region]
  (read-str
    (net/read-url
      (to/create-url region bn-game-wow bn-path-perks ""))
    :key-fn keyword))

(defn read-remote-rewards
  "Returns guild rewards info as a map."
  [region]
  (read-str
    (net/read-url
      (to/create-url region bn-game-wow bn-path-rewards ""))
    :key-fn keyword))

(defn read-remote-item
  "Returns item info as a map."
  [region itemid]
  (read-str
    (net/read-url
      (wto/create-url-item region bn-game-wow bn-path-item itemid))
    :key-fn keyword))

(defn read-remote-character
  "Returns character data as a map."
  ([region realm charname]
  (read-str
    (net/read-url
      (wto/create-url-character region bn-game-wow bn-path-character realm charname))
    :key-fn keyword))
  ([region realm charname params]
  (read-str
    (net/read-url
      (wto/create-url-character region bn-game-wow bn-path-character realm charname params))
    :key-fn keyword)))

(defn read-remote-guild
  "Returns guild data as a map."
  [region realm guildname]
  (read-str
    (net/read-url
      (wto/create-url-guild region bn-game-wow bn-path-guild realm guildname))
    :key-fn keyword))
