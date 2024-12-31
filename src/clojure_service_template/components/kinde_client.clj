(ns clojure-service-template.components.kinde-client
  (:require [clojure-service-template.types :as t]
            [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log])
  (:import (com.kinde KindeClientBuilder)
           (com.kinde.authorization AuthorizationType)))

(defrecord KindeClientComponent
           [component-config]
  component/Lifecycle
  (start [component]
    (log/info "Starting KindeClientComponent")
    (let [{:keys [domain
                  client-id
                  client-secret
                  redirect-uri
                  logout-redirect-uri]} component-config]
      (assoc component :kinde-client
             (-> (KindeClientBuilder/builder)
                 (.domain domain)
                 (.clientId client-id)
                 (.clientSecret client-secret)
                 (.redirectUri redirect-uri)
                 (.logoutRedirectUri logout-redirect-uri)
                 (.grantType AuthorizationType/CODE)
                 (.addScope "openid email profile")
                 (.build)))))
  (stop [component]
    (log/info "Stopping KindeClientComponent")
    (assoc component :kinde-client nil)))

(defn new-kinde-client
  {:malli/schema [:=> [:cat t/KindeClientComponentConfig] :any]}
  [component-config]
  (map->KindeClientComponent
   {:component-config component-config}))