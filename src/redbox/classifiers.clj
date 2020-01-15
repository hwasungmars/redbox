(ns redbox.classifiers
  (:require [clojure.tools.logging :as logging])
  )

(set! *warn-on-reflection* true)

(defn classifier
  "Extract field and classify."
  [predicate? tag data]
  (try
    (if (predicate? data)
      (assoc data :tag tag)
      data)
    (catch Exception e
      (logging/warn "Failed to classify [" tag "]; skipping:" data)
      data)))

(defn desc-classifier
  "Classify based on description."
  [predicate? tag]
  (partial classifier #(-> % :description predicate?) tag))

(def cafe
  "Identify cafe and tag them."
  (let [is-cafe (fn [^String desc] (or
                                     (.contains desc "COFFEE")
                                     (.contains desc "ESPRESSO")
                                     (.contains desc "PRET A MANGER")
                                     (.startsWith desc "CAFFE NERO")
                                     (.startsWith desc "COSTA")
                                     (.startsWith desc "EX CELLAR")
                                     (.startsWith desc "PARADE AND ALBANY")
                                     (.startsWith desc "SOPRATTUTTO")
                                     ))]
    (desc-classifier is-cafe "cafe")))

(def beauty
  "Identify beauty item and tag them."
  (let [is-beauty (fn [^String desc] (or
                                       (.startsWith desc "BOOTS")
                                       (.contains desc "CLEANER")
                                       ))]
    (desc-classifier is-beauty "beauty")))

(def eating-out
  "Identify eating out and tag them."
  (let [is-eating-out (fn [^String desc] (or
                                           (.contains desc "THAI")
                                           (.startsWith desc "HARTS BOATYARD")
                                           (.startsWith desc "NANDO")
                                           (.startsWith desc "WAGAMAMA")
                                           ))]
    (desc-classifier is-eating-out "eating-out")))

(def groceries
  "Identify groceries and tag them."
  (let [is-groceries (fn [^String desc] (or
                                          (.startsWith desc "H MART")
                                          (.startsWith desc "MARKS & SPENCER")
                                          (.startsWith desc "MARKS AND SPENCER")
                                          (.startsWith desc "POUNDLAND ")
                                          (.startsWith desc "SAINSBURY'S")
                                          (.startsWith desc "TESCO")
                                          (.startsWith desc "W H SMITH")
                                          (.startsWith desc "WAITROSE")
                                          ))]
    (desc-classifier is-groceries "groceries")))

(def kids
  "Identify kids item and tag them."
  (let [kids? (fn [^String desc] (or
                                   (.contains desc "BABY")
                                   (.contains desc "JOJOMAMAN")
                                   (.contains desc "SLING")
                                   (.contains desc "TOY")
                                   (.startsWith desc "JOJO MAMAN")
                                   (.startsWith desc "MOTHERCARE")
                                   ))]
    (desc-classifier kids? "kids")))

(def shopping
  "Identify shopping and tag them."
  (let [is-shopping (fn [^String desc] (or
                                         (.contains desc "AMAZON")
                                         (.startsWith desc "BENTALLS")
                                         (.startsWith desc "FARRAGO")
                                         (.startsWith desc "JOHN LEWIS")
                                         ))]
    (desc-classifier is-shopping "shopping")))

(def exercise
  "Identify exercise items."
  (let [exercise? (fn [^String desc] (or
                                       (.contains desc "LES MILLS")
                                       (.contains desc "WIGGLE")
                                       ))]
    (desc-classifier exercise? "exercise")))

(def technology
  "Identify technology items."
  (let [tech? (fn [^String desc] (.startsWith desc "APPLE"))]
    (desc-classifier tech? "technology")))

(def book
  "Identify book item"
  (let [book? (fn [^String desc] (.startsWith desc "WATERSTONES"))]
    (desc-classifier book? "book")))

(def travelling
  "Identify travelling cost and tag them."
  (let [travelling? (fn [data] (let [^String desc (:description data)
                                     ^String misc (:misc data)]
                                 (or
                                   (.contains desc "TFL")
                                   (.startsWith desc "BA HIGH LIFE")
                                   (.contains misc "COMM.FEE")
                                   )))]
    (partial classifier travelling? "travelling")))

(def payment
  "Identify payment into the account."
  (let [payment? (fn [data] (-> data :amount (< 0)))]
    (partial classifier payment? "payment")))

(def default
  "If no tag has been assigned, assign default unknown tag."
  (let [not-tagged? (fn [data] (-> data :tag nil?))]
    (partial classifier not-tagged? "unknown")))

(defn full-classifier
  "The classifier to run the data through."
  [data]
  (-> data
      book
      beauty
      cafe
      eating-out
      exercise
      groceries
      kids
      payment
      shopping
      technology
      travelling

      default
      ))
