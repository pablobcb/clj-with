(ns clj-with.core-test
  (:require [clojure.test :refer :all]
            [clj-with.core :refer :all]))

(defn f1 [] [:ok 10 20])
(defn f2 [k] [:ok (inc k)])
(defn produce-error [] [:error :match-error])

(deftest single-expr-body
  (is (= 62
         (with [[:ok x y] (f1)
                [:ok z] (f2 (+ x y))]
               (* z 2)

               :else
               [:error err] err))))

(deftest match-error
  (= :match-error
     (with [[:ok x y] (f1)
            [:ok z] (produce-error)]
           (* z 2)
           :else
           [:error err] err)))
