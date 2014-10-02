(ns lufibooks.models.books
  (:require
    [lufibooks.models.utils :as utils])
  (:use [hyperion.api]
        [hyperion.types]))

(defentity :books
  ; from client
  [numBorrowed :type java.lang.Integer :default 0]
  [numAvail :type java.lang.Integer :default 0]
  [allInStock :type java.lang.Integer :default 0]
  [descKey :type (foreign-key :descriptions)]
  ; calculated

  ; auto
  [created-at]
  [updated-at])

(def client-can-edit [:isbn])


(def get-by-key
  (partial utils/get-by-key :books))

(defn del-entry [old-entry]
   (let [entry-key (:key old-entry)]
     (delete-by-key entry-key)
     old-entry))

(defn persist [json-as-map]
    (save (books)
          (select-keys json-as-map client-can-edit)))

(defn update [old-entry json-as-map]
    (save (books)
          (merge old-entry
                 (select-keys json-as-map client-can-edit))))

(defn get-all [params] (apply find-by-kind
                              (cons :books (utils/query-params-to-filter params))))

