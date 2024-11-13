(ns clojure-service-template.http-test
  (:require [charred.api :as charred]
            [clojure.test :refer :all]
            [clojure-service-template.test-support :as test-support]
            [clojure-service-template.core :refer :all]
            [clojure-service-template.queries :as queries]
            [babashka.http-client :as http]))

(use-fixtures
  :each
  test-support/with-postgresql-test-container
  (test-support/with-system test-support/test-configuration))

(deftest weather-api-test
  (is (= 1 (-> (http/get "http://localhost:8080/api/weather/forecast")
               :body
               (charred/read-json))))
  (is (= [] (queries/get-weather-forecast-entries (test-support/database)))))
