(ns lufibooks.models.amazon-utils
  (:use [clj-amazon-fixed.core]
        [clj-amazon-fixed.product-advertising]))

(def ACCESS-KEY "AKIAI3GHDT7LYTTDJCMQ" )

(def SECRET-KEY "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" )

(def ASSOCIATE-ID "678832359350")

; (with-signer (ACCESS-KEY, SECRET-KEY)
;   (item-search :search-index "Books",
;                :keywords "Apple",
;                :associate-tag ASSOCIATE-ID,
;                :condition "New"
;                ;:response-group "images"
;                ))

;"076243631X"

(defn get-book-data-by-isbn [isbn]
  (with-signer (ACCESS-KEY, SECRET-KEY) (item-lookup :id-type "ISBN",
                                                     :associate-tag ASSOCIATE-ID
                                                     :search-index "Books",
                                                     :item-id isbn
                                                     :response-group "Images,EditorialReview,ItemAttributes"
                                                     )))


