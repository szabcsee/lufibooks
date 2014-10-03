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


(defn get-by-key [entry-key]
  (let [entry (utils/get-by-key :books entry-key)]
    (desc/add-desc entry)))

(defn del-entry [old-entry]
   (let [entry-key (:key old-entry)]
     (delete-by-key entry-key)
     old-entry))

(defn borrow [entry num-borrowed]
  (let [all-in-stock (:allInStock entry)]
    (assoc entry :numBorrowed num-borrowed :numAvail (- all-in-stock num-borrowed))))

(defn persist [json-as-map]
  (let [proposals-key (:proposalsKey json-as-map)
        proposal (proposals/get-by-key proposals-key)
        all-in-stock (:allInStock json-as-map)
        new-entry (desc/add-desc (save (books)
                                       (merge {:allInStock all-in-stock
                                               :descKey (:descKey proposal)
                                               :numAvail all-in-stock
                                               :numBorrowed 0})))]
    (proposals/del-entry proposal)
    new-entry))

(defn update [old-entry json-as-map]
  (let [num-borrowed (:numBorrowed json-as-map)]
    (desc/add-desc (save (books)
          (merge old-entry
                 (borrow old-entry num-borrowed))))))


(defn get-all [params] (vals (desc/merge-result
                        (apply find-by-kind
                              (cons :books (utils/query-params-to-filter params)))
                         (desc/get-all))))
