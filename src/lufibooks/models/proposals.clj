(ns lufibooks.models.proposals
  (:require
    [lufibooks.models.utils :as utils]
    [lufibooks.models.descriptions :as desc])
  (:use [hyperion.api]
        [hyperion.types]
        [lufibooks.models.amazon-utils]))


(defentity :proposals
  ; from client
  [vote :type java.lang.Integer :default 0]
  ; calculated
  [descKey :type (foreign-key :descriptions)]
  ; auto
  [created-at]
  [updated-at])

(def client-can-edit [:vote])

(defn concat-if-needed [c]
  (if (coll? c) (clojure.string/join ", " c) c))

(defn- to-desc [response]
   {:short-desc (:review-content response)
    :title (-> response :item-atributes :title)
    :author (concat-if-needed (-> response :item-atributes :author))
    :manufacturer (-> response :item-atributes :manufacturer)
    :imageWidth (-> response :large-image :width)
    :imageHeight (-> response :large-image :height)
    :imageUrl (-> response :large-image :url)
    })


(defn get-by-key [entry-key]
  (let [entry (utils/get-by-key :proposals entry-key)]
    (assoc entry :description (desc/get-by-key (:descKey entry)))))

(defn del-entry [old-entry]
   (let [entry-key (:key old-entry)]
     (delete-by-key entry-key)
     old-entry))

(defn persist [json-as-map]
  (let [isbn (:isbn json-as-map)
        resp (get-book-data-by-isbn isbn)
        desc (to-desc (-> resp :items first))
        saved-desc (desc/persist (assoc desc :isbn isbn))]
    (assoc (save (proposals) {:descKey (:key saved-desc)}) :description saved-desc)))


(defn update [old-entry json-as-map]
    (save (proposals)
          (merge old-entry
                 (select-keys json-as-map client-can-edit))))

(defn get-all [params] (vals (desc/merge-result
                        (apply find-by-kind
                              (cons :proposals (utils/query-params-to-filter params)))
                         (desc/get-all))))







