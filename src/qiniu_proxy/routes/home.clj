(ns qiniu-proxy.routes.home
  (:import (java.util Date))
  (:use compojure.core
        lamina.core
        aleph.http
        [ring.middleware resource file-info])
  (:require [compojure.route :as route]
            [clojure.data.json :as json]
            [clojure.tools.logging :as logging]
            [hiccup.core :as hc]
            [qiniu-proxy.utils :as utils]))

(defn- input-field
  ([props name]
   (input-field props name ""))
  ([props name value]
   [:input (into props {:name name :value value})]))

(defn- input-field-row
  ([display-text props name]
   (input-field-row display-text props name ""))
  ([display-text props name value]
   [:div
    [:span display-text]
    (input-field props name value)
    [:br]]))

(defn home-page
  []
  (hc/html
    [:div {:class "container"}
     [:form {:action  "/api/v1/upload"
             :method  "POST"
             :enctype "multipart/form-data"}
      [:div
       (input-field-row "Qiniu App Key" {} "qiniu_key" "")
       (input-field-row "Qiniu App Secret" {} "qiniu_secret" "")
       (input-field-row "Qiniu Bucket" {} "qiniu_bucket" "")
       (input-field-row "Content Type" {} "content_type" "image/jpeg")
       (input-field-row "File" {:type "file"} "file")
       (input-field-row "Upload Type" {} "upload_type" "local_path")
       (input-field-row "File Path" {} "filepath" "D:\\config.xml")
       (input-field-row "Return Type" {} "return_type" "web_hook")
       (input-field-row "WebHook URL" {} "web_hook_url" "http://localhost:3000/test")
       [:input {:type "submit" :value "Submit"}]]]]))

(defn test-web-hook
  []
  (hc/html
    [:div "Got web hook callback"]))

(defroutes home-routes
           (GET "/" [] (home-page))
           (GET "/test" [] (test-web-hook)))