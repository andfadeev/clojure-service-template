(ns clojure-service-template.test-support
  (:require [clojure-service-template.core :as core]
            [clojure.test :refer :all]
            [com.stuartsierra.component :as component])
  (:import (org.testcontainers.containers PostgreSQLContainer)))

(def ^:dynamic *test-system* nil)

(defn database
  []
  {:database ((-> (into {} *test-system*) :database))})

(defonce postgresql-test-container
  (-> (PostgreSQLContainer. "postgres:16")
      (.withReuse true)))

(defn with-postgresql-test-container
  [f]
  (.start postgresql-test-container)
  (f))

(defn test-configuration
  []
  {:web-server {:host "0.0.0.0"
                :port 8080
                :cookie-store-key [124 39 -128 97 79 -21 34 16 -81 -24 -81 86 -110 62 23 118]}
   :database {:jdbcUrl (.getJdbcUrl postgresql-test-container)
              :username (.getUsername postgresql-test-container)
              :password (.getPassword postgresql-test-container)}
   :weather-api {:base-url "https://api.open-meteo.com"}
   :kinde-client {:domain "https://andreyfadeev-local.uk.kinde.com"
                  :client-id "***"
                  :client-secret "***"
                  :redirect-uri "http://localhost:8080/kinde/callback"
                  :logout-redirect-uri "http://localhost:8080/"}})

(defn with-system
  [configuration-fn]
  (fn
    [test-fn]
    (binding [*test-system* (-> (configuration-fn)
                                (core/build-system)
                                (component/start-system))]
      (try
        (test-fn)
        (finally
          (component/stop *test-system*))))))

