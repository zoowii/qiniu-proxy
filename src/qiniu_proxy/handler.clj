(ns qiniu-proxy.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [noir.util.middleware :refer [app-handler]]
            [ring.middleware.reload :as reload]
            [qiniu-proxy.routes.api :refer [api-routes]]
            [qiniu-proxy.routes.home :refer [home-routes]]))

(defroutes app-routes
           (GET "/" [] "Hello World")
           (route/resources "/")
           (route/not-found "Not Found"))

(def app
  (-> (app-handler [api-routes home-routes app-routes])
      handler/site
      reload/wrap-reload))

