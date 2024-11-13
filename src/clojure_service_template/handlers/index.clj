(ns clojure-service-template.handlers.index
  (:require [clojure-service-template.utils :as utils]
            [hiccup.page :as hiccup-page]))

(defn layout
  [body]
  [:head
   [:title "Clojure Service Template"]
   (hiccup-page/include-js
     "https://cdn.tailwindcss.com?plugins=forms"
     "https://unpkg.com/htmx.org@1.9.4")
   body])

(defn index
  [request]
  (let [user (utils/request->user request)
        body [:body
              [:div.max-w-screen-xl.mx-auto.px-5
               (if user
                 [:div
                  [:h1 "Welcome"]
                  [:div (str user)]
                  [:div [:a {:href "/logout"} "Logout"]]]
                 [:div
                  [:h1 "Login or Signup"]
                  [:div [:a {:href "/login"} "Login"]]
                  [:div [:a {:href "/signup"} "Signup"]]])]]]
    (->> body
         (layout)
         (utils/html))))
