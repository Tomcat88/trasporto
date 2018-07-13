(ns trasporto.trasporto-client
  (:require [clj-http.client :as http-client]
            [environ.core :refer [env]]))

(def base-url (env :trasposto-base-url))

(defn get-line-stops [line direction]
  (let [direction-param (if (and direction (re-matches #"[01]" direction)) direction "0")  
        url (str "tpportal/tpl/journeyPatterns/" line "|" direction-param)]
    (println "url:" url)
    (http-client/post base-url
                      {:form-params {:url url}})))
