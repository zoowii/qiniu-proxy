(defproject qiniu-proxy "0.1.0-SNAPSHOT"
  :description "接收请求,接收文件上传请求,上传到七牛再返回或者回调(两种方式)"
  :url "http://github.com/zoowii"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2202"]
                 [lib-noir "0.8.2"]
                 [org.clojure/tools.logging "0.2.6"]
                 [clj-http "0.9.1"]
                 [com.qiniu/sdk "6.1.0"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [selmer "0.6.6"]
                 [com.taoensso/timbre "3.1.6"]
                 [com.taoensso/tower "2.0.2"]
                 [jayq "2.5.0"]
                 [markdown-clj "0.9.43"]
                 [lamina "0.5.2"]
                 [aleph "0.3.2"]
                 [http-kit "2.1.18"]
                 [org.clojure/data.json "0.2.4"]
                 [org.clojure/tools.logging "0.2.6"]
                 [com.novemberain/monger "1.7.0"]
                 [environ "0.4.0"]
                 [compojure "1.1.6"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler qiniu-proxy.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})
