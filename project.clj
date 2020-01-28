(defproject redbox "0.1.0"
  :description "Classify bank statements for analysis."
  :url "http://example.com/FIXME"
  :dependencies [
                 [clj-glob "1.0.0"]

                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/data.csv "0.1.4"]
                 [org.clojure/test.check "0.10.0"]
                 [org.clojure/tools.logging "0.5.0"]
                 ]
  :main ^:skip-aot redbox.core
  :target-path "target/%s"
  :profiles {:dev {:source-paths ["dev"]}
             :uberjar {:aot :all}}
  :plugins [[jonase/eastwood "0.3.7"]]
  :eastwood {:exclude-namespaces [redbox.core-notebook]
             :add-linters [:unused-fn-args :unused-locals :unused-namespaces :unused-private-vars]})
