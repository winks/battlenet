(ns battlenet.tools
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:use [clojure.contrib.duck-streams :only (slurp*)])
  (:use [battlenet.defs])
  (:import [battlenet.model BRealm BCharacter])
  (:import (java.net URL URLConnection HttpURLConnection UnknownHostException)
           (java.io FileNotFoundException)))

(defn create-url
  "Builds a request URL."
  [region game path params]
    (.replace
      (.replace
        (.replace
          (.replace bn-baseurl "{region}" region)
          "{game}" game)
        "{path}" path)
    "{params}" params))

(defn join-params
  "Joins URL parameters."
  [params]
  (str (first params) "=" (apply str (interpose \, (nthnext params 1)))))


(defn access-realm-map
  "Access a member of a realm map"
  [map crit]
  (get (first (get map :realms)) crit))

(defn realm-map-to-model
  "convert"
  [map]
  (BRealm.
    (access-realm-map map :type)
    (access-realm-map map :queue)
    (access-realm-map map :status)
    (access-realm-map map :population)
    (access-realm-map map :name)
    (access-realm-map map :slug)
    ""))
