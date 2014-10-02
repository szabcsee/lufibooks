(ns lufibooks.models.descriptions
  (:require
    [lufibooks.models.utils :as utils])
  (:use [hyperion.api]
        [hyperion.types]))

(defentity :descriptions
  ; from client
  [isbn]
  [short-desc]
  [title]
  [author]
  [image-width :type java.lang.Integer]
  [image-height :type java.lang.Integer]
  [image-url]


  ; calculated

  ; auto
  [created-at]
  [updated-at])



(def get-by-key
  (partial utils/get-by-key :descriptions))

(defn persist [data]
    (save (descriptions) data))


(defn get-all [] (find-by-kind :descriptions))
