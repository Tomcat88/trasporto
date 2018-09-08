(ns trasporto.handler
  (:require [clojure.tools.logging :as log]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [response header]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.json :refer [wrap-json-response]]
            [trasporto.routes :as t-routes]))

(defroutes app-routes
  (GET     "/" []  (response {:foo "bar"}))
  (OPTIONS "/*" [] (t-routes/add-cors-headers (response {"Allow" "GET"})))
  
  (GET     "/lines" [raw]
           (t-routes/lines-route raw))
  (GET     "/line/:line/stops" [line direction raw]
           (t-routes/line-stops-route line direction raw))
  (GET     "/line/:line/stop/:stop/timetable" [stop line direction raw]
           (t-routes/timetable-route stop line direction raw))
  (GET     "/stop/:code" [code raw]
           (t-routes/stop-route code raw))
  (route/not-found (response {:error "resource not found"})))

(def app
  (-> app-routes
      (wrap-json-response)
      (wrap-params)))
