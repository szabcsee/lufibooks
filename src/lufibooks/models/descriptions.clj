(ns lufibooks.models.descriptions
  (:require
    [lufibooks.models.utils :as utils])
  (:use [hyperion.api]
        [hyperion.types]))

(defentity :descriptions
  ; from client
  [shortDesc]
  [title]
  [author]
  [imageWidth :type java.lang.Integer]
  [imageHeight :type java.lang.Integer]
  [imageUrl]

  ; calculated

  ; auto
  [created-at]
  [updated-at])



(def get-by-key
  (partial utils/get-by-key :descriptions))

(defn persist [data]
    (save (descriptions) data))

(defn get-all [] (find-by-kind :descriptions))


(defn merge-result [parent-entries descriptions-list]
  (let [parent-map (reduce #(into %1 {(:descKey %2) %2}) {} parent-entries)
        child-map (reduce #(into %1 {(:key %2) %2}) {} descriptions-list)]
  (merge-with (fn [a b] (assoc a :description b))
              parent-map
              (select-keys child-map (keys parent-map) ))))

