(ns battlenet.wow.core
  (:use [battlenet.defs])
  (:require [battlenet.wow.tools :as wto])
  (:require [battlenet.wow.network :as wnet]))

(defn realm-is-online
  "Shows whether a realm is online."
  [region realm]
  (wnet/read-remote-field region realm :status))

(defn realm-is-offline
  "Shows whether a realm is offline."
  [region realm]
  (not (realm-is-online region realm)))

(defn realm-has-queue
  "Shows whether a realm has a queue."
  [region realm]
  (wnet/read-remote-field region realm :queue))

(defn realm-get-type
  "Shows the game rule type of a realm."
  [region realm]
  (wnet/read-remote-field region realm :type))

(defn realm-get-info
  "Returns info about a realm."
  [region realm]
  (get (wnet/read-remote-realms region realm) :realms))

(defn get-realm-names
  "Returns all realm names."
  [region]
  (map wto/get-name
       (get (wnet/read-remote-realms region "") :realms)))
