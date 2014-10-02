; to delete and never store in VCS


(use 'lufibooks.repl)

(start-server "8090")

(stop-server)


(use 'lufibooks.models.amazon-utils)

(get-book-data-by-isbn "076243631X")

(use 'clj-amazon-fixed.core)

(use 'clj-amazon-fixed.product-advertising)


(def ACCESS-KEY "AKIAI3GHDT7LYTTDJCMQ" )

(def SECRET-KEY "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" )

(def ASSOCIATE-ID "678832359350")



 (def res
(with-signer (ACCESS-KEY, SECRET-KEY) (item-lookup :id-type "ISBN",
                                                               :associate-tag ASSOCIATE-ID
                                                               :search-index "Books",
                                                               :item-id "076243631X"
                                                               :response-group "Images,EditorialReview,ItemAttributes"
                                                               )))


 res



 (defn to-desc [response]
   {:description (:review-content response)
    :title (-> response :item-atributes :title)
    :image (:large-image response)
    }


   )

 (to-desc
   (-> res :items first)
  )


 (-> re :content first :content)

 (-> ((fn [c] (filter #(= (:tag %) :Content) c)) (-> re :content first :content )) first :content first)
