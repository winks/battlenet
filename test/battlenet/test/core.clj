(ns battlenet.test.core
  (:use [battlenet.core])
  (:use [clojure.test]))

(deftest test-join-params
  (is
    (.equals "realms=aegwynn,aerie-peak"
      (join-params ["realms" "aegwynn" "aerie-peak"]))))

(deftest test-create-url
  (is
    (.equals "https://eu.battle.net/api/wow/realm/status?realms=aegwynn"
      (create-url "eu" "wow" "/realm/status" "realms=aegwynn"))))
