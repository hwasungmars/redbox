(ns redbox.core
  (:require [redbox.classifiers :as classifiers]

            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.pprint :as pprint])
  (:gen-class))

(set! *warn-on-reflection* true)

(defn load-csv
  "Load CSV file as a map."
  [reader]
    (let [csv-data (csv/read-csv reader)]
    (map zipmap
         (->> (first csv-data)
              (map keyword)
              repeat)
         (rest csv-data))))

(defn concat-if
  "Concat elements without duplicates.  This is order preserving."
  [left right]
  (let [missing (filter #(not (.contains left %)) right)]
    (concat left missing)))

(defn select-values
  "Select the values from the given hash-map.  This is order preserving."
  [data extract-keys]
  (reduce #(conj %1 (data %2)) [] extract-keys))

(defn classifier
  "The classifier to run the data through."
  [data]
  (-> data
      (dissoc (keyword ""))
      classifiers/beauty
      classifiers/cafe
      classifiers/eating-out
      classifiers/groceries
      classifiers/kids
      classifiers/shopping
      ))

(defn collect
  "Given a seq of hash-map collect the data to a structure where the result is the following:

  {:field-names [:a :b] :data ([1 2] [3 4])}
  "
  [maps]
  (reduce (fn [z x]
            (let [new-names (concat-if (:field-names z) (keys x))
                  new-data (conj (:data z) (select-values x new-names))]
              (assoc z :field-names new-names :data new-data)))
          {:field-names []
           :data []}
          maps))

(defn -main
  "Read in bank statement CSV and classify them."
  [& args]
  (with-open [reader (io/reader (first args))]
    (with-open [writer (io/writer (second args))]
      (let [collected (collect (map classifier (load-csv reader)))]
        (csv/write-csv writer
                       (concat (conj [] (:field-names collected)) (:data collected)))))))
