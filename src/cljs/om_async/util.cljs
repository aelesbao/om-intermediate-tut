(ns om-async.util)

(defn display [show]
  (if show
    #js {}
    #js {:display "none"}))