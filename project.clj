(defproject battlenet "1.0.0-SNAPSHOT"
  :description "A Battle.net API client"
  :url "https://github.com/winks/battlenet"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/data.json "2.4.0"]
                 [clj-http "3.12.3"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]]
  :plugins [[lein-ring "0.12.5"]]
  :repl-options {:init-ns battlenet.core}
  ;:main battlenet.core
  :aliases {"cli" ["run" "-m" "battlenet.core"]}
  :aot [battlenet.core]
  :ring {:handler battlenet.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
