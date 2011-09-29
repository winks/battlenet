(ns battlenet.tools
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:use [clojure.contrib.duck-streams :only (slurp*)])
  (:use [battlenet.defs])
  (:import [battlenet.model BRealm BCharacter]))

(defn join-params
  "Joins URL parameters."
  [params]
  (str (first params) "=" (apply str (interpose \, (nthnext params 1)))))

(defn create-url
  "Builds a request URL."
  [region game path params]
    (.replace
      (.replace
        (.replace
          (.replace bn-baseurl "{region}" region)
          "{game}" game)
        "{path}" path)
    "{params}"  params))

(defn create-url-item
  "Builds a request URL for item requests."
  [region game path itemid]
   (.replace
     (create-url region game path "") "{id}" 
     (if (integer? itemid) (Integer/toString itemid) itemid)))

(defn create-url-character
  "Builds a request URL for character."
  [region game path realm charname]
  (.replace
    (.replace
      (create-url region game path "") "{realm}" realm)
    "{name}" charname))

(defn access-rmap
  "Access a member of a realmsmap"
  [rsmap crit]
  (get (first (get rsmap :realms)) crit))

(defn get-name
  "Get a name from a rmap."
  [rmap]
  (get rmap :name))

(defn rmap-to-model
  "convert"
  [rmap]
  (BRealm.
    (access-rmap rmap :type)
    (access-rmap rmap :queue)
    (access-rmap rmap :status)
    (access-rmap rmap :population)
    (access-rmap rmap :name)
    (access-rmap rmap :battlegroup)
    (access-rmap rmap :slug)
    ""))
