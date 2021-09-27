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