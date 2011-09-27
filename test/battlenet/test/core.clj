(ns battlenet.test.core
  (:use [battlenet.core])
  (:use [battlenet.defs])
  (:use [battlenet.tools])
  (:use [clojure.test]))

(def mock-json "{\"realms\":[{
      \"type\":\"pvp\",
      \"queue\":false,
      \"status\":true,
      \"population\":\"high\",
      \"name\":\"Aegwynn\",
      \"slug\":\"aegwynn\"
    }]}")



(deftest test-realm-is-online
  (is
    (= true
      (realm-is-online "eu" "aegwynn"))))


