(defproject lufibooks "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.9"]
                 [org.clojure/data.json "0.2.5"]
                 [hiccup "1.0.5"]
                 [com.cemerick/friend "0.2.1"]
                 [ring-server "0.3.1"]
                 [ring/ring-json "0.3.1"]
                 [hyperion/hyperion-api "3.7.1"]
                 [org.clojars.freeagent/clj-amazon "0.2.2-SNAPSHOT"]
                 ]
  :plugins [[lein-ring "0.8.12"]]
  :ring {:handler lufibooks.handler/app
         :init lufibooks.handler/init
         :destroy lufibooks.handler/destroy
         :open-browser? false
         :stacktraces? true
         :auto-reload? true}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
