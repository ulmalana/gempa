(ns gempa.core
  (:require [clojure.data.json :as json]
            [gempa.database :as db])
  (:gen-class))

(defn get-data []
  (slurp "https://data.bmkg.go.id/DataMKG/TEWS/autogempa.json"))

(defn mapify [json-string]
  (json/read-json json-string))


(defn get-data-gempa []
  (mapify (get-data)))

;; get magnitude

(defn extract-data [map-data]
  (let [extract (get-in map-data [:Infogempa :gempa])]
    (select-keys extract [:DateTime :Coordinates
                          :Magnitude :Kedalaman
                          :Wilayah :Shakemap])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;;;;

