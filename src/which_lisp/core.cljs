(ns which-lisp.core
    (:require
     [which-lisp.style :as s]
     [reagent.core :as r]
     [reagent.dom :as d]
     [stylefy.core :as stylefy :refer [use-style]]))

(defn home-page []
  [:div [:h1 "Which Lisp?"]])

(defn mount-root []
  (s/constant-styles)
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
