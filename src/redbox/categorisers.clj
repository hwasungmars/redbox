(ns redbox.categorisers)

(set! *warn-on-reflection* true)

(def tag->category
  {
   :beauty :want
   :book :want
   :cafe :want
   :commuting :need
   :donation :want
   :eating-out :want
   :exercise :need
   :groceries :need
   :health :need
   :household :need
   :kids :need
   :motors :want
   :shopping :want
   :technology :want
   :travelling :want
   })

(defn categorise
  "Given a classified map, categorise it based on tag."
  [{:keys [tag] :as classified-map}]
  (assoc classified-map :category (tag->category tag tag)))

