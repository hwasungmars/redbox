(defproject redbox "0.1.0"
  :description "Classify bank statements for analysis."
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/data.csv "0.1.4"]
                 [org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot redbox.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
