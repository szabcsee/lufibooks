(ns lufibooks.routes.utils
  (:require [ring.util.response :refer :all]
            [cemerick.friend :as friend])
  (:use [ring.middleware.json :only [wrap-json-response wrap-json-body]]))

(def not-auth-response
  (status (response {:error "Not authorized!"}) 403))

(defn is-owned [entry]
  (= (:owner entry) (:username (friend/current-authentication))))
