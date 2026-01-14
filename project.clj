(defproject battlenet "1.0.11.0"
  :description "A Battle.net API client"
  :url "https://codeberg.org/wink/battlenet"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.4"]
                 [org.clojure/data.json "2.5.2"]
                 [clj-http "3.13.1"]
                 [compojure "1.7.2"]
                 [ring/ring-defaults "0.7.0"]]
  :plugins [[lein-cloverage "1.2.4"]
            [lein-ancient "1.0.0-RC3"]
            [lein-ring "0.12.6"]]
  :repl-options {:init-ns battlenet.core}
  :main battlenet.core
  :aliases {"cli" ["run" "-m" "battlenet.core"]}
  :aot [battlenet.core]
  :ring {:handler battlenet.handler/app}
  :test-paths ["test/"]
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                                  [ring/ring-mock "0.6.2"]]
                   :test-paths ["test/"]}})
