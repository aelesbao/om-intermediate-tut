(ns om-async.components.editable
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-async.util :as util]))

(defn handle-change [e data edit-key _]
  (om/transact! data edit-key (fn [_] (.. e -target -value))))

(defn commit-change [text owner cb]
  (om/set-state! owner :editing false)
  (cb text))

(defn editable [data owner {:keys [edit-key on-edit]}]
  (reify
    om/IInitState
    (init-state [_]
      {:editing false})

    om/IRenderState
    (render-state [_ {:keys [editing]}]
      (let [text (get data edit-key)]
        (dom/li nil
                (dom/span #js {:style (util/display (not editing))} text)
                (dom/input
                  #js {:style     (util/display editing)
                       :value     text
                       :onChange  #(handle-change % data edit-key owner)
                       :onKeyDown #(when (= (.-key %) "Enter")
                                    (commit-change text owner on-edit))
                       :onBlur    (fn [_]
                                    (when (om/get-state owner :editing)
                                      (commit-change text owner on-edit)))})
                (dom/button
                  #js {:style   (util/display (not editing))
                       :onClick #(om/set-state! owner :editing true)}
                  "Edit"))))))