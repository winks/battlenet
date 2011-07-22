(ns battlenet.core
  (:use [battlenet.defs])
  (:use [battlenet.tools]))

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
