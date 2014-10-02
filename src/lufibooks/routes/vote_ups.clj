(ns lufibooks.routes.vote-ups
  (:require [compojure.core :refer :all]
            [lufibooks.routes.utils :refer :all]
            [clojure.data.json :as json]
            [cemerick.friend :as friend]
            [lufibooks.models.proposals :as model])
  (:use [ring.middleware.json :only [wrap-json-response wrap-json-body]]
     [ring.util.response :only [response]]))

(defn vote-up [request-body]
  (let [json-as-map (json/read-str (slurp request-body) :key-fn clojure.core/keyword)
        old-entry (model/get-by-key (:proposal-key json-as-map))]
    (response (model/update old-entry {:vote (inc (:vote old-entry))}))))

(defroutes vote-ups-routes
  (context "/vote-ups"
           request
           (routes
            (POST "/" {request-body :body} (vote-up request-body)))))
