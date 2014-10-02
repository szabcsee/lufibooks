(ns lufibooks.models.books
  (:require
    [lufibooks.models.utils :as utils]
    [lufibooks.models.proposals :as proposals]
    [lufibooks.models.descriptions :as desc])
  (:use [hyperion.api]
        [hyperion.types]
        ))

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

(def client-can-edit [:allInStock])


(def get-by-key
  (partial utils/get-by-key :books))

(defn del-entry [old-entry]
   (let [entry-key (:key old-entry)]
     (delete-by-key entry-key)
     old-entry))

(defn persist [json-as-map]
  (let [isbn (:isbn json-as-map)
        proposal (proposals/get-by-isbn isbn)
        new-entry
    (assoc (save (books)
          (merge (select-keys json-as-map client-can-edit) (select-keys proposal [:descKey])))
      :description (desc/get-by-key (:descKey proposal)))]
    (proposals/del-entry proposal)
    new-entry))

(defn update [old-entry json-as-map]
    (save (books)
          (merge old-entry
                 (select-keys json-as-map client-can-edit))))


(defn get-all [params] (vals (desc/merge-result
                        (apply find-by-kind
                              (cons :books (utils/query-params-to-filter params)))
                         (desc/get-all))))
