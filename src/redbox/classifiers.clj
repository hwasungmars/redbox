(ns redbox.classifiers
  (:require [clojure.tools.logging :as logging])
  )

(set! *warn-on-reflection* true)

(defn classifier
  "Extract field and classify."
  [tag predicate? data]
  (try
    (if (predicate? data)
      (assoc data :tag tag)
      data)
    (catch Exception e
      (logging/warn "Failed to classify [" tag "]; skipping:" data e)
      data)))

(defn desc-classifier
  "Classify based on description."
  [tag predicate?]
  (partial classifier tag #(-> % :description predicate?)))

(def cafe
  (desc-classifier :cafe
                   (fn [^String x]
                     (or
                       (.contains x "COFFEE")
                       (.contains x "ESPRESSO")
                       (.contains x "PRET A MANGER")
                       (.contains x "STARBUCKS ")
                       (.startsWith x "CAFFE NERO")
                       (.startsWith x "COSTA")
                       (.startsWith x "EX CELLAR")
                       (.startsWith x "LES 3 CHOCOLATS")
                       (.startsWith x "PARADE AND ALBANY")
                       (.startsWith x "SOPRATTUTTO")
                       ))))

(def beauty
  (desc-classifier :beauty
                   (fn [^String x]
                     (or
                       (.contains x "CLEANER")
                       (.startsWith x "BOOTS")
                       (.startsWith x "SUPERDRUG")
                       ))))

(def eating-out
  (desc-classifier :eating-out
                   (fn [^String x]
                     (or
                       (.contains x "ARONG")
                       (.contains x "BURGER")
                       (.contains x "CATERING")
                       (.contains x "THAI")
                       (.contains x "XIAOMIAN")
                       (.startsWith x "HARTS BOATYARD")
                       (.startsWith x "KANADA YA")
                       (.startsWith x "MCDONALDS")
                       (.startsWith x "NANDO")
                       (.startsWith x "PIZZA EXPRESS")
                       (.startsWith x "WAGAMAMA")
                       ))))

(def groceries
  (desc-classifier :groceries
                   (fn [^String x]
                     (or
                       (.contains x "HELLOFRESH")
                       (.contains x "MARKSSPENCE")
                       (.contains x "WAITROSE")
                       (.startsWith x "COOK")
                       (.startsWith x "H MART")
                       (.startsWith x "MARKS & SPENCER")
                       (.startsWith x "MARKS AND SPENCER")
                       (.startsWith x "NEXT")
                       (.startsWith x "POUNDLAND")
                       (.startsWith x "SAINSBURY'S")
                       (.startsWith x "TESCO")
                       ))))

(def kids
  (desc-classifier :kids
                   (fn [^String x]
                     (or
                       (.contains x "BABY")
                       (.contains x "CHILDREN")
                       (.contains x "JOJOMAMAN")
                       (.contains x "KIDS")
                       (.contains x "MOTHERCARE")
                       (.contains x "PARTYBAGS")
                       (.contains x "SLING")
                       (.contains x "TOY")
                       (.startsWith x "JOJO MAMAN")
                       (.startsWith x "TUPPENCE AND CRUMBLE")
                       ))))

(def health
  (desc-classifier :health
                   (fn [^String x]
                     (.contains x "DENTAL")
                     )))

(def shopping
  (desc-classifier :shopping
                   (fn [^String x]
                     (or
                       (.contains x "AMAZON")
                       (.contains x "JOHNLEWIS")
                       (.contains x "PARCELFORCE")
                       (.contains x "W H SMITH")
                       (.startsWith x "BENTALLS")
                       (.startsWith x "FARRAGO")
                       (.startsWith x "FAT FACE")
                       (.startsWith x "HOTEL CHOCOLAT")
                       (.startsWith x "JOHN LEWIS")
                       ))))

(def exercise
  (desc-classifier :exercise
                   (fn [^String x]
                     (or
                       (.contains x "SWEATBANDCO")
                       (.contains x "LES MILLS")
                       (.contains x "WIGGLE")
                       ))))

(def technology
  (desc-classifier :technology
                   (fn [^String x]
                     (or
                       (.contains x "DIGITALRIVE")
                       (.contains x "HPINCUKLIMI")
                       (.contains x "MICROSOFT")
                       (.startsWith x "APPLE")
                       (.startsWith x "ITUNES.COM")
                       (.startsWith x "PLUME LABS")
                       ))))

(def book
  (desc-classifier :book
                   (fn [^String x]
                     (or
                       (.contains x "KINDLE")
                       (.startsWith x "WATERSTONES")
                       ))
                   ))

(def household
  (desc-classifier :household
                   (fn [^String x]
                     (or
                       (.contains x "AVIVA INSURANCE")
                       (.startsWith x "LAKELAND")
                       (.startsWith x "ZARA HOME")
                       ))
                   ))

(def motors
  (desc-classifier :motors
                   (fn [^String x]
                     (or
                       (.contains x "MAYPOLEMOTO")
                       (.startsWith x "DIRECT LINE")
                       ))))

(def donation
  (desc-classifier :donation
                   (fn [^String x]
                     (or
                       (.contains x "WIKIPEDIA")
                       (.startsWith x "CROWDFUNDER")
                       (.startsWith x "CANCER RESEARCH")
                       (.startsWith x "JUSTGIVING")
                       (.startsWith x "WATERSTONES")
                       ))))

(def travelling
  (partial classifier :travelling
           (fn [data] (let [^String desc (:description data)
                            ^String misc (:misc data)]
                        (or
                          (.contains desc "PARKING")
                          (.contains desc "SAMSONITE")
                          (.contains desc "SNAPPYSNAPS")
                          (.contains misc "COMM.FEE")
                          (.startsWith desc "BA HIGH LIFE")
                          (.startsWith desc "UBER")
                          )))))

(def commuting 
  (desc-classifier :commuting
                   (fn [^String x]
                     (.contains x "TFL"))))

(def payment (partial classifier :payment #(-> % :amount (< 0))))

(def default (partial classifier :unknown #(-> % :tag nil?)))

(defn full-classifier
  "The classifier to run the data through."
  [data]
  (-> data

      beauty
      book
      cafe
      commuting
      donation
      eating-out
      exercise
      groceries
      health
      household
      kids
      motors
      payment
      shopping
      technology
      travelling

      default
      ))
