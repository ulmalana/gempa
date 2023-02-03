(ns gempa.core
  (:require [clojure.data.json :as json]
            [gempa.database :as db]
            [gempa.route :as route]
            [ring.adapter.jetty :as jetty])
  (:gen-class))

(defn get-data []
  (slurp "https://data.bmkg.go.id/DataMKG/TEWS/autogempa.json"))

(defn mapify [json-string]
  (json/read-json json-string))


(defn get-data-gempa []
  (mapify (get-data)))

;; get magnitude

(defn extract-data [map-data]
  (let [extract (get-in map-data [:Infogempa :gempa])
        selected-keys (select-keys extract [:DateTime :Coordinates
                                            :Magnitude :Kedalaman
                                            :Wilayah :Shakemap])
        ;;hash-id (hash (vals (select-keys selected-keys [:Datetime :Coordinates])))
        ]
    ;;(assoc selected-keys :dataid (str hash-id))
    selected-keys))

(defn insert-data-to-db [extracted-data]
  (db/insert-data db/db extracted-data))

(defn add-new-data []
  (let [data (extract-data (get-data-gempa))]
    (insert-data-to-db data)))

(defn pull-data-bmkg! []
  (let [data-puller (Thread. (fn []
                               (while true
                                ;; (println "Pulling data from BMKG...")
                                (add-new-data)
                                ;;(println "Data pulled.")
                                (Thread/sleep 45000))))]
    (.start data-puller)))

(def gempa-web
  (jetty/run-jetty #'route/app {:port 5000
                                :join? false}))

(defn -main
  "I don't do a whole lot ... yet"
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (jetty/run-jetty #'route/app {:port port
                                  :join? false})))

;;;;

