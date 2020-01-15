(ns redbox.core-check
  (:require [clojure.test.check :as check]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            )
  )

(set! *warn-on-reflection* true)

(def property
  (prop/for-all [v (gen/vector gen/small-integer)]
    (let [s (sort v)]
      (and (= (count v) (count s))
           (or (empty? s)
               (apply <= s))))))

(check/quick-check 100 property)

(gen/sample gen/small-integer)
