(ns clojure-service-template.handlers.index
  (:require [clojure-service-template.utils :as utils]
            [hiccup.page :as hiccup-page]))

(defn layout
  [body]
  [:head
   [:title "Clojure Service Template"]
   (hiccup-page/include-css
     "/assets/css/output.css")
   (hiccup-page/include-js
     "https://unpkg.com/htmx.org@1.9.4")
   body])

(defn index
  [request]
  (let [user (utils/request->user request)
        body [:body.bg-stone-950.text-white
              [:div.container.mx-auto.px-5
               (if user
                 [:div
                  [:h1.text-lg "Welcome"]
                  [:div (str user)]
                  [:div [:a {:href "/logout"} "Logout"]]]
                 [:div
                  [:h1.text-xl.font-bold.text-red-500 "Login or Signup"]
                  [:div [:a {:href "/login"} "Login"]]
                  [:div [:a {:href "/signup"} "Signup"]]])]]]
    (->> body
         (layout)
         (utils/html))))
