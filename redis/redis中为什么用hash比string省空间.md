# Use hashes when possible



## 1丶尽可能的使用hashes.

redis官网原话为:

```tex
	Small hashes are encoded in a very small space, so you should try representing your data using hashes whenever possible. For instance if you have objects representing users in a web application, instead of using different keys for name, surname, email, password, use a single hash with all the required fields.
```

点击进入原文查看 [原文地址](https://redis.io/docs/reference/optimization/memory-optimization/)



## 2丶当你读完原文时,发现他借助数据结构为ziplist.



### 2.1 进入 ziplist 探究.





