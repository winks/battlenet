(ns battlenet.tools
  (:require [clojure.string :as string])
  (:use [battlenet.defs]))

(defn join-params
  "Joins URL parameters."
  [params]
  (str (first params) "=" (apply str (interpose \, (nthnext params 1)))))

(defn create-url
  "Builds a request URL."
  [region game path params]
    (->
      bn-baseurl
      (string/replace "{region}" region)
      (string/replace "{game}" game)
      (string/replace "{path}" path)
      (string/replace "{params}" params)))

(defn create-url-item
  "Builds a request URL for item requests."
  [region game path itemid]
   (->
     (create-url region game path "")
     (string/replace
       "{id}"
       (if (integer? itemid) (Integer/toString itemid) itemid))))

(defn create-url-character
  "Builds a request URL for character requests."
  ([region game path realm charname]
    (->
      (create-url region game path "")
      (string/replace "{realm}" realm)
      (string/replace "{name}" charname)))
  ([region game path realm charname params]
    (->
      (create-url region game path params)
      (string/replace "{realm}" realm)
      (string/replace "{name}" charname))))

(defn create-url-guild
  "Builds a request URL for guild requests."
  ([region game path realm guildname]
    (create-url-character region game path realm guildname))
  ([region game path realm guildname params]
    (create-url-character region game path realm guildname params)))

(defn media-url-icon
  "Builds an icon URL."
  [region game size icon]
  (->
    bn-media-icon
    (string/replace "{region}" region)
    (string/replace "{game}" game)
    (string/replace "{size}" (if (.equals "small" size) "18" "56"))
    (string/replace "{icon}" icon)))

(defn access-rmap
  "Access a member of a realmsmap"
  [rsmap crit]
  (get (first (get rsmap :realms)) crit))

(defn get-name
  "Get a name from a rmap."
  [rmap]
  (get rmap :name))

(defn copper-to-gold
  "Currency conversion."
  [input]
  (let [gold (quot input 10000)
        silver (quot (- input (* 10000 gold)) 100)
        copper (mod input 100)]
    (str (if (< 0 gold) (str gold " G "))
         (if (< 0 silver) (str silver " S "))
         copper " C")))