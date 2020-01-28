(ns redbox.core
  (:require
    [redbox.categorisers :as categorisers]
    [redbox.classifiers :as classifiers]

    [org.satta.glob :as glob]

    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.set :as set]
    [clojure.string :as string]
    [clojure.tools.logging :as logging]
    )
  (:import [java.io File])
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

(defn collect-all-keys
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

(defn csv-file->classified-maps
  "Read in a CSV file and return the classified data."
  [csv]
  (->> csv
       io/reader
       load-csv
       (map classifiers/full-classifier)))

(defn classified-maps->csv-data
  "Convert the classified maps to CSV data strucuture with header and then followed by data."
  [classified-maps]
  (let [header (-> classified-maps collect-all-keys create-header)
        extract-data (extract-data-by-header header)
        formatted (map extract-data classified-maps)]
    (concat [header] formatted)))

(defn construct-output-path
  "Given an input file, construct the output file."
  [^File input-file]
  (let [file-name (.getName input-file)
        ^String parent-dir (.getParent input-file)]
    (File. parent-dir (str "classified-" file-name))))

(defn reader->classified-csv
  "Given a CSV file reader, load it, classify it and return data in CSV format."
  [reader]
  (let [classified (->> reader
                        load-csv
                        (map classifiers/full-classifier)
                        (map categorisers/categorise)
                        )
        header (-> classified collect-all-keys create-header)
        extract-data (extract-data-by-header header)
        formatted (map extract-data classified)]
    (concat [header] formatted)))

(defn process-file
  "Process the file and write out results."
  [^String file-path]
  (let [^File input-file (File. file-path)
        ^File output-file (construct-output-path input-file)]
    (logging/info "Classifying" (.getAbsolutePath input-file) "->" (.getAbsolutePath output-file))
    (with-open [reader (io/reader input-file)]
      (with-open [writer (io/writer output-file)]
        (csv/write-csv writer
                       (reader->classified-csv reader))))))

(defn -main
  "Read in bank statement CSV and classify them."
  [& args]
  (->> args
       (map process-file)
       doall))
