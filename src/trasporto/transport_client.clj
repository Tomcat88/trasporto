(ns trasporto.transport-client
  (:require [clojure.tools.logging :as log]
            [clj-http.client :as http-client]
            [environ.core :refer [env]]
            [clojure.data.json :as json]))

(def base-url (env :trasporto-base-url))

(defn basic-http-post 
  ([base-url url-param]
   (basic-http-post base-url url-param true))
  ([base-url url-param as-json?]
   (let [response (http-client/post base-url {:form-params {:url url-param}})]
     (log/info response)
     (if as-json? (json/read-str (:body response) :key-fn keyword)
         response))))

(defn get-line-and-direction [line direction]
  (str line "|" (if (and direction (re-matches #"[01]" direction)) direction "0")))

(defn get-line-stops [line direction]
  (let [url (str "tpportal/tpl/journeyPatterns/" (get-line-and-direction line direction))]
    (log/info "get line stops: url:" url)
    (basic-http-post base-url url)))

(defn get-lines []
  (let [url "tpPortal/tpl/journeyPatterns"]
    (log/info "get lines: url:" url)
    (basic-http-post base-url url)))

(defn get-stop [stop-code]
  (let [url (str "tpPortal/geodata/pois/stops/" (Integer/parseInt stop-code))]
    (log/info "get line stops: url:" url)
    (basic-http-post base-url url)))

(defn get-timetable [stop-code line-code direction]
  (let [url (str "tpPortal/tpl/stops/"  (Integer/parseInt stop-code)
                 "/timetable/line/"     line-code
                 "/dir/"                (or direction "0"))]
    (log/info "get timetable: url:" url)
    (basic-http-post base-url url)))
