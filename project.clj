(defproject clojure-service-template "0.1.0-SNAPSHOT"
  :description "Opinionated Clojure Service Template"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [metosin/reitit "0.7.2"]
                 [ring/ring-core "1.13.0"]
                 [ring/ring-jetty-adapter "1.13.0"]
                 [com.stuartsierra/component "1.1.0"]
                 [aero "1.1.6"]
                 [metosin/malli "0.16.4"]
                 [hiccup/hiccup "2.0.0-RC3"]
                 [com.kinde/kinde-core "2.0.0"]

                 [camel-snake-kebab "0.4.3"]
                 [org.babashka/http-client "0.3.11"]
                 [com.cnuernber/charred "1.034"]

                 [com.github.seancorfield/next.jdbc "1.3.955"]
                 [com.layerware/hugsql "0.5.3"]
                 [com.github.seancorfield/honeysql "2.6.1203"]
                 [org.postgresql/postgresql "42.7.4"]
                 [org.flywaydb/flyway-core "10.21.0"]
                 [org.flywaydb/flyway-database-postgresql "10.21.0"]

                 [com.zaxxer/HikariCP "6.0.0"]

                 [org.clojure/tools.logging "1.3.0"]
                 [org.slf4j/slf4j-api "2.1.0-alpha1"]
                 [org.slf4j/slf4j-simple "2.1.0-alpha1"]]
  :main ^:skip-aot clojure-service-template.core
  :target-path "target/%s"
  :plugins [[com.github.liquidz/antq "2.10.1241"]]
  :antq {}
  :repl-options {:init-ns dev}
  :profiles {:dev {:dependencies [[com.stuartsierra/component.repl "1.0.0"]
                                  [nrepl/nrepl "1.3.0"]
                                  [org.testcontainers/testcontainers "1.18.0"]
                                  [org.testcontainers/postgresql "1.18.0"]]
                   :source-paths ["dev"]}
             :kaocha {:dependencies [[lambdaisland/kaocha "1.91.1392"]]}
             :uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
