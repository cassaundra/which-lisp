(ns which-lisp.style
  (:require
   [stylefy.core :as s]))

(s/init)

(defn constant-styles []
  (s/tag "body"
         {
          :margin "0 auto"
          :padding-top "72px"
          ;; :-webkit-font-smoothing "antialiased"
          ;; :font-size "1.125em"
          ;; :color "#333"
          ;; :line-height "1.5em"
          }))
