(ns trasporto.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [clj-http.client :as http-client]))


(defroutes app-routes
  (GET "/" [] (response {:foo "bar"}))
  (GET "/line/:line/stops" [line direction]
       (get-line-stops line direction))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-response)
      (wrap-params)))
