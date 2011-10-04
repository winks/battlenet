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

(defn media-url-avatar
  "Builds an avatar URL."
  [region thumbnail]
  (->
    bn-media-avatar
    (string/replace "{region}" region)
    (string/replace "{thumbnail}" thumbnail)))

(defn access-rmap
  "Access a member of a realmsmap"
  [rsmap crit]
  (get (first (get rsmap :realms)) crit))

(defn get-name
  "Get a name from a rmap."
  [rmap]
  (get rmap :name))

(defn get-title-x
  "Helper function for get-title"
  [map]
  (for [title (:titles map)
        :when (= true (:selected title))]
    (:name title)))

(defn get-title
  "Get a title from a map"
  [map]
  (let [new (get-title-x map)]
    (if (string/blank? (first new)) "%s" (first new))))

(defn get-primary-professions
  [map]
  (let [primary-1 (first (:primary map))
        primary-2 (nth (:primary map) 1)]
    [(if (string/blank? (:name primary-1)) nil [(:name primary-1) (:rank primary-1)])
     (if (string/blank? (:name primary-2)) nil [(:name primary-2) (:rank primary-2)])]))

(defn get-secondary-professions
  [map]
  (let [first-aid (for [prof (:secondary map)
                        :when (.equals "First Aid" (:name prof))]
                    (str (:name prof) " " (:rank prof)))
        arch      (for [prof (:secondary map)
                        :when (.equals "Archaeology" (:name prof))]
                    (str (:name prof) " " (:rank prof)))
        fishing   (for [prof (:secondary map)
                        :when (.equals "Fishing" (:name prof))]
                    (str (:name prof) " " (:rank prof)))
        cooking   (for [prof (:secondary map)
                        :when (.equals "Cooking" (:name prof))]
                    (str (:name prof) " " (:rank prof)))]
    {:firstaid (if (string/blank? (first first-aid)) nil (first first-aid)),
     :archaeology (if (string/blank? (first arch)) nil (first arch)),
     :fishing (if (string/blank? (first fishing)) nil (first fishing)),
     :cooking (if (string/blank? (first cooking)) nil (first cooking))}))

(defn get-secondary-profession
  [map profname]
  (let [value (for [prof (:secondary map)
                   :when (.equals profname (:name prof))]
               (str (:rank prof)))]
    (if (string/blank? (first value)) nil (first value))))

(defn copper-to-gold-plain
  "Helper function for copper-to-gold"
  [input]
  (let [gold (quot input 10000)
        silver (quot (- input (* 10000 gold)) 100)
        copper (mod input 100)]
    [(if (< 0 gold) gold 0)
     (if (< 0 silver) silver 0)
      copper]))

(defn copper-to-gold
  "Currency conversion."
  ([input]
  (let [currency (copper-to-gold-plain input)]
    (str (if (> (nth currency 0) 0) (str (nth currency 0) " G "))
         (if (> (nth currency 1) 0) (str (nth currency 1) " S "))
         (nth currency 2) " C")))
  ([input x]
    (let [currency (copper-to-gold-plain input)]
      (str (if (> (nth currency 0) 0) (str "<span class=\"icon-gold\">" (nth currency 0) "</span>"))
           (if (> (nth currency 1) 0) (str "<span class=\"icon-silver\">" (nth currency 1) "</span>"))
           "<span class=\"icon-copper\">" (nth currency 2) "</span>"))))