(ns qiniu-proxy.utils
  (:import (com.qiniu.api.config Config)
           (com.qiniu.api.auth.digest Mac)
           (com.qiniu.api.rs PutPolicy)
           (com.qiniu.api.io PutExtra IoApi)
           (java.io InputStream Reader File)
           (java.util Properties))
  (:require [clojure.tools.logging :as logging]))

(defn rand-str
  [n]
  (clojure.string/join
    (repeatedly n
                #(rand-nth "abcdefghijklmnopqrstuvwxyz0123456789"))))

(defn load-props
  [file-name]
  (with-open [^Reader reader (clojure.java.io/reader file-name)]
    (let [props (Properties.)]
      (.load props reader)
      (into {} (for [[k v] props] [(keyword k) (read-string v)])))))

(defn- get-uptoken
  [key secret bucket]
  (let [_ (set! Config/ACCESS_KEY key)
        _ (set! Config/SECRET_KEY secret)
        mac (Mac. key secret)
        put-policy (PutPolicy. bucket)
        uptoken (.token put-policy mac)]
    uptoken))

(defn upload-binary-stream
  ([key secret bucket ^InputStream stream content-type]
   (upload-binary-stream key secret bucket ^InputStream stream content-type (rand-str 40)))
  ([key secret bucket ^InputStream stream content-type file-key]
   (let [file-size (.available stream)
         uptoken (get-uptoken key secret bucket)
         extra (PutExtra.)
         _ (set! (.mimeType extra) content-type)
         _ (IoApi/Put uptoken file-key stream extra)
         _ (.close stream)]
     (logging/info (str "upload done " file-key))
     {:store-key file-key
      :file-size file-size})))

(defn upload-local-file
  ([key secret bucket filepath content-type]
   (upload-local-file key secret bucket filepath content-type (rand-str 40)))
  ([key secret bucket filepath content-type file-key]
   (let [uptoken (get-uptoken key secret bucket)
         extra (PutExtra.)
         ;_ (set! (.mimeType extra) content-type)
         _ (IoApi/putFile uptoken file-key filepath extra)]
     (logging/info (str "upload done " file-key))
     {:store-key file-key
      :file-size (.length (File. filepath))})))
