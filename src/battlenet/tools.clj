(ns battlenet.tools
  (:require [clojure.string :as string])
  (:require [battlenet.defs :as defs]))

;;;;;;;;;;;;;
; url stuff
;;;;;;;;;;;;;

(defn create-url
  "Builds a request URL."
  [region game path params]
    (->
      defs/bn-baseurl
      (string/replace "{region}" region)
      (string/replace "{game}" game)
      (string/replace "{path}" path)
      (string/replace "{qp}" (if (empty? params) "" "?"))
      (string/replace "{params}" params)))

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
