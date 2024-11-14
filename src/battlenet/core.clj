(ns battlenet.core
  (:gen-class)
  (:require [battlenet.config :as config])
  (:require [battlenet.wow :as wow]))

(defn -main [& m]
  (let [name (if-let [xs (second m)] (keyword xs) config/current-list)]
    (cond
      (.equals "chars"      (first m)) (wow/full-wow name :chars "wow/header-chars" "wow/footer-chars")
      (.equals "reps"       (first m)) (wow/full-wow name :reps "wow/header-rep" "wow/footer-rep")
      (.equals "chars-solo" (first m)) (print (wow/run-wow-chars name))
      (.equals "reps-solo"  (first m)) (print (wow/run-wow-reps name))
      (.equals "d3"         (first m)) (print (wow/run-all-d3))
      :else                           (println "Usage: lein cli ...  chars [FILENAME] | reps | full | full-reps | d3"))))
