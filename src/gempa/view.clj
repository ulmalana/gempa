(ns gempa.view
  (:require [gempa.database :as db]
            [clojure.string :as str]
            [hiccup.page :as page]
            [ring.util.anti-forgery :as util]
            [selmer.parser :as selmer]))

(defn gen-page-head [title]
  [:head
   [:title (str title)]])

(def header-links
  [:div#header-links
   [:a {:href "/"} "Home"]])

(defn home-page-hiccup []
  (let [all-data (db/get-all)]
    (page/html5
     (gen-page-head "Info gempa")
     header-links
     [:h1 "Info gempa"]
      (for [data all-data]
        [:div
         [:p (str "Waktu: " (:datetime data))]
         [:p (str "Koordinat: " (:coordinates data))]
         [:p (str "Wilayah: " (:wilayah data))]
         [:p (str "Magnitude: " (:magnitude data))]
         [:p (str "Kedalaman: " (:kedalaman data))]
         [:p (str "Shakemap: ")]
         [:img {:src (str "https://data.bmkg.go.id/DataMKG/TEWS/"
                          (:shakemap data))}]
         [:hr]]))))

(defn home-page-selmer []
  (let [all-data (db/get-all)]
    (selmer/render-file "test.html" {:all-data all-data})))

(defn data-page [dataid]
  (let [data-one (db/get-data-by-id dataid)]
    (if (empty? data-one)
      (selmer/render-file "no-data.html" {})
      (selmer/render-file "data.html" {:data (first data-one)}))))
