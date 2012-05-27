(ns battlenet.d3.tools
  (:use [battlenet.d3.defs])
  (:require [clojure.string :as string])
  (:require [battlenet.tools :as tools]))

(defn create-url-account
  "Builds a request URL for account requests."
  [region game path account number]
  (->
    (tools/create-url region game path "")
    (string/replace "{account}" account)
    (string/replace "{number}" number)))

(defn create-url-hero
  "Builds a request URL for hero requests."
  [region hame path hero]
  (->
    (tools/create-url region game path "")
    (string/replace "{hero}" hero)))