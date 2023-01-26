(ns gempa.view
  (:require [gempa.database :as db]
            [clojure.string :as str]
            [hiccup.page :as page]
            [ring.util.anti-forgery :as util]))

(defn gen-page-head [title]
  [:head
   [:title (str title)]])

(def header-links
  [:div#header-links
   [:a {:href "/"} "Home"]])

(defn home-page []
  (page/html5
   (gen-page-head "Info gempa")
   header-links
   [:h1 "Info gempa"]
   [:p "selamat datang"]))
