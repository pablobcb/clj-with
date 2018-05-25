# clj-with
Macro inspired by Elixir's `with` statement

[![Clojars Project](https://img.shields.io/clojars/v/clj-with.svg)](https://clojars.org/clj-with)

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
  (do (println "all clauses have matched :)")
      (* z 2))
      
  :otherwise [:error err] err
  
  :else :no-match)
;=> 62

(with [[:ok x y] (f1)
       [:ok z] (produce-error)]
  (* z 2)
  :otherwise
  [:error err] err)
;=> :match-error
```
For more use cases peek at the `test` folder