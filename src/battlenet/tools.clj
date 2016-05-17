(ns battlenet.tools
  (:require [clojure.string :as string])
  (:require [battlenet.defs :as defs]))

;;;;;;;;;;;;;
; url stuff
;;;;;;;;;;;;;

(defn create-url
  "Builds a request URL."
  [region game path params]
    (let [elocale (System/getenv "BATTLENET_LOCALE")
          eapikey (System/getenv "BATTLENET_APIKEY")
          xlocale (if (and (not (.contains params "locale=")) (not (empty? elocale))) (str "locale=" elocale) "")
          xapikey (if (and (not (.contains params "apikey=")) (not (empty? eapikey))) (str "apikey=" eapikey) "")
          params2 (apply str (interpose \& (filter not-empty [params xlocale xapikey])))]
    (->
      defs/bn-baseurl
      (string/replace "{region}" region)
      (string/replace "{game}" game)
      (string/replace "{path}" path)
      (string/replace "{qp}" (if (empty? params2) "" "?"))
      (string/replace "{params}" params2))))

(defn media-url-icon
  "Builds an icon URL."
  [region game size icon]
  (->
    defs/bn-media-icon
    (string/replace "{region}" region)
    (string/replace "{game}" game)
    (string/replace "{size}" (if (.equals "small" size) "18" "56"))
    (string/replace "{icon}" icon)))

(defn media-url-avatar
  "Builds an avatar URL."
  [region thumbnail]
  (->
    defs/bn-media-avatar
    (string/replace "{region}" region)
    (string/replace "{thumbnail}" thumbnail)))

(defn join-params
  "Joins a vector of vectors of k v pairs."
  [m]
  (apply str (interpose \& (map (fn [x] (apply str (interpose \= x)))m))))
