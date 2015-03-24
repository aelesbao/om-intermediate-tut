(ns om-async.core
  (:require [figwheel.client :as fw]
            [om.core :as om :include-macros true]
            [om-async.views.classes :as classes]))

(enable-console-print!)

(def app-state
  (atom {:classes []}))

(om/root classes/view app-state
         {:target (.getElementById js/document "classes")})

(fw/start {})