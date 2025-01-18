(ns clojure-service-template.handlers.index
  (:require [clojure-service-template.utils :as utils]
            [hiccup.page :as hiccup-page]
            [clojure-service-template.svg :as svg]))

(defn layout
  [body]
  [:head
   [:title "Clojure Service Template"]
   (hiccup-page/include-css
    (str "/assets/css/output.css?v=" (random-uuid)))
   [:script {:src (str "/assets/js/darkModeInit.js?v=" (random-uuid))}]
   (hiccup-page/include-js
    "https://unpkg.com/htmx.org@1.9.4"
    "https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js")
   body])

(defn nav
  []
  [:nav {:class "flex justify-between container py-5 items-center"}
   [:a {:href "/"
        :class "text-xl font-bold"} "Clojure Service Template"]

   [:button {"@click" "$store.theme.toggleTheme()"
             :class "text-gray-500 bg-slate-50 rounded p-1 hover:text-sky-900"}
    (svg/sun {:x-show "$store.theme.dark"})
    (svg/moon {:x-show "!$store.theme.dark"})]])

(defn index
  [request]
  (let [user (utils/request->user request)
        body [:body {:class "bg-white dark:bg-black dark:text-white"}
              (nav)
              [:div.container
               (if user
                 [:div
                  [:h1.text-lg "Welcome"]
                  [:div (str user)]
                  [:div [:a {:href "/logout"} "Logout"]]]
                 [:div
                  [:h1 {:class "text-xl font-bold text-green-500 dark:text-red-500"} "Login or Signup"]
                  [:div [:a {:href "/login"} "Login"]]
                  [:div [:a {:href "/signup"} "Signup"]]])]]]
    (->> body
         (layout)
         (utils/html))))
