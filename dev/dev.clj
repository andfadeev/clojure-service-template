(ns dev
  #_{:clj-kondo/ignore [:unused-referred-var]}
  (:require [com.stuartsierra.component.repl :as component-repl :refer [reset stop]]
            [clojure-service-template.core :as core]
            [malli.dev :as md])
  (:import (org.testcontainers.containers PostgreSQLContainer)))

(defonce postgresql-test-container
         (-> (PostgreSQLContainer. "postgres:16-alpine")
             (.withReuse true)))

(md/start!)

(component-repl/set-init
  (fn [_]
    (.start postgresql-test-container)

    (core/build-system
      {:web-server {:host "0.0.0.0"
                    :port 8080
                    :cookie-store-key [124 39 -128 97 79 -21 34 16 -81 -24 -81 86 -110 62 23 118]}
       :database {:jdbcUrl (.getJdbcUrl postgresql-test-container)
                  :username (.getUsername postgresql-test-container)
                  :password (.getPassword postgresql-test-container)}
       :weather-api {:base-url "https://api.open-meteo.com"}
       :kinde-client {:domain "https://andreyfadeev-local.uk.kinde.com"
                      :client-id "4d1c2c7eb9954ddf94c63191cb7ffe5e"
                      :client-secret (or (System/getenv "CST_KINDE_CLIENT_CLIENT_SECRET")
                                         "kinde-client-secret")
                      :redirect-uri "http://localhost:8080/kinde/callback"
                      :logout-redirect-uri "http://localhost:8080/"}})))
