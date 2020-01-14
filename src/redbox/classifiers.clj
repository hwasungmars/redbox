(ns redbox.classifiers)

(set! *warn-on-reflection* true)

(defn desc-classifier
  "Classify based on description."
  ([is-true tag]
   (partial desc-classifier is-true tag))
  ([is-true tag data]
   (try
     (let [desc (:description data) ]
       (if (is-true desc)
         (assoc data :tag tag)
         data))
     (catch Exception e (RuntimeException. "Failed to classify data" e)))))

(def cafe
  "Identify cafe and tag them."
  (let [is-cafe (fn [^String desc] (or
                                     (.startsWith desc "COSTA")
                                     (.startsWith desc "SOPRATTUTTO")
                                     (.contains desc "COFFEE")
                                     (.contains desc "ESPRESSO")
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
                                           ))]
    (desc-classifier is-eating-out "eating-out")))

(def groceries
  "Identify groceries and tag them."
  (let [is-groceries (fn [^String desc] (or
                                          (.startsWith desc "SAINSBURY'S")
                                          (.startsWith desc "MARKS & SPENCER")
                                          (.startsWith desc "TESCO")
                                          (.startsWith desc "WAITROSE")
                                          (.startsWith desc "H MART")
                                          ))]
    (desc-classifier is-groceries "groceries")))

(def kids
  "Identify kids item and tag them."
  (let [kids? (fn [^String desc] (.contains desc "TOY"))]
    (desc-classifier kids? "kids")))

(def shopping
  "Identify shopping and tag them."
  (let [is-shopping (fn [^String desc] (or
                                         (.startsWith desc "AMAZON")
                                         (.startsWith desc "BENTALLS")
                                         (.startsWith desc "JOHN LEWIS")
                                         ))]
    (desc-classifier is-shopping "shopping")))
