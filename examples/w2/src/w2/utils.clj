(ns w2.utils
  (:require [clojure.string :as string])
  (:require [w2.config :as config])
  (:use [clojure.data.json :only (read-str write-str)])
  (:import (java.net URL URLConnection HttpURLConnection UnknownHostException)
           (java.io FileNotFoundException IOException)))

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

(defn read-url
  "Reads from an URL."
  [url]
  (try
    (slurp url)
    (catch UnknownHostException ex
      (.println *err* ex)
      nil)
    (catch FileNotFoundException ex
      (.println *err* ex)
      nil)
    (catch IOException ex
      (.println *err* ex)
      (try
        (slurp url)
        (catch IOException ex
          (.println *err* ex)
          nil)))))

(defn create-url-char-profile
  [region realm name params]
  (->
   "https://{region}.api.blizzard.com/profile/wow/character/{realm}/{name}?namespace=profile-eu&{params}"
   (string/replace "{region}" region)
   (string/replace "{realm}" realm)
   (string/replace "{name}" name)
   (string/replace "{params}" params)))

(defn read-char-profile
  [region realm name params]
  (let [url (create-url-char-profile region realm name params)]
    (.println *err* url)
    (read-str (read-url url) :key-fn keyword)))

(defn create-url-char-reputations
  [region realm name params]
  (->
   "https://{region}.api.blizzard.com/profile/wow/character/{realm}/{name}/reputations?namespace=profile-eu&{params}"
   (string/replace "{region}" region)
   (string/replace "{realm}" realm)
   (string/replace "{name}" name)
   (string/replace "{params}" params)))

(defn read-char-reputations
  [region realm name params]
  (let [url (create-url-char-reputations region realm name params)]
    (.println *err* url)
    (read-str (read-url url) :key-fn keyword)))

(defn slugify-d3-profile [s]
  (if (empty? s) ""
    (string/replace s "#" "-")))

(defn create-url-d3-profile
  [region account params]
  (->
   "https://{region}.api.blizzard.com/d3/profile/{account}/?{params}"
   (string/replace "{region}" region)
   (string/replace "{account}" account)
   (string/replace "{params}" params)))

(defn read-d3-profile
  [region account params]
  (let [url (create-url-d3-profile region account params)]
    (.println *err* url)
    (read-str (read-url url) :key-fn keyword)))