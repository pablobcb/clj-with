(ns slip-api.with
  (:require
    [clojure.walk :refer :all]
    [clojure.core.match :refer [match]]))

(defn ^:private match* [body bindings else-bindings]
  (if (empty? bindings)
    (do  (prn "ASSADSD")
      body)
    (let [[clause expr] (first bindings)
          value (eval expr)
          x 4]
      `(match '~value
              ~clause
              ;~(make-add x)
              ~(match* body (next bindings) else-bindings)

              ~'else
              (match ~'else
                     ~@else-bindings)))))

(defmacro with [bindings & rest]
  (assert vector? bindings)
  (let [[body else-bindings] (split-with #(not (keyword? %)) rest)
        else-bindings (next else-bindings)
        res (match* (first body) (partition 2 bindings) else-bindings)]
    (clojure.pprint/pprint res)
    res
    ))


(macroexpand-1
  '(with [[n 1] [1 1]
         [m] (* 2 n)]
        (+ m 1)

        ;:else
        ;~[:error data] :ok
        ;
        ;wildcard :wildcard
        )
  )
(with [[n 1] [1 1]
       [m] (* 2 n)]
      (+ m 1)

      ;:else
      ;~[:error data] :ok
      ;
      ;wildcard :wildcard
      )
