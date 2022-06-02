(ns battlenet.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [battlenet.config :as config]
            [battlenet.wow :as wow]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(defn allowed-name [s]
  (re-matches #"[a-z0-9-]+" s))

(defn handle-run [params fnx h f]
  (if-let [name (allowed-name (:name params))]
    (let [r (wow/full-wow name fnx h f)]
      (str "[" params "]" r))
    "invalid input"))

(defn handle-hook [id]
  (let [dir (str config/data-dir "/hooks")
        hook (str dir "/" id)]
    (if-let [exists (.exists (clojure.java.io/file hook))]
      (clojure.java.shell/sh hook)
      {:exit 1, :out "not found", :err ""})))

(defroutes app-routes
  (GET "/" request (str "Lok'tar Ogar!"))
  (POST "/chars" request (handle-run (:params request) :chars "head" "foot"))
  (POST "/reps"  request (handle-run (:params request) :reps "head-rep" "foot-rep"))
  (POST "/hooks/:id" [id] (let [rv (handle-hook id)] (str "{" id "}" (:exit rv) "-"(:out rv))))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes (assoc api-defaults :static {:resources "public"})))
