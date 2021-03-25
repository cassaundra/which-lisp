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

(defonce language-table-data (r/atom nil))

(def language-table-columns '("Language" "Type" "Target(s)" "Implementation(s)" "Features" "Status"))

;; TODO improve?
(defn read-language-table-data []
  (go (let [response (<! (http/get "data.csv"))]
        (reset! language-table-data (csv/read-csv (str/trim (:body response)) :separator "|")))))

;; TODO clean up

(defn language-type [type-code]
  (case type-code
    "cl" "Common Lisp-like"
    "clj" "Clojure-like"
    "extra" "Extra semantics"
    "scm" "Scheme-like"
    "sexp" "S-exp mapping"
    "Unknown"))

(defn language-badges [features]
  (for [feature (str/split features " ")]
    ^{:key feature}
    [:span
     [:span {:class "badge rounded-pill bg-secondary"} feature]
     " "]))

(defn language-table-row [entry]
  (let [[lang url type-code target impl description features status] entry]
    (list [:a {:href url :target "blank"} lang]
          [language-type type-code] ; expand abbrevation to human-readable name
          target
          impl
          [:span (use-style {:white-space "pre-wrap"}) ; description and features in same line
           description "\n"
           (language-badges features)]
          status)))

(defn language-table []
  (when @language-table-data
    [:table.table
     ;; table headers
     [:thead
      [:tr
       (for [col language-table-columns]
         ^{:key col}
         [:th {:scope "col"} col])]]
     ;; table data
     [:tbody
      (doall
       (for [[row-idx entry] (map-indexed list @language-table-data)]
         ^{:key row-idx}
         [:tr
          ;; draw columns of row
          (for [[col-idx col] (map-indexed list (language-table-row entry))]
            ^{:key col-idx}
            [:td {:scope "row"} col])]
         ))]
     ]))

;; TODO use Markdown for intro
(defn home-page []
  [:div.container
   [:h1 "Which Lisp?"]
   [:p "Lisp is great, but there are so many dialects! How do you choose?"]
   [:p
    "This tool is designed to help those interested in learning to use a lisp-flavored "
    "language, but don't know where to start."]
   [:p "Special thanks to the contributors of "
    [:a {:href "https://github.com/dundalek/awesome-lisp-languages" :target "blank"} "awesome-lisp-languages"]
    ", from which much of the information on this site originates."]
   [:h2 "Languages"]
   [:div {:class "alert alert-warning"} [:b "Note: "] "The information in the following table is approximately correct, but has yet to be thoroughly reviewed and may contain errors or omissions."]
   [language-table]])

(defn mount-root []
  (s/constant-styles)
  (d/render [home-page] (.getElementById js/document "app"))
  (read-language-table-data))

(defn ^:export init! []
  (mount-root))
