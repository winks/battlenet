(ns battlenet.handler
  (:require [clojure.data.json :as json]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [battlenet.config :as config]
            [battlenet.wow :as wow]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(defn allowed-name [s]
  (re-matches #"[a-z0-9-]+" s))

(defn handle-run [params fnx h f]
  (if-let [name (allowed-name (:name params))]
    (let [r (wow/full-wow name fnx h f)]
      (assoc params :msg r :code 0))
      {:msg "invalid input" :code 13}))

(defn handle-hook [id]
  (let [dir (str config/data-dir "/hooks")
        hook (str dir "/" id)]
    (if-let [exists (.exists (clojure.java.io/file hook))]
      (let [stdout (:out (clojure.java.shell/sh hook))]
		{:exit 0, :out (clojure.string/split stdout #"\n"), :err ""})
      {:exit 1, :out "Not Found", :err ""})))

(defroutes app-routes
  (GET "/" request (str "Lok'tar Ogar!"))
  (POST "/chars" request (json/write-str (handle-run (:params request) :chars "header" "footer")))
  (POST "/reps"  request (json/write-str (handle-run (:params request) :reps "header-rep" "footer-rep")))
  (POST "/hooks/:id" [id]
    (let [rv (handle-hook id)]
      (json/write-str {:id id, :code (:exit rv), :msg (:out rv)})))
  (route/not-found (json/write-str {:msg "Not Found"})))

(def app
  ;(wrap-defaults app-routes (assoc api-defaults :static {:resources "public"})))
  (wrap-defaults (wrap-file app-routes config/docroot-dir) api-defaults))
