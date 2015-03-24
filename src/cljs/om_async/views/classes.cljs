(ns om-async.views.classes
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-async.http :as http]
            [om-async.components.editable :as e-com]))

(defn on-edit [id title]
  (http/edn-xhr
    {:method      :put
     :url         (str "class/" id)
     :data        {:class/title title}
     :on-complete #(println "server response:" %)}))

(defn- load-classes [app]
  (http/edn-xhr
    {:method      :get
     :url         "classes"
     :on-complete #(om/transact! app :classes (fn [_] %))}))

(defn view [app _]
  (reify
    om/IWillMount
    (will-mount [_]
      (load-classes app))

    om/IRender
    (render [_]
      (dom/div #js {:id "classes"}
               (dom/h2 nil "Classes")
               (apply dom/ul nil
                      (map
                        (fn [class]
                          (let [id (:class/id class)]
                            (om/build e-com/editable class
                                      {:opts {:edit-key :class/title
                                              :on-edit  #(on-edit id %)}})))
                        (:classes app)))))))