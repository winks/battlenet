(ns battlenet.utils
  (:require [clojure.string :as string])
  (:require [battlenet.auth :as auth])
  (:require [battlenet.config :as config])
  (:require [clj-http.client :as client])
  (:use [clojure.data.json :only (read-str write-str)])
  (:import (java.net URLEncoder UnknownHostException)
           (java.io FileNotFoundException IOException)))

(defn- ppe [s]
  (.println *err* s))

(def conn-mgr (clj-http.conn-mgr/make-reusable-conn-manager {:timeout 3 :threads 5}))

(defn url-enc [s]
  (URLEncoder/encode s))

(defn uc-first [s]
  (if (empty? s)
  ""
  (str (string/upper-case (subs s 0 1)) (string/lower-case (subs s 1)))))

(defn slugify-guild-char [s]
  (if (empty? s) ""
    (string/lower-case (string/replace (string/replace s "'" "") " " "-"))))

(defn slugify-realm [s]
  (if (empty? s) ""
   (string/lower-case (string/replace (string/replace s "'" "") " " "-"))))

(defn slugify-class [s]
  (if (empty? s) ""
    (string/lower-case (string/replace (string/replace s " " "") "'" ""))))

(defn slugify-locale [s]
  (if (empty? s) ""
    (string/lower-case (string/replace s "_" "-"))))

(defn kw-prof [s]
  (if (empty? s) nil)
    (let [r (string/lower-case (string/replace s " " ""))]
      (if (empty? r) nil (keyword r))))

(defn write-cache [realm char-name data]
  (let [dir (str "./cache/" config/current-region "/" (slugify-realm realm))
        file (str dir "/" (slugify-guild-char char-name))]
    (try
      (do
        (.mkdir (java.io.File. dir))
        (spit file (write-str data)))
      (catch Exception ex nil))))

(defn read-cache [realm char-name]
  (let [dir (str "./cache/" config/current-region "/" (slugify-realm realm))
        file (str dir "/" (slugify-guild-char char-name))]
    (try
      (read-str (slurp file) :key-fn keyword)
      (catch Exception ex {}))))

(defn html-get
  "slurp replacement"
  [url]
  (get (client/get url
                   {:connection-manager conn-mgr
                    :headers {"Authorization" (str "Bearer " (auth/get-token config/oauth-config))}}) :body))

(defn read-url
  "Reads from an URL."
  [url]
  (try
    (html-get url)
    (catch UnknownHostException ex
      (ppe ex)
      nil)
    (catch FileNotFoundException ex
      (ppe ex)
      nil)
    (catch IOException ex
      (ppe ex)
      (try
        (html-get url)
        (catch IOException ex
          (ppe ex)
          nil)))))

; @unused
(defn create-url-char-profile
  [region realm name params]
  (->
   "https://{region}.api.blizzard.com/profile/wow/character/{realm}/{name}?namespace=profile-eu&{params}"
   (string/replace "{region}" region)
   (string/replace "{realm}" realm)
   (string/replace "{name}" (url-enc name))
   (string/replace "{params}" params)))

(defn read-char-profile
  [region realm name params]
  (let [url (create-url-char-profile region realm name params)]
    (ppe url)
    (read-str (read-url url) :key-fn keyword)))

; @unused
(defn create-url-char-professions
  [region realm name params]
  (->
   "https://{region}.api.blizzard.com/profile/wow/character/{realm}/{name}/professions?namespace=profile-eu&{params}"
   (string/replace "{region}" region)
   (string/replace "{realm}" realm)
   (string/replace "{name}" (url-enc name))
   (string/replace "{params}" params)))

(defn read-char-professions
  [region realm name params]
  (let [url (create-url-char-professions region realm name params)]
    (ppe url)
    (read-str (read-url url) :key-fn keyword)))

; @unused
(defn create-url-char-reputations
  [region realm name params]
  (->
   "https://{region}.api.blizzard.com/profile/wow/character/{realm}/{name}/reputations?namespace=profile-eu&{params}"
   (string/replace "{region}" region)
   (string/replace "{realm}" realm)
   (string/replace "{name}" (url-enc name))
   (string/replace "{params}" params)))

(defn read-char-reputations
  [region realm name params]
  (let [url (create-url-char-reputations region realm name params)
        json (read-url url)]
    (do (ppe url)
        (ppe json)
        (read-str json :key-fn keyword))))

(defn create-url-char-achievements
  [region realm name params]
  (->
   "https://{region}.api.blizzard.com/profile/wow/character/{realm}/{name}/achievements?namespace=profile-eu&{params}"
   (string/replace "{region}" region)
   (string/replace "{realm}" realm)
   (string/replace "{name}" (url-enc name))
   (string/replace "{params}" params)))

(defn read-char-achievements
  [region realm name params]
  (let [url (create-url-char-achievements region realm name params)
        json (read-url url)]
    (do (ppe url)
        (ppe json)
        (read-str json :key-fn keyword))))

(defn slugify-d3-profile [s]
  (if (empty? s) ""
    (string/replace s "#" "-")))

; @unused?
(defn create-url-d3-profile
  [region account params]
  (->
   "https://{region}.api.blizzard.com/d3/profile/{account}?{params}"
   (string/replace "{region}" region)
   (string/replace "{account}" (slugify-d3-profile account))
   (string/replace "{params}" params)))

(defn read-d3-profile
  [region account params]
  (let [url (create-url-d3-profile region account params)]
    (ppe url)
    (read-str (read-url url) :key-fn keyword)))
