(ns gempa.route
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults
                                             site-defaults]]
            [ring.middleware.anti-forgery :refer :all]
            [gempa.view :as view]))

(defroutes app-routes
  (GET "/" []
       (view/home-page-hiccup))
  (GET "/selmer" []
       (view/home-page-selmer))
  (GET "/data/:dataid" [dataid]
       (view/data-page dataid))
  (GET "/cari-data" []
       (view/cari-data))
  (POST "/cari-data" {params :params}
        (view/tampilkan-cari params))

  (route/resources "/")
  (route/not-found "Halaman tidak ditemukan"))

(def app
  (wrap-defaults app-routes site-defaults))
