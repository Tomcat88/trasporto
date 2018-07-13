(ns trasporto.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-response]]
            [clojure.data.json :as json]
            [ring.util.response :refer [response header]]
            [clj-http.client :as http-client]
            [trasporto.transport-client :refer [get-line-stops]]))


(defroutes app-routes
  (GET "/" [] (response {:foo "bar"}))
  (GET "/line/:line/stops" [line direction]
       (let [res (get-line-stops line direction)]
         (println res)
         (-> (response (json/write-str res)) (header "Content-Type" "application/json"))))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-response)
      (wrap-params)))
