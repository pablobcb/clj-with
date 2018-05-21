(ns clj-with.core)

(ns slip-api.with
  (:require [clojure.core.match :refer [match]]))


(defn ^:private match* [body bindings else-bindings]
  (if (empty? bindings)
    (do ~@body)
    (let [[clause expr] (first bindings)
          value (eval expr)]
      `(match '~value
         ~clause
          ~'body
         ;(match* ~body ~(next bindings) ~else-bindings)

         ~'else
         (match ~'else
           ~@else-bindings)
         ))))

(defmacro with [bindings & rest]
  (assert vector? bindings)
  (let [[body else-bindings] (split-with #(not (keyword? %)) rest)
        else-bindings (next else-bindings)]
    (match* (first body) (partition 2 bindings) else-bindings)))


#_(macroexpand-1 '(with [[n 1] [1 1]]
                        (+ n 1)
  
                        :else
                        [:error data] :ok)
                         
                        wildcard :wildcard)



(defn -main
  "Hello `with`"
  []
  (with [[n 1] [10 1]]
      (+ n 1)

      :else
      [:error data] :ok))
