(ns redbox.core
  (:require [redbox.classifiers :as classifiers]

            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as string]
            )
  (:import clojure.lang.PersistentVector)
  (:gen-class))

(set! *warn-on-reflection* true)

(def csv-fields [:date :referece :amount :description :misc])

(defn label-data
  "Given Amex csv file, label the data. In effect, this is the schema we expect."
  [data]
  (zipmap csv-fields data))

(defn parse-data
  "Given Amex labelled data, parse it to appropriate format."
  [data]
  (update-in data [:amount] #(-> %
                                 string/trim
                                 Float/parseFloat)))

(defn load-csv
  "Load CSV file as a map."
  [reader]
  (let [csv-data (csv/read-csv reader)]
    (map #(-> %
              label-data
              parse-data)
         csv-data)))

(defn classifier
  "The classifier to run the data through."
  [data]
  (-> data
      classifiers/beauty
      classifiers/cafe
      classifiers/eating-out
      classifiers/groceries
      classifiers/kids
      classifiers/shopping
      ))

(defn all-keys
  "Collect all the keys from a collection of hash-map."
  [hash-map]
  (let [keys (map #(-> %
                       keys
                       set)
                  hash-map)]
    (reduce set/union #{} keys)))

(defn create-header
  "Given all the keys, constructor the CSV header."
  [keys]
  (let [new-keys (set/difference keys csv-fields)]
    (concat csv-fields new-keys)))

(defn extract-data-by-header
  "Given heaer, extract data corresponding to the order."
  ([header]
   (partial extract-data-by-header header))
  ([header data]
   (map data header)))

(defn -main
  "Read in bank statement CSV and classify them."
  [& args]
  (with-open [reader (io/reader (first args))]
    (with-open [writer (io/writer (second args))]
      (let [classified (->> reader
                            load-csv
                            (map classifier))
            header (-> classified all-keys create-header)
            extract-data (extract-data-by-header header)
            formatted (map extract-data classified)]
        (csv/write-csv writer
                       (concat [header] formatted))))))
