(ns battlenet.test.integration
  (:use [battlenet.core])
  (:use [battlenet.defs])
  (:use [battlenet.tools])
  (:use [battlenet.network])
  (:use [battlenet.test.mock])
  (:use [clojure.test]))

(deftest test-check-url-int-success
  (is
    (= 1
       (check-url "https://eu.battle.net/api/wow/realm/status"))))

(deftest test-check-url-int-error
  (is
    (= 0
       (check-url "https://eu.battle.net/api/wow/realm/statuswhateverinvalidurl"))))

(deftest test-read-url-int-success
  (is
    (.startsWith
      (read-url "https://eu.battle.net/api/wow/realm/status?realms=aegwynn")
      "{")))

(deftest test-read-url-int-error
  (is
    (.equals
      (read-url "https://eu.battle.net/api/wow/realm/statuswhateverinvalidurl")
      "{}")))

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
                  (get (read-remote-realms "eu" "aegwynn,aerie-peak") :realms)))))

(deftest test-realm-get-info-int
  (is
    (.equals "pvp"
             (:type (first( realm-get-info "eu" "aegwynn"))))))

(deftest test-realm-has-queue-int
  (is
    (instance? Boolean
       (realm-has-queue "eu" "aegwynn"))))

(deftest test-get-realm-names-int
  (is
    (.equals ["Aggramar" "Agamaggan"]
             (let [g (get-realm-names "eu")] (conj (conj nil (nth g 2)) (nth g 3))))))

