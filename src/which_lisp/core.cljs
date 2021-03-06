(ns which-lisp.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   [cljs-http.client :as http]
   [cljs.core.async :refer [<!]]
   [clojure.string :as str]
   [reagent.core :as r]
   [reagent.dom :as d]
   [stylefy.core :as stylefy :refer [use-style]]
   [testdouble.cljs.csv :as csv]
   [which-lisp.style :as s]))

(def data (r/atom nil))

(defn read-data []
  (go (let [response (<! (http/get "data.csv"))]
        (reset! data (:body response)))))

(defn language-table []
  (when @data
    [:table.table
     [:thead
      [:tr
       (for [col '("Language" "Type" "Target(s)" "Implementation(s)" "Features" "Status" "License")]
         [:th {:scope "col"} col])]]
     [:tbody
      ;; TODO add keys for performance
      (for [entry (csv/read-csv (str/trim @data) :separator "|")]
        [:tr
         (let [cols
           (let [[lang url type target impl description features status license] entry]
             (list [:a {:href url :target "blank"} lang]
                   (case type
                     "cl" "Common Lisp-like"
                     "clj" "Clojure-like"
                     "extra" "Extra semantics"
                     "scm" "Scheme-like"
                     "sexp" "S-exp mapping"
                     "Unknown")
                   target
                   impl
                   [:span (use-style {:white-space "pre-wrap"})
                    description "\n"
                    (for [feature (str/split features " ")]
                      [:span [:span {:class "badge rounded-pill bg-secondary"} feature] " "])]
                   status
                   license
                   ))]
           (for [col cols]
             ;; ^{:key col}
             [:td {:scope "row"} col]))]
        ;; TODO how

        )]])
  )

(defn home-page []
  [:div.container
   [:h1 "Which Lisp?"]
   [:p "Lisp is great, but there are so many dialects! How do you choose?"]
   [:p
    "This tool is designed to help those interested in learning to use a lisp-flavored "
    "language, but don't know where to start."]
   [:p "Special thanks to the contributors of "
    [:a {:href "https://github.com/dundalek/awesome-lisp-languages" :target "blank"}
     "awesome-lisp-languages"]
    ", from which much of the information on this site originates."
    ]
   [:h2 "Table"]
   [language-table]])

(defn mount-root []
  (s/constant-styles)
  (d/render [home-page] (.getElementById js/document "app"))
  (read-data))

(defn ^:export init! []
  (mount-root))
