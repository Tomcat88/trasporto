(ns trasporto.transport-client
  (:require [clj-http.client :as http-client]
            [environ.core :refer [env]]
            [clojure.data.json :as json]))

(def base-url (env :trasporto-base-url))

(defn get-line-stops [line direction]
  (let [direction-param (if (and direction (re-matches #"[01]" direction)) direction "0")  
        url (str "tpportal/tpl/journeyPatterns/" line "|" direction-param)]
    (println "url:" url)
    (json/read-str (:body (http-client/post base-url
                                            {:form-params {:url url}})) :key-fn keyword)))
