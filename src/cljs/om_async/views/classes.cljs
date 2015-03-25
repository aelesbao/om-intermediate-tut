(ns om-async.views.classes
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [ajax.core :refer [GET PUT]]
            [om-async.components.editable :as e-com]))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn- get-classes [app]
  (GET "/classes"
       {:handler       #(om/transact! app :classes (fn [_] %))
        :error-handler error-handler}))

(defn update-class [id title]
  (PUT (str "/classes/" id)
       {:params        {:class/title title}
        :handler       #(println "server response:" %)
        :error-handler error-handler}))

(defn view [app _]
  (reify
    om/IWillMount
    (will-mount [_]
      (get-classes app))

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
                                              :on-edit  #(update-class id %)}})))
                        (:classes app)))))))