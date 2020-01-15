(ns redbox.classifiers-test
  (:require [redbox.classifiers :refer :all]
            [clojure.test :refer :all]))

(deftest travelling-test
  (testing "Foreign commision fee"
    (let [data {:date "18/11/2019"
                :reference "Reference: AT193230039000010415332"
                :amount 921.43
                :description "CITADINES ARC DE TRIOMP PARIS"
                :misc "1.043,00 EUR COMM.FEE 26.75 Process Date 18/11/2019 Currency Conversion Rate 1.1657 Commission Amount 26.75"}]
      (is (-> data
              travelling
              :tag
              (= "travelling"))))))

(deftest full-classifier-test
  (testing "Negative amount is payment"
    (let [data {:date "18/11/2019"
                :reference "Reference: AT193230039000010415332"
                :amount -921.43
                :description "CITADINES ARC DE TRIOMP PARIS"
                :misc "1.043,00 EUR COMM.FEE 26.75 Process Date 18/11/2019 Currency Conversion Rate 1.1657 Commission Amount 26.75"}]
      (is (-> data
              full-classifier
              :tag
              (= "payment")))))
  (testing "Classifier should be able to classify well known cases."
    (is (-> {:description "BOOTS Optician"}
            full-classifier
            :tag
            (= "beauty")))))

