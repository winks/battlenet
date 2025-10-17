(ns battlenet.core
  (:gen-class)
  (:require [battlenet.config :as config])
  (:require [battlenet.d3 :as d3])
  (:require [battlenet.wow :as wow]))

(defn -main [& m]
  (let [name (if-let [xs (second m)] (keyword xs) config/current-list)]
    (cond
      (.equals "chars"      (first m)) (wow/full-wow name :chars "wow/header-chars" "wow/footer-chars")
      (.equals "reps"       (first m)) (wow/full-wow name :reps "wow/header-rep" "wow/footer-rep")
      (.equals "achi"       (first m)) (wow/full-wow name :achi "wow/header-achi" "wow/footer-achi")
      (.equals "chars-solo" (first m)) (print (wow/run-wow-chars name))
      (.equals "reps-solo"  (first m)) (print (wow/run-wow-reps name))
      (.equals "achi-solo"  (first m)) (print (wow/run-wow-achi name))
      (.equals "d3"         (first m)) (print (d3/run-all-d3))
      :else                           (println "Usage: lein cli ... chars/reps/achi [FILENAME] | chars/reps/achi-solo [NAME] | d3"))))
