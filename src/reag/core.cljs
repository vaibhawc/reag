(ns reag.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as d]
   [cljs-http.client :as http]
   [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;; -------------------------

(def hb {:headers {"Accept" "application/json" "Content-Type" "application/json"}
         :body (js/JSON.stringify (clj->js {:duid "testWeb" :contacts [{:givenName "gn1" :middleName "mn1" :familyName "fn1"}]}))})

(defn add-contacts [val](go (let [response (<! (http/post "https://mithra.vrundavan/add-contacts" hb))]
                             (cond (= "received!" (:msg (:body response))) (reset! val (inc @val))))))

(defn atom-input [val]
  [:input {:type "button"
           :value "add-contacts"
           :on-click #(add-contacts val)}])

(defn shared-state []
  (let [val (r/atom 0)]
    (fn []
      [:div
       [:p "The value is now: " @val]
       [:p "Change it here: " [atom-input val]]])))

(defn app []
  [:div [:h2 "वन्दे कृष्णम् जगद्गुरुम्!"] [shared-state]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [app] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
