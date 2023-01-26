(ns gempa.route
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults
                                             site-defaults]]
            [gempa.view :as view]))

(defroutes app-routes
  (GET "/" []
       (view/home-page))

  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (wrap-defaults app-routes site-defaults))
