# clj-with
Macro inspired by Elixir's `with` statement

Macro
  Chain pattern matching clauses, using `clojure.core.match/match`.  Take a vector of
  bindings to match, a body, and optionally an `:else` keyword followed by list
  of clauses to match in case of failure. Pattern matches each bind and if all
  clauses match, the body is executed returning its result. Otherwise the chain
  is aborted and the non-matched value is matched against the optional :else
  clauses, if provided. Raises `java.lang.IllegalArgumentException` if none
  of the clauses match, just as `clojure.core.match/match`.  
  
  
  Example:
```clojure
(defn f1 [] [:ok 10 20])
(defn f2 [k] [:ok (inc k)])
(defn produce-error [] [:error :match-error])

(with [[:ok x y] (f1)
       [:ok z] (f2 (+ x y))]
  (* z 2)
  :else
  [:error err] err)
;=> 62

(with [[:ok x y] (f1)
       [:ok z] (produce-error)]
  (* z 2)
  :else
  [:error err] err)
;=> :match-error
```
