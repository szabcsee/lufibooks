(ns lufibooks.models.proposals
  (:require
    [lufibooks.models.utils :as utils]
    [lufibooks.models.descriptions :as desc])
  (:use [hyperion.api]
        [hyperion.types]
        [lufibooks.models.amazon-utils]))


(defentity :proposals
  ; from client
  [desc-key :type (foreign-key :descriptions)]
  [vote :type java.lang.Integer :default 0]
  ; calculated

  ; auto
  [created-at]
  [updated-at])

(def client-can-edit [:isbn])

(defn- to-desc [response]
   {:short-desc (:review-content response)
    :title (-> response :item-atributes :title)
    :author (-> response :item-atributes :author)
    :manufacturer (-> response :item-atributes :manufacturer)
    :image-width (-> response :large-image :width)
    :image-height (-> response :large-image :height)
    :image-url (-> response :large-image :url)
    })


(defn get-by-key [entry-key]
  (let [entry (utils/get-by-key :proposals entry-key)]
    (assoc entry :description (desc/get-by-key (:desc-key entry)))))

(defn del-entry [old-entry]
   (let [entry-key (:key old-entry)]
     (delete-by-key entry-key)
     old-entry))

(defn persist [json-as-map]
  (let [isbn (:isbn json-as-map)
        resp (get-book-data-by-isbn isbn)
        desc (to-desc (-> resp :items first))
        saved-desc (desc/persist (assoc desc :isbn isbn))]
    (assoc (save (proposals) {:desc-key (:key saved-desc)}) :description saved-desc)))


(defn update [old-entry json-as-map]
    (save (proposals)
          (merge old-entry
                 (select-keys json-as-map client-can-edit))))


(defn- merge-result [proposals-list descriptions-list]
  (merge-with (fn [a b] (assoc a :description b))
              (reduce #(into %1 {(:desc-key %2) %2}) {} proposals-list)
              (reduce #(into %1 {(:key %2) %2}) {} descriptions-list)))


(defn get-all [params] (vals (merge-result
                        (apply find-by-kind
                              (cons :proposals (utils/query-params-to-filter params)))
                         (desc/get-all))))







