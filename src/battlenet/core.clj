(ns battlenet.core
  (:use [clojure.data.json :only (json-str write-json read-json)])
  (:require clojure.contrib.duck-streams))

(def baseurl "https://{region}.battle.net/api/{game}{path}?{params}")

(defn create-url
  "builds a request URL"
  [region game path params]
    (.replace
      (.replace
        (.replace
          (.replace baseurl "{region}" region)
          "{game}" game)
        "{path}" path)
    "{params}" params))

(defn join-params
  "joins url parameters"
  [params]
  (str (first params) "=" (apply str (interpose \, (nthnext params 1)))))

