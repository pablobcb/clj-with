(ns clj-with.core)

(ns slip-api.with
  (:require [clojure.core.match :refer [match]]))


(defmacro with [bindings & rest]
  (assert vector? bindings)
  (let [[body else-bindings] (split-with #(not (keyword? %)) rest)
        else-bindings (next else-bindings)
        [clause expr] (first (partition 2 bindings))
        value (eval expr)
        [clause* then] (first (partition 2 else-bindings))]
    `(match '~value
            ~clause (do ~@body)

            ~clause* ~then

            ~'else ~'else

            )))

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
