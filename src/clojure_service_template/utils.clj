(ns clojure-service-template.utils
  (:require [charred.api :as charred]
            [hiccup2.core :as html]))

(defn request->user
  [request]
  (-> request :session :user))

(defn request->database
  [request]
  (let [database
        (-> request
            :dependencies
            :database)]
    (database)))

(defn json
  ([body status]
   {:status status
    :body (charred/write-json-str body)
    :headers {"Content-Type" "application/json"}})
  ([body]
   (json body 200)))

(defn html
  ([body]
   (html body 200))
  ([body status]
   {:status status
    :headers {"Content-Type" "text/html"}
    :body (-> body (html/html) (str))}))