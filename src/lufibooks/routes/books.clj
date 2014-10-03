(ns lufibooks.routes.books
  (:require [compojure.core :refer :all]
            [lufibooks.routes.utils :refer :all]
            [clojure.data.json :as json]
            [cemerick.friend :as friend]
            [lufibooks.models.books :as model])
  (:use [ring.middleware.json :only [wrap-json-response wrap-json-body]]
     [ring.util.response :only [response]]))

(defn type-safe-search [params]
  (if (contains? params :numAvail) (merge params {:numAvail (Integer/parseInt (:numAvail params))})
    params))

(defn- get-all [params]
    (case (:state params)
      "borrowed" (response  {:book (map #(dissoc % :kind) (model/get-borrowed (type-safe-search params)))})
      "available" (response  {:book (map #(dissoc % :kind) (model/get-available (type-safe-search params)))})
        (response  {:book (map #(dissoc % :kind) (model/get-all (type-safe-search params)))})))

(defn- del-entry [entry-key]
  (let [old-entry (model/get-by-key entry-key)]
    (response  {:book (dissoc (model/del-entry old-entry) :kind)})))

(defn- get-entry [entry-key]
  (let [foundEntry (model/get-by-key entry-key)]
    (response  {:book (dissoc foundEntry :kind)})))

(defn- post [request-body]
  (let [json-as-map (json/read-str (slurp request-body) :key-fn clojure.core/keyword)]
    (response  {:book
                (dissoc
                 (model/persist (json-as-map :book)) :kind)})))

(defn- put [entry-key request-body]
  (let [old-entry (model/get-by-key entry-key)
        json-as-map (json/read-str
                     (slurp request-body)
                     :key-fn
                     clojure.core/keyword)]
      (response {:book  (dissoc (model/update old-entry (assoc (json-as-map :book) :key entry-key)) :kind)})))


(defroutes books-routes
  (context "/books"
           request
           (routes
            (DELETE "/:entry-key" [entry-key] (del-entry entry-key))
            (GET "/" {params :params} (get-all params))
            (POST "/" {request-body :body} (post request-body))
            (PUT "/:entry-key" {params :params request-body :body} (put (params :entry-key) request-body))
            (GET "/:entry-key" [entry-key] (get-entry entry-key)))))

