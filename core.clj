(ns slip-api.with
  (:require
    [clojure.walk :refer :all]
    [clojure.core.match :refer [match]]))

(defn ^:private match* [body bindings else-bindings]
  (if (empty? bindings)
    body
    (let [[clause expr] (first bindings)]
      `(match (let [val# (eval ~expr)]
                (if (vector? val#)
                  '~expr
                  [val#]))
              ~clause
              ~(match* body (next bindings) else-bindings)

              ~'else
              (match ~'else
                     ~@else-bindings)))))

(defmacro with [bindings & rest]
  (assert vector? bindings)
  (let [[body else-bindings] (split-with #(not (keyword? %)) rest)
        else-bindings (next else-bindings)]
    (match* (first body) (partition 2 bindings) else-bindings)))


#_(with [[n 1] [10 1]
       [m] (* 2 n)]
      (+ m 1)

      :else
      [:error data] :ok

      wildcard :wildcard)
