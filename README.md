# nexsuj


## 参考
* https://blog.csdn.net/rstevens/category_13738.html
* https://www.cnblogs.com/yangzhili/p/10092675.html
* https://blog.csdn.net/yy254117440/article/details/51113358
* https://www.cnblogs.com/ningci/p/14798537.html
* https://github.com/atomashpolskiy/bt
* https://github.com/BrightStarry/zx-bt

* https://sunyunqiang.com/blog/bittorrent_protocol/

https://blog.csdn.net/u010087886/article/details/46829793


## 接口
### announce接口
GET /announce
请求参数：

info_hash: Info键值的20字节SHA杂凑值.




BEP：10 Extension Protocol
关于扩展的扩展协议

- [x]  bep3 协议  
  主要讲述torrent文件的结构和tracker http的请求和响应
英文地址 http://www.bittorrent.org/beps/bep_0003.html
  
- [x] BEP：23
  tracker 返回peers采用紧凑模式, 每留个字节表示一个peer

- [x] bep27
  http://bittorrent.org/beps/bep_0027.html
  pt协议, 官方协议中的介绍很简单, 并没有涉及到上传流量等计算和控制

- [ ]  BEP：15
  tracker 服务器采用UDP协议,减少压力

- [ ]  BEP:	41
  客户端和tracker服务器直接通信的时候采用的UDP协议的扩展

- [ ]  BEP-20: Peer ID Conventions,
  peerId的生成协议, peerId中带上客户端类型和版本信息

- [ ]  BEP：12
  Multitracker Metadata Extension,
  备用tracker服务器 和分布式tracker 服务器

- [ ]   BEP：7
  IPv6 Tracker Extension

- [ ]   BEP：24
  Tracker Returns External IP,
  tracker服务器返回peer它自己的公网ip, 帮助peer获取自己公网ip

- [ ]   BEP: 30
  Merkle hash torrent extension,
  解决传输超大文件,piece便越来越大的问题





# 接口文档
## announce接口
GET  /announce
### 请求参数：

name 1 | type  | 说明
---|---|---
info_hash |  byteArray  |Info键值的20字节SHA杂凑值.
peer_id |byteArray |20字节的下载者标识
ip |String | 非必填， 下载者的地址， 可以是域名也可是ip
port|Number |下载者的端口号
uploaded|Number | 已经下载的字节数
downloaded| Number | 已经上传的字节数
left| Number | 剩余需要下载的字节数
event|String| 下载者的当前状态， 四个值 empty , started, completed, stoped
compact|String|返回peer的方式， 是否紧凑型， 可选
auth_key|String|下载者的标识，用于PT下载


### 返回结果
失败结果：

name 1 | type  | 说明
---|---|---
failure reason |  String  | 失败的原因， 只有这个字段了

成功结果：

name 1 | type  | 说明
---|---|---
interval |  Number  |  下载者每次的请求频率，单位秒
min interval|  Number  |  下载者最小的请求频率， 单位秒
incomplete|  Number  |  正在下载中的其他peer数量
complete|Number| 下载完成的peer数量
peers|Object| 两种返回方式， 推荐使用紧凑型


## 注册
POST /signup

### 请求参数
name 1 | type  | 说明
--- |--- |---
nick |String |  昵称， 数字和字母组合
email |String |  邮箱， 重要凭证
pwd | String| 密码
sex| Int | 性别
code|String|邮箱收到的验证码

### 返回结果
name 1 | type  | 说明
--- |--- |---
id |int |  id
nick |String |  昵称， 数字和字母组合
email |String |  邮箱， 重要凭证
sex| Int | 性别
authKey|String|用户秘钥，十分重要



## 登录
POST /login

### 请求参数
name 1 | type  | 说明
--- |--- |---
email |String |  邮箱， 重要凭证
pwd | String| 密码

### 返回 header
name 1 | type  | 说明
---|---|---
contentType|Stirng | text/plain

### 返回结果

name 1 | type  | 说明
---|---|---
code| int | 200
message |String |  错误信息
body|Object|空


## 退出登录
GET /logout
### 请求参数
无
### 返回结果

name 1 | type  | 说明
---|---|---
code| int | 200
message |String |  错误信息
body|Object|空


## 下载种子
GET /download/torrent


### 请求参数：
name 1 | type  | 说明
---|---|---
id| Int |  种子文件的id

### 返回 header
name 1 | type  | 说明
---|---|---
contentType|Stirng |application/x-bittorrent

### 返回结果
返回文件下载流， 文件命就是种子名称


## 上传种子文件
POST  /upload/torrent
### 请求header
name 1 | type  | 说明
---|---|---
contentType|Stirng | multipart/form-data

### 请求参数

name 1 | type  | 说明
---|---|---
torrent |File |  种子文件
type |String |  分类
labels|String|标签
description|String| 简介
title|String | 标题
### 返回结果

name 1 | type  | 说明
---|---|---
code| int | 200
message |String |  错误信息
body|Object|空
