(ns battlenet.test.integration
  (:use [battlenet.core])
  (:use [battlenet.defs])
  (:use [battlenet.model])
  (:use [battlenet.tools])
  (:use [battlenet.network])
  (:use [battlenet.test.mock])
  (:use [clojure.test]))

(deftest test-check-url-int
  (is
    (= 1
       (check-url "https://eu.battle.net/api/wow/realm/status"))))

(deftest test-read-url-int
  (is
    (.startsWith
      (read-url "https://eu.battle.net/api/wow/realm/status?realms=aegwynn") "{")))

(deftest test-realm-is-online-int
  (is
    (instance? Boolean
      (realm-is-online "eu" "aegwynn"))))

(deftest test-realm-is-offline-int
  (is
    (instance? Boolean
      (realm-is-offline "eu" "aegwynn"))))

(deftest test-get-type-int
  (is
    (= "pvp"
      (realm-get-type "eu" "aegwynn"))))

(deftest test-get-names-int
  (is
    (.equals ["Aerie Peak" "Aegwynn"]
             (map get-name 
                  (get (read-remote-rmap "eu" "aegwynn,aerie-peak") :realms)))))

(deftest test-realm-get-info
  (is
    (.equals "pvp"
             (:type (first( realm-get-info "eu" "aegwynn"))))))

(deftest test-realm-has-queue
  (is
    (instance? Boolean
       (realm-has-queue "eu" "aegwynn"))))

(deftest test-get-realm-names
  (is
    (.equals ["Aggramar" "Agamaggan"]
             (let [g (get-realm-names "eu")] (conj (conj nil (nth g 2)) (nth g 3))))))

