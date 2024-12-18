(ns clojure-service-template.queries
  (:require [honey.sql :as honey]
            [next.jdbc :as jdbc]))

(defn execute!
  [{:keys [database]} query]
  (jdbc/execute!
   database
   (honey/format query)
   jdbc/unqualified-snake-kebab-opts))

(defn execute-one!
  [{:keys [database]} query]
  (jdbc/execute-one!
   database
   (honey/format query)
   jdbc/unqualified-snake-kebab-opts))

(defn get-weather-forecast-entries
  [dependencies]
  (execute!
   dependencies
   {:select [:*]
    :from :weather-forecast}))

(defn insert-weather-forecast-entry!
  {:malli/schema [:=> [:cat :any :any] :any]}
  [dependencies weather-forecast-entry]
  (execute-one!
   dependencies
   {:insert-into :weather-forecast
    :values [weather-forecast-entry]
    :returning [:*]}))
