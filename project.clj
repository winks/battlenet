(defproject battlenet "0.5.1-alpha2"
  :description "Clojure library for Blizzard's Community Platform API"
  :url "https://github.com/winks/battlenet"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/data.json "0.2.0"]]
  :dev-dependencies [[radagast "1.1.1"]
                     [lein-clojars "0.9.1"]]
  :repositories {"clojure-releases" "http://build.clojure.org/releases"}
  :deploy-repositories [["clojars" {:sign-releases false}]]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main battlenet.core)
