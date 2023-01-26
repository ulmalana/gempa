(ns gempa.database
  (:require [clojure.java.jdbc :as jdbc]))


(def testdata
  {:url "http://example.com"
   :title "sqlite example"
   :body "example clojure sqlite"})

(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "db/gempa.db"})

(defn create-db []
  (try
    (jdbc/db-do-commands db
                         (jdbc/create-table-ddl :gempa
                                                [[:DateTime :datetime :default :current_timestamp]
                                                 [:Coordinates :text]
                                                 [:Magnitude :text]
                                                 [:Kedalaman :text]
                                                 [:Wilayah :text]
                                                 [:Shakemap :text]]))
    (jdbc/execute! db ["create unique index gempa_date_coord on gempa (datetime, coordinates)"])
    (catch Exception e
      (println (.getMessage e)))))

(defn print-result-set [result-set]
  (doseq [row result-set]
    (println row)))

(defn get-all []
  (jdbc/query db ["select * from gempa"]))

;; (defn insert-data [db map-data]
;;   (jdbc/insert! db :gempa map-data))

(defn insert-data [db map-data]
  (jdbc/execute! db
                 ["insert or ignore into gempa (datetime, coordinates, magnitude, kedalaman, wilayah, shakemap) values (?, ?, ?, ?, ?, ?)"
                  ;; (:dataid map-data)
                  (:DateTime map-data)
                  (:Coordinates map-data)
                  (:Magnitude map-data)
                  (:Kedalaman map-data)
                  (:Wilayah map-data)
                  (:Shakemap map-data)]))

;;(create-db)
;;(jdbc/insert! db :news testdata)
;;(print-result-set (output))
