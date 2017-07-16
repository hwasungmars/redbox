(ns redbox.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            )
  (:gen-class))

(defn -main
  "Read in bank statement CSV and classify them."
  [& args]
  (with-open [reader (io/reader (first args))]
    (doall
      (println (csv/read-csv reader))
      )
    ))
