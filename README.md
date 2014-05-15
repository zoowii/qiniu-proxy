# qiniu-proxy

接收请求,接收文件上传请求,上传到七牛再返回或者回调(两种方式)

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## TODO

* 安全问题
* 支持XML-RPC
* 支持其他云存储,比如AWS S3, 百度BCS等,或者使用MongoDB等服务
* 日志

## 使用说明

POST    /api/v1/upload  使用multipart/form的方式传入数据

传入文件可以选择直接传入文件内容,也可以选择传入本地文件路径(将来是否可以传入远程url等路径待定)

返回结果的方式可以选择直接返回,也可以选择回调,由请求传入参数指定

请求还需要传入七牛的app-key/app-secret,还有bucket名称,暂时不考虑安全问题,主要在同一个服务器上运行

**传入参数说明**

参数名称 | 参数说明 | 是否必须
--------|---------|--------

* qiniu_key         七牛的app-key                                             Yes
* qiniu_secret       七牛的app-secret                                           Yes
* qiniu_bucket      七牛的Bucket名称                                           Yes
* upload_type       上传方式,直接上传(direct)或者传入本地文件路径(local_path)     Yes
* content_type      文件的mime-type                                            Yes
* filepath          如果upload_type=local_path时需要,表示本地文件路径            No
* file              文件正文内容,如果upload_type=direct的话需要                 No
* return_type       返回类型,直接返回(direct)或者http回调(web_hook)的方式          Yes
* web_hook_url      如果return_type=web_hook时需要,表示回调url                   No

**返回参数说明**

如果是直接返回,返回JSON,如果是回调,通过query string传入,返回数据一样,只是格式不同

* success       boolean 是否成功
* file_size     int     文件大小
* store_key     string  一个唯一的ID字符串,存储到七牛中的文件名(key)

## License

Copyright © 2014 [zoowii](http://github.com/zoowii)
