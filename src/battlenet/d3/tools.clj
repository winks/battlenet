(ns battlenet.d3.tools
  (:use [battlenet.d3.defs])
  (:require [clojure.string :as string])
  (:require [battlenet.tools :as tools]))

(defn create-url-profile
  "Builds a request URL for profile requests."
  [region game path profile number params]
  (->
    (tools/create-url region game path params)
    (string/replace "{profile}" profile)
    (string/replace "{number}" number)))

(defn create-url-hero
  "Builds a request URL for hero requests."
  [region game path profile number hero params]
  (->
    (create-url-profile region game path profile number params)
    (string/replace "{hero}" hero)))
