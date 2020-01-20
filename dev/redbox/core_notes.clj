(ns redbox.core-notes
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.pprint :as pprint]
    [clojure.string :as string]
    [redbox.categorisers :as categorisers]
    [redbox.classifiers :as classifiers]
    [redbox.core :refer :all]
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

(classifiers/shopping (first (load-csv (io/reader (io/resource "ofx.csv")))))

({:a 1 :b 2} :a)

(def header (create-header #{:description :tag :something-else}))

(extract-data-by-header header {:description "foo" :tag "bar" :something-else "baz"})

(type {:a 1})

(classifiers/full-classifier {:description "BOOTS Optician"})

(def classified (map classifiers/full-classifier (take 10 (load-csv (io/reader (io/resource "ofx.csv"))))))

(def header (-> classified collect-all-keys create-header))

(csv-file->classified-maps (io/resource "ofx.csv"))

(def classified-maps
  (->> "ofx.csv"
       io/resource
       csv-file->classified-maps
       ))

(reduce (fn [acc x]
          (let [tag (:tag x)
                amount (:amount x)
                ]
            (update acc tag #(+ % amount))
            )
          )
        classified-maps
        )

(def grouped-value [{:amount -21.38} {:amount 100}])

(reduce #(+ %1 (:amount %2)) 0 grouped-value)

(partition-by last (string/split "/foo/bar" #"/"))

(type  (.getName (File. "/foo/bar.csv")))

(file->file-name-string (File. "/foo/bar.csv"))

(-> "/Foo/bar.csv" File. .getName (string/split #"\."))

(categorisers/categorise {:tag :commuting})
