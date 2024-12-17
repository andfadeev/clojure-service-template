(ns clojure-service-template.core
  (:gen-class)
  (:require [clojure-service-template.types :as t]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [clojure-service-template.components
             [web-server :as web-server]
             [database :as database]
             [kinde-client :as kinde-client]]
            [aero.core :as aero]
            [malli.instrument :as mi]))

(defn build-system
  {:malli/schema [:=> [:cat t/ServiceConfig] :any]}
  [config]
  (component/system-map
   :config config
   ;:kinde-client (kinde-client/new-kinde-client
   ;               (:kinde-client config))
   :database (database/new-database
              (:database config))
   :web-server (component/using
                (web-server/new-web-server
                 (:web-server config))
                [:database
                 ;:kinde-client
                 :config])))

(defn read-config
  []
  (-> "config.edn"
      (io/resource)
      (aero/read-config)))

(defn start-system
  []
  (->> (read-config)
       (build-system)
       (component/start-system)))

(defn -main []
  (log/info "Starting system")
  (mi/instrument!)
  (let [system (start-system)]
    (log/info "System started")
    (.addShutdownHook
     (Runtime/getRuntime)
     (new Thread (fn []
                   (log/info "Stopping system")
                   (component/stop-system system)
                   (log/info "System stopped"))))))
