(ns redbox.core-notebook
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.pprint :as pprint]
    [redbox.core :refer :all]
    [redbox.classifiers :as classifiers]
    )
  (:import [java.io File])
  )

(set! *warn-on-reflection* true)

(pprint/pprint (load-csv (io/reader (io/resource "ofx.csv"))))

(pprint/pprint (csv/read-csv (io/reader (io/resource "ofx.csv"))))

(Integer/parseInt "123")

(parse-data {:amount " 123"})

(-main (.getAbsolutePath (File. (.toURI (io/resource "ofx.csv")))) "test.csv")

(assoc {:a 1 :b 2} :a 3)

(classifier (first (load-csv (io/reader (io/resource "ofx.csv")))))

(def results (map classifier (load-csv (io/reader (io/resource "ofx.csv")))))

(pprint/pprint results)

(all-keys [ {:a 1 :b 2} {:c 3} {:d 4 :a 2}])

({:a 1 :b 2} :a)

(def header (create-header #{:description :tag :something-else}))

(extract-data-by-header header {:description "foo" :tag "bar" :something-else "baz"})
