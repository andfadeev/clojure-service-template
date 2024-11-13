(ns clojure-service-template.components.database
  (:require
   [clojure.tools.logging :as log]
   [clojure-service-template.types :as t]
   [next.jdbc.connection :as connection])
  (:import
   (com.zaxxer.hikari HikariDataSource)
   (org.flywaydb.core Flyway)))

(defn init-fn
  [datasource]
  (log/info "Running database migrations...")
  (.migrate
   (.. (Flyway/configure)
       (dataSource datasource)
       (locations (into-array String ["classpath:database/migrations"]))
       (table "schema_version")
       (load))))

(defn new-database
  {:malli/schema [:=> [:cat t/DatabaseComponentConfig] :any]}
  [component-config]
  (connection/component
   HikariDataSource
   (assoc component-config :init-fn init-fn)))
