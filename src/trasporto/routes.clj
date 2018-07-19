(ns trasporto.routes
  (:require [clojure.tools.logging :as log]
            [trasporto.transport-client :refer [get-line-stops get-stop get-lines]]
            [ring.util.response :refer [response header]]
            [clojure.data.json :as json]))

(defn wrap-ct-json [res] (header res "Content-Type" "application/json"))

(defn json-ex [status msg] (-> {:status status :msg msg}
                               json/write-str
                               response
                               wrap-ct-json))

(defn finalize-response [res raw convert-f]
  (-> (response (json/write-str (if raw res (convert-f res)))) wrap-ct-json))


(defn line-stops-route [line direction raw]
  (try (let [res (get-line-stops line direction)]
         (log/trace res)
         (finalize-response res raw identity))
       (catch Throwable t
         (log/error t)
         (json-ex 500 (.getMessage t)))))


(defn stop-route [stop-code raw]
  (try (let [res (get-stop stop-code)]
         (log/info res)
         (finalize-response res raw identity))
       (catch Throwable t
         (log/error t)
         (json-ex 500 (.getMessage t)))))


(defn lines-route [raw]
  (try (let [res (get-lines)]
         (log/info res)
         (finalize-response res raw identity))
       (catch Throwable t
         (log/error t)
         (json-ex 500 (.getMessage t)))))
