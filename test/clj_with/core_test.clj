(ns clj-with.core-test
  (:require [clojure.test :refer :all]
            [clj-with.core :refer :all]))

(defn f1 [] [:ok 10 20])
(defn f2 [k] [:ok (inc k)])
(defn produce-error [] [:error :match-error])

(deftest pattern-match-ok
  (is (= 62
         (with [[:ok x y] (f1)
                [:ok z] (f2 (+ x y))]
               (do :nothing
                   (* z 2))

               :else
               [:error err] err))))

(deftest pattern-match-wild-card
  (is (= 62
         (with [[:ok x y] (f1)
                [:ok z] (f2 (+ x y))
                [wildcard] 62]
               (do :nothing
                   wildcard)

               :else
               [:error err] err))))

(deftest ignored-vars
  (is (= 10
         (with [[:ok x _] (f1)] x
               :else [:error err] err))))

(deftest no-else-provided
  (is (= 10 (with [[:ok x _] (f1)] x))))

(deftest else
  (= :match-error
     (with [[:ok x y] (f1)
            [:ok z] (produce-error)]
           (* z 2)
           :else
           [:error err] err)))

(deftest no-match
  (is (thrown? IllegalArgumentException
               (with [[:ok x y] (f1)
                      [:ok z] [:no-match :_ :-]]
                     (* z 2)
                     :else
                     [:error err] err))))

(deftest double-else
  (is (= :ok
         (with [[:ok x y] (f1)
                [:ok z] [:no-match :_ :-]]
               (* z 2)

               :else
               [:error err] err

               :else :ok))))

(deftest else-wild-card
  (is (= [:else-wildcard]
         (with [[:ok x y] (f1)
                [:ok z] [:else-wildcard]]
               (* z 2)

               :else wildcard wildcard))))
