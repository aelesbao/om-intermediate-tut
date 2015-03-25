(ns om-async.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes GET POST PUT]]
            [compojure.route :as route]
            [clojure.edn :as edn]
            [om-async.controller :as c]))

(defroutes routes
  (GET "/" []
       (c/index))

  (GET "/classes" []
       (c/classes))
  (PUT "/classes/:id" {params :params edn-body :edn-body}
       (c/update-class (:id params) edn-body))

  (route/files "/" {:root "resources/public"}))

(defn read-inputstream-edn [input]
  (edn/read
   {:eof nil}
   (java.io.PushbackReader.
    (java.io.InputStreamReader. input "UTF-8"))))

(defn parse-edn-body [handler]
  (fn [request]
    (handler (if-let [body (:body request)]
               (assoc request :edn-body (read-inputstream-edn body))
               request))))

(def handler
  (parse-edn-body routes))
