(defproject clojure-service-template "0.1.0-SNAPSHOT"
  :description "Opinionated Clojure Service Template"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.5"]
                 [metosin/reitit "0.10.1"]
                 [ring/ring-core "1.15.4"]
                 [ring/ring-jetty-adapter "1.15.4"]
                 [com.stuartsierra/component "1.2.0"]
                 [aero "1.1.6"]
                 [metosin/malli "0.20.1"]
                 [hiccup/hiccup "2.0.0"]
                 [com.kinde/kinde-core "2.3.0"]
                 [com.fasterxml.jackson.core/jackson-annotations "2.21"]

                 [camel-snake-kebab "0.4.3"]
                 [org.babashka/http-client "0.4.23"]
                 [com.cnuernber/charred "1.038"]

                 [com.github.seancorfield/next.jdbc "1.3.1108"]
                 [com.layerware/hugsql "0.5.3"]
                 [com.github.seancorfield/honeysql "2.7.1368"]
                 [org.postgresql/postgresql "42.7.11"]
                 [org.flywaydb/flyway-core "12.7.0"]
                 [org.flywaydb/flyway-database-postgresql "12.7.0"]

                 [com.zaxxer/HikariCP "7.0.2"]

                 [org.clojure/tools.logging "1.3.1"]
                 [org.slf4j/slf4j-api "2.1.0-alpha1"]
                 [org.slf4j/slf4j-simple "2.1.0-alpha1"]]
  :main ^:skip-aot clojure-service-template.core
  :target-path "target/%s"
  :plugins [[com.github.liquidz/antq "2.11.1276"]]
  :antq {:exclude ["com.fasterxml.jackson.core/jackson-annotations"]}
  :repl-options {:init-ns dev}
  :profiles {:dev {:dependencies [[com.stuartsierra/component.repl "1.0.0"]
                                  [nrepl/nrepl "1.7.0"]
                                  [org.testcontainers/testcontainers "2.0.5"]
                                  [org.testcontainers/postgresql "1.21.4"]]
                   :source-paths ["dev"]}
             :kaocha {:dependencies [[lambdaisland/kaocha "1.91.1392"]]}
             :uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
