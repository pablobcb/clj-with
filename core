(ns clj-with.core)

(ns slip-api.with
  (:require [clojure.core.match :refer [match]]))

(defmacro with [bindings & rest]
  (assert vector? bindings)
  (let [[body else-bindings] (split-with #(not (keyword? %)) rest)
        else-bindings (next else-bindings)
        [clause val] (first (partition 2 bindings))
        [clause* then] (first (partition 2 else-bindings))]
    `(match ~val
            ~clause (do ~@body)
            ~clause* ~then)))

#_(macroexpand-1 '(with [[n 1] [1 1]]
                        (+ n 1)
  
                        :else
                        [:error data] :ok))



(defn -main
  "Hello `with`"
  []
  (with [[n 1] [10 1]]
      (+ n 1)

      :else
      [:error data] :ok))
