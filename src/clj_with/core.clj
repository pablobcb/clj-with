(ns clj-with.core
  (:require [clojure.core.match :refer [match]]))

(defn ^:private match* [body bindings else-bindings]
  (if (empty? bindings)
    body
    (let [[clause expr] (first bindings)]
      `(match (let [val# (eval ~expr)]
                (if (vector? val#) val# [val#]))
              ~clause
              ~(match* body (next bindings) else-bindings)

              ~'else
              (match ~'else
                     ~@else-bindings)))))

(defmacro with
  "Chain pattern matching clauses, using 'clojure.core.match/match'. Take a
  vector of bindings to match, a body, and optionally an `:otherwise` keyword
  followed by a list of clauses to match in case of failure.
  Pattern matches each bind and if all clauses match, the body is executed
  returning its result. Otherwise the chain is aborted and the non-matched
  value is matched against the optional :otherwise clauses, if provided.
  Raises 'java.lang.IllegalArgumentException' if none of the clauses match,
  just like 'clojure.core.match/match'.

  Example:
  (defn f1 [] [:ok 10 20])
  (defn f2 [k] [:ok (inc k)])
  (defn produce-error [] [:error :match-error])

  (with [[:ok x y] (f1)
         [:ok z] (f2 (+ x y))]
    (* z 2)
    :otherwise
    [:error err] err)
  ;=> 62

  (with [[:ok x y] (f1)
         [:ok z] (produce-error)]
    (* z 2)
    :otherwise
    [:error err] err)
  ;=> :match-error"
  [bindings & rest]
  (assert vector? bindings)
  (assert (even? (count bindings)))
  (let [[body else-bindings] (split-with #(not (keyword? %)) rest)
        else-bindings (next else-bindings)]
    (match* (first body) (partition 2 bindings) else-bindings)))
