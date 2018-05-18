# clj-with
Macro mimicking Elixir's `with` statement

```clojure
(defn f1 [] [:foo "bar"])
(defn f2 [str] [:ok 1])
(defn produce-error [number] [:error :vishh])

(with [[:foo str] (f1)
       [:ok number] (f2 str)
       [:ok _] (produce-error number)]
  :else
    [:error err] :stuff
    [:error :vish] :another-stuff) ;  returns `:another-stuff`

(with [[:foo str] (f1)
       [:ok number] (f2 str)]
     (inc number)
  :else
    [:error err] :stuff
    [:error :vish] :another-stuff) ;  returns `2`

```
