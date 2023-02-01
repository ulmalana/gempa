(ns gempa.route
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults
                                             site-defaults]]
            [gempa.view :as view]))

(defroutes app-routes
  (GET "/" []
       (view/home-page-hiccup))
  (GET "/selmer" []
       (view/home-page-selmer))
  (GET "/data/:dataid" [dataid]
       (view/data-page dataid))

  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (wrap-defaults app-routes site-defaults))
