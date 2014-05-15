(ns qiniu-proxy.routes.api
  (:import (java.util Date)
           (java.io FileInputStream))
  (:use compojure.core
        lamina.core
        aleph.http
        [ring.middleware resource file-info])
  (:require [compojure.route :as route]
            [clojure.data.json :as json]
            [hiccup.core :as hc]
            [clj-http.client :as client]
            [ring.middleware [multipart-params :as mp]]
            [ring.util [response :as response]]
            [clojure.tools.logging :as logging]
            [qiniu-proxy.utils :as utils]))

(defn- do-upload
  ([params]
   (do-upload params (utils/rand-str 40)))
  ([{qiniu-key    :qiniu_key qiniu-secret :qiniu_secret qiniu-bucket :qiniu_bucket
     content-type :content_type upload-type :upload_type {file-size :size file :tempfile} :file filepath :filepath
     return-type  :return_type webhook-url :web_hook_url :as params} file-key]
   (case upload-type
     "direct" (utils/upload-binary-stream
                qiniu-key qiniu-secret qiniu-bucket (FileInputStream. file) content-type file-key)
     "local_path" (utils/upload-local-file
                    qiniu-key qiniu-secret qiniu-bucket filepath content-type file-key)
     "error")))

(defn- do-upload-and-callbak
  [{qiniu-key    :qiniu_key qiniu-secret :qiniu_secret qiniu-bucket :qiniu_bucket
    content-type :content_type upload-type :upload_type {file-size :size file :tempfile} :file filepath :filepath
    return-type  :return_type webhook-url :web_hook_url :as params} file-key]
  (let [{file-size :file-size :as _} (do-upload params file-key)]
    (client/get (str webhook-url "?success=true&file-size=" file-size "&store_key=" file-key))
    (logging/info "hi, agent done")))

(defn upload-handler
  "接收上传文件请求并处理"
  [{qiniu-key    :qiniu_key qiniu-secret :qiniu_secret qiniu-bucket :qiniu_bucket
    content-type :content_type upload-type :upload_type {file-size :size file :tempfile} :file filepath :filepath
    return-type  :return_type webhook-url :web_hook_url :as params}]
  (do
    (println params upload-type file return-type)
    (case return-type
      "direct" (json/write-str {:success true :file-size file-size :store_key (:store-key (do-upload params))})
      "web_hook" (let [file-key (utils/rand-str 40)]
                   (send (agent params) #(do-upload-and-callbak % file-key))
                   (json/write-str {:success true :store_key file-key}))
      "error")
    ))

(defroutes api-routes
           (mp/wrap-multipart-params
             (POST "/api/v1/upload"
                   {params :params}
                   (upload-handler params))))