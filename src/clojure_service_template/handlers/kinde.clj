(ns clojure-service-template.handlers.kinde
  (:require [jsonista.core :as json]
            [ring.util.response :as response])
  (:import (com.kinde KindeClient)
           (com.kinde.authorization AuthorizationUrl)
           (com.kinde.token KindeTokens)
           (com.kinde.user UserInfo)
           (com.nimbusds.oauth2.sdk.pkce CodeVerifier)
           (java.net URI)))

(def KINDE_STATE_COOKIE "kinde-state")

(defn authorization-url->cookie-string
  [_request ^AuthorizationUrl authorization-url]
  {:value (json/write-value-as-string
           {:url (.toString (.getUrl authorization-url))
            :code-verifier (.getValue (.getCodeVerifier authorization-url))})})
   ;; TODO: configure properties for prod
   ;:path      - the subpath the cookie is valid for
   ;:domain    - the domain the cookie is valid for
   ;:max-age   - the maximum age in seconds of the cookie
   ;:expires   - a date string at which the cookie will expire
   ;:secure    - set to true if the cookie requires HTTPS, prevent HTTP access
   ;:http-only - set to true if the cookie is valid for HTTP and HTTPS only
   ;(ie. prevent JavaScript access)
   ;:same-site - set to :strict or :lax to set SameSite attribute of the cookie

(defn request->authorization-url
  ^AuthorizationUrl
  [request]
  (let [state-cookie (json/read-value (get-in request [:cookies KINDE_STATE_COOKIE :value]))]
    (AuthorizationUrl.
     (.toURL (URI. (get state-cookie "url")))
     (CodeVerifier. (get state-cookie "code-verifier")))))

(defn request->kinde-client
  ^KindeClient [request]
  (-> request :dependencies :kinde-client :kinde-client))

(defn login
  [request]
  (let [kinde-client (request->kinde-client request)
        kinde-client-session (.clientSession kinde-client)
        authorization-url (.login kinde-client-session)]

    (assoc (response/redirect (.toString (.getUrl authorization-url)))
           :cookies {KINDE_STATE_COOKIE (authorization-url->cookie-string request authorization-url)})))

(defn signup
  [request]
  (let [authorization-url (.register
                           (.clientSession
                            (request->kinde-client request)))]
    (assoc (response/redirect (.toString (.getUrl authorization-url)))
           :cookies {KINDE_STATE_COOKIE (authorization-url->cookie-string request authorization-url)})))

(defn logout
  [request]
  (let [authorization-url (.logout
                           (.clientSession
                            (request->kinde-client request)))]

    (assoc (response/redirect (.toString (.getUrl authorization-url)))
           :session nil)))

(defn kinde-callback
  [request]
  (let [{:keys [code]} (:params request)
        ^KindeTokens tokens (-> (.initClientSession
                                 (request->kinde-client request)
                                 code (request->authorization-url request))
                                (.retrieveTokens))
        ^UserInfo user-info (-> (.initClientSession
                                 (request->kinde-client request)
                                 (.getAccessToken tokens))
                                (.retrieveUserInfo))]
    (assoc (response/redirect "/")
           :session {:user {:id (.getSubject user-info)
                            :email (.getEmail user-info)
                            :picture (.getPicture user-info)}})))
