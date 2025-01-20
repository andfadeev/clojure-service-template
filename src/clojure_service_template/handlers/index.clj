(ns clojure-service-template.handlers.index
  (:require [clojure-service-template.utils :as utils]
            [hiccup.page :as hiccup-page]
            [clojure-service-template.svg :as svg]))

(defn service-version
  []
  (or (System/getenv "SERVICE_VERSION")
      (random-uuid)))

(defn with-service-version
  [path]
  (str path "?v=" (service-version)))

(defn layout
  [body]
  [:head
   [:title "Clojure Service Template"]
   (hiccup-page/include-css
    (with-service-version "/assets/css/output.css"))
   [:script {:src (with-service-version "/assets/js/theme.js")}]
   [:script {:src "https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js"}]
   [:script {:src "https://unpkg.com/htmx.org@2.0.4"
             :integrity "sha384-HGfztofotfshcF7+8n44JQL2oJmowVChPTg48S+jvZoztPfvwD79OC/LTtG6dMp+"
             :crossorigin "anonymous"}]
   body])

(defn theme-toggle
  []
  [:div
   [:button {"@click" "$store.theme.lightTheme()"
             :class "text-white rounded p-1 hover:text-yellow-500"
             :x-show "$store.theme.dark"}
    (svg/sun)]

   [:button {"@click" "$store.theme.darkTheme()"
             :class "text-gray-400 rounded p-1 hover:text-sky-900"
             :x-show "!$store.theme.dark"}
    (svg/moon)]])

(defn nav
  []
  [:nav {:class "flex justify-between container py-5 items-center"}
   [:a {:class "text-xl font-bold"} "Clojure Service Template"]
   (theme-toggle)])

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
