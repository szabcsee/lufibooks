(ns lufibooks.models.utils
  (:use [hyperion.api]))

(defn get-by-key [kind ekey] (when-let [entry (find-by-key ekey)] (if (= kind (keyword (entry :kind))) entry)))

(defn query-params-to-filter [query-params]
    (let [filter-list (map (fn [[k value]] [:= k value]) query-params)]
     (if (not (empty? filter-list)) (cons :filters [filter-list]))))
