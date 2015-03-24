(ns om-async.http
  (:require [cljs.reader :as reader]
            [goog.events :as events])
  (:import [goog.net EventType XhrIo]))

(def ^:private meths
  {:get "GET"
   :put "PUT"
   :post "POST"
   :delete "DELETE"})

(defn edn-xhr [{:keys [method url data on-complete]}]
  (let [xhr (XhrIo.)]
    (events/listen xhr EventType.COMPLETE
                   (fn [_] (-> xhr .getResponseText reader/read-string on-complete)))
    (. xhr
       (send url (meths method) (when data (pr-str data))
             #js {"Content-Type" "application/edn"}))))