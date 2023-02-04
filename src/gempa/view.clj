(ns gempa.view
  (:require [gempa.database :as db]
            [clojure.string :as str]
            [hiccup.page :as page]
            [ring.util.anti-forgery :as util]
            [selmer.parser :as selmer]
            [java-time.api :as jt])
  (:import [java.time ZonedDateTime]))


(defn gen-page-head [title]
  [:head
   [:title (str title)]])

(def header-links
  [:div#header-links
   [:a {:href "/"} "Home"]])

(defn home-page-hiccup []
  (let [all-data (db/get-all)
        converted (map #(update % :datetime convert-to-local-time)
                       all-data)]
    (page/html5
     (gen-page-head "Info gempa")
     header-links
     [:h1 "Info gempa"]
      (for [data converted]
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


(defn convert-to-local-time [time-string]
  (let [parsed-time (ZonedDateTime/parse time-string)
        converted (jt/with-zone-same-instant parsed-time "+07:00")]
    (str converted)))

(defn home-page-selmer []
  (let [all-data (db/get-all)
        converted (map #(update % :datetime convert-to-local-time)
                       all-data)]
    (selmer/render-file "test.html" {:all-data converted})))

(defn data-page [dataid]
  (let [data-one (db/get-data-by-id dataid)]
    (if (empty? data-one)
      (selmer/render-file "no-data.html" {})
      (let [extracted (first data-one)
            converted (update extracted :datetime convert-to-local-time)]
       (selmer/render-file "data.html" {:data converted})))))
