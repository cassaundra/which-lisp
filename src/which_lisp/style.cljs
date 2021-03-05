(ns which-lisp.style
  (:require
   [stylefy.core :as s]))

(s/init)

(def text-style
  {:background-color "red"})
(defn constant-styles []
  (s/tag "body"
         {:font-family "'Helvetica Neue', Verdana, Helvetica, Arial, sans-serif"
          :max-width "600px"
          :margin "0 auto"
          :padding-top "72px"
          :-webkit-font-smoothing "antialiased"
          :font-size "1.125em"
          :color "#333"
          :line-height "1.5em"})
  (s/tag "h1, h2, h3" {:color "#000"})
  (s/tag "h1" {:font-size "2.5em"})
  (s/tag "h2" {:font-size "2em"})
  (s/tag "h3" {:font-size "1.5em"})
  (s/tag "a" {:text-decoration "none"
              :color "#09f"})
  (s/tag "a:hover" {:text-decoration "underline"}))
