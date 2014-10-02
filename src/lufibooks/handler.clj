(ns lufibooks.handler
  (:require [compojure.core :refer [defroutes routes ANY GET]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            ;[cemerick.friend :as friend]
            ;(cemerick.friend [workflows :as workflows]
            ;                 [credentials :as creds])
            [lufibooks.routes.books :refer [books-routes]]
            [lufibooks.models.proposals :as proposals-model]
            [lufibooks.models.books :as books-model]
            [lufibooks.routes.proposals :refer [proposals-routes]]
            [lufibooks.routes.vote-ups :refer [vote-ups-routes]]
            [compojure.handler :as handler]
            [compojure.route :as route])

  (:use [ring.middleware.json :only [wrap-json-response]]
        [ring.util.response :only [response redirect resource-response]]
        [hyperion.api]))

(defn init []
  (do
    (set-ds! (new-datastore :implementation :memory))
    (doseq [isbn ["1934356867" "0321721330" "0321944275" "1934356867" "1937785645" "1449366171"]]
       (proposals-model/persist {:isbn isbn}))
    (doseq [isbn ["0321721330" "1449366171"]]
      (books-model/persist {:isbn isbn :allInStock 2}))
    (println "lufibooks is starting")))

(defn destroy []
  (println "lufibooks is shutting down"))

(defroutes app-routes
  (GET "/" [] (response {:h "hello!"}))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-json-response
   (wrap-base-url
    (handler/site
      (routes
         books-routes
         proposals-routes
         vote-ups-routes
         app-routes)))))

