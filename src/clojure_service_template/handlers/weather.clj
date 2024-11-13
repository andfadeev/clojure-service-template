(ns clojure-service-template.handlers.weather
  (:require [babashka.http-client :as http-client]
            [clojure.string :as str]
            [camel-snake-kebab.core :as csk]
            [clojure-service-template.utils :as utils]
            [charred.api :as charred]
            [clojure-service-template.queries :as queries]
            [next.jdbc :as jdbc]))

(defn request->weather-api-base-url
  [request]
  (-> request :dependencies :config :weather-api :base-url))

(defn get-weather-forecast-handler
  [request]
  (let [uri (str/join "/" [(request->weather-api-base-url request)
                           "v1"
                           "forecast"])
        query-params {"hourly" "temperature_2m"
                      "latitude" "52.52"
                      "longitude" "13.41"}
        response (http-client/get uri {:query-params query-params})]

    (when-not (= 200 (:status response))
      (throw (ex-info "Failed to get weather forecast"
                      {:uri uri
                       :query-params query-params
                       :response response})))

    (let [response (charred/read-json
                    (:body response)
                    :key-fn csk/->kebab-case-keyword)
          {:keys [time temperature-2m]} (:hourly response)
          weather-forecast-entries (into (sorted-map)
                                         (map (fn [time temperature]
                                                [time temperature])
                                              time temperature-2m))]

      (jdbc/with-transaction
        [tnx (utils/request->database request)]
        (let [dependencies (assoc (:dependencies request) :database tnx)]
          (doseq [weather-forecast-entry weather-forecast-entries]
            (queries/insert-weather-forecast-entry!
              dependencies
              weather-forecast-entry))))

      (utils/json {:weather-forecast-entries weather-forecast-entries}))))