(ns om-async.controller
  (:require [ring.util.response :refer [file-response]]
            [datomic.api :as d]))

(def uri "datomic:free://localhost:4334/om_async")
(def conn (d/connect uri))

(defn- db [] (d/db conn))

(defn- query-db [query & params]
  (apply (partial d/q query (db)) params))

(defn- generate-response [data & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/edn"}
   :body    (pr-str data)})

(defn index []
  (file-response "public/html/index.html" {:root "resources"}))

(defn update-class [id params]
  (let [title (:class/title params)
        eid   (-> '[:find ?class
                    :in $ ?id
                    :where [?class :class/id ?id]]
                  (query-db id)
                  ffirst)]
    (d/transact conn [[:db/add eid :class/title title]])
    (generate-response {:status :ok})))

(defn classes []
  (let [classes (->> '[:find ?class
                       :where [?class :class/id]]
                     query-db
                     (map #(d/touch (d/entity (db) (first %))))
                     vec)]
    (generate-response classes)))