(ns battlenet.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [battlenet.wow :as wow]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn allowed-name [s]
  (re-matches #"[a-z0-9-]+" s))

(defn handle-run [params fnx]
  (if-let [name (allowed-name (:name params))]
    (let [r (wow/full-wow name fnx "head" "foot")]
      (str "[" params "]" r))
    "invalid input"))

(defroutes app-routes
  (GET "/" request (str "<br>\n[" (:params request) "]"))
  (GET "/chars" request (handle-run (:params request) :chars))
  (GET "/reps"  request (handle-run (:params request) :reps))
  ;(GET "/" [req] (str "Hello World, " req))
  ;(GET "/" {{:keys [query-params headers]} :query-params} (str "Hello World, " name))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
