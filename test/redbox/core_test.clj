(ns redbox.core-test
  (:require [clojure.test :refer :all]
            [redbox.core :refer :all]))

(deftest classifier-test
  (testing "Classifier should be able to classify well known cases."
    (is (= (:tag (classifier {:Description "BOOTS Optician"})) "beauty"))))
