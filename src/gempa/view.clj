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

(defn convert-to-local-time [time-string]
  (let [parsed-time (ZonedDateTime/parse time-string)
        converted (jt/with-zone-same-instant parsed-time "+07:00")]
    (str converted)))

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

(defn cari-data []
  (let [data (db/get-all)]
    (if (empty? data)
      (selmer/render-file "no-data.html" {})
      (page/html5
       (gen-page-head "Cari data gempa")
       header-links
       [:h2 "Cari data gempa berdasarkan magnitude"]
       [:form {:action "/cari-data" :method "POST"}
        (util/anti-forgery-field)
        [:p "Magnitude minimal: " [:input {:type "number" :name "min-mag"}]]
        [:p "Magnitude maksimal: " [:input {:type "number" :name "max-mag"}]]
        [:p [:input {:type "submit" :value "Cari"}]]]))))

(defn tampilkan-cari [{:keys [min-mag max-mag]}]
  (let [c-min (read-string min-mag)
        c-max (read-string max-mag)
        result (db/get-data-by-mag-range c-min c-max)]
    (if (empty? result)
      (page/html5
       [:h1 "pencarian tidak ada hasil."])
      (page/html5
       (gen-page-head "Info gempa")
       header-links
       [:h1 "Info gempa"]
       (for [data result]
         [:div
          [:p (str "Waktu: " (:datetime data))]
          [:p (str "Koordinat: " (:coordinates data))]
          [:p (str "Wilayah: " (:wilayah data))]
          [:p (str "Magnitude: " (:magnitude data))]
          [:p (str "Kedalaman: " (:kedalaman data))]
          [:p (str "Shakemap: ")]
          [:img {:src (str "https://data.bmkg.go.id/DataMKG/TEWS/"
                           (:shakemap data))}]
          [:hr]])))))
