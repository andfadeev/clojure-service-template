(ns clojure-service-template.types)

(def WebServerComponentConfig
  [:map {:closed true}
   [:host :string]
   [:port :int]
   [:cookie-store-key [:vector :int]]])

(def KindeClientComponentConfig
  [:map {:closed true}
   [:domain :string]
   [:client-id :string]
   [:client-secret :string]
   [:redirect-uri :string]
   [:logout-redirect-uri :string]])

(def DatabaseComponentConfig
  [:map {:closed true}
   [:username :string]
   [:password :string]
   [:jdbcUrl :string]])

(def ServiceConfig
  [:map {:closed true}
   [:web-server WebServerComponentConfig]
   [:database DatabaseComponentConfig]
   [:kinde-client KindeClientComponentConfig]
   [:weather-api
    [:map
     [:base-url :string]]]])