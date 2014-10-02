(ns lufibooks.handler
  (:require [compojure.core :refer [defroutes routes ANY GET]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            ;[cemerick.friend :as friend]
            ;(cemerick.friend [workflows :as workflows]
            ;                 [credentials :as creds])
            [lufibooks.routes.books :refer [books-routes]]
            [lufibooks.routes.proposals :refer [proposals-routes]]
            [compojure.handler :as handler]
            [compojure.route :as route])

  (:use [ring.middleware.json :only [wrap-json-response]]
        [ring.util.response :only [response redirect resource-response]]
        [hyperion.api]))

(defn init []
  (do
    (set-ds! (new-datastore :implementation :memory))
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
         app-routes)))))

