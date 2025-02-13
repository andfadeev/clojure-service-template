(ns clojure-service-template.components.web-server
  (:require [clojure-service-template.types :as t]
            [clojure.tools.logging :as log]
            [reitit.ring :as ring]
            [ring.adapter.jetty :as jetty]
            [reitit.ring.middleware.parameters :as parameters]
            [ring.middleware.keyword-params :as keyword-params]
            [reitit.coercion.malli :as coercion-malli]
            [reitit.ring.coercion :as ring-coercion]
            [ring.middleware.session :as session]
            [ring.middleware.session.cookie :as cookie-store]
            [com.stuartsierra.component :as component]
            [clojure-service-template.handlers
             [kinde :as kinde]
             [index :as index]
             [weather :as weather]])
  (:import (org.eclipse.jetty.server Server)))

(defn wrap-dependencies
  [handler context]
  (fn [request]
    (-> request
        (assoc :dependencies context)
        (handler))))

(defn component->cookie-store-key
  [component]
  (-> component
      :component-config
      :cookie-store-key
      (byte-array)))

(defn component->cookie-store
  [component]
  {:cookie-attrs {:secure true
                  :max-age 604800}
   :store (cookie-store/cookie-store
           {:key (component->cookie-store-key component)})})

(defn app
  [component]
  (ring/ring-handler
   (ring/router
    [["/" {:get index/index}]
     ["/login" {:get kinde/login}]
     ["/signup" {:get kinde/signup}]
     ["/logout" {:get kinde/logout}]
     ["/api"
      ["/weather/forecast" {:get weather/get-weather-forecast-handler}]]
     ["/kinde/callback" {:get kinde/kinde-callback}]
     ["/assets/*" (ring/create-resource-handler)]]
    {:data {:coercion coercion-malli/coercion
            :middleware [[wrap-dependencies component]
                         parameters/parameters-middleware
                         keyword-params/wrap-keyword-params
                         ring-coercion/coerce-request-middleware
                         ring-coercion/coerce-response-middleware
                         [session/wrap-session


                          (component->cookie-store component)]]}})
   (ring/routes
    (ring/redirect-trailing-slash-handler)
    (ring/create-default-handler
     {;:not-found error-pages/not-found-page
      :method-not-allowed (constantly {:status 405
                                       :body ""})
      :not-acceptable (constantly {:status 406
                                   :body ""})}))))

(defrecord WebServerComponent
           [component-config]
  component/Lifecycle
  (start [component]
    (log/info "Starting WebServerComponent" component-config)
    (let [web-server (jetty/run-jetty
                      (app component)
                      {:port (:port component-config)
                       :join? false})]
      (assoc component :web-server web-server)))
  (stop [{:keys [web-server] :as component}]
    (when web-server
      (log/info "Stopping WebServerComponent")
      (.stop ^Server web-server))
    (assoc component :web-server nil)))

(defn new-web-server
  {:malli/schema [:=> [:cat t/WebServerComponentConfig] :any]}
  [component-config]
  (map->WebServerComponent
   {:component-config component-config}))