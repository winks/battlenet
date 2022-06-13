(ns battlenet.core
  (:gen-class)
  (:require [battlenet.config :as config])
  (:require [battlenet.wow :as wow]))

(defn -main [& m]
  (let [name (if-let [xs (second m)] (keyword xs) config/current-list)]
    (cond
      (.equals "full"      (first m)) (wow/full-wow name :chars "header" "footer")
      (.equals "full-reps" (first m)) (wow/full-wow name :reps "header-rep" "footer-rep")
      (.equals "reps"      (first m)) (print (wow/run-wow-reps name))
      (.equals "chars"     (first m)) (print (wow/run-wow-chars name))
      (.equals "d3"        (first m)) (print (wow/run-all-d3))
      :else                           (println "Usage: lein cli ...  chars | reps | full | full-reps | d3"))))
