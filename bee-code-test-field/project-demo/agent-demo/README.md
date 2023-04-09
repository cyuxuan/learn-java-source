# 特性概览
## 以下信息统计在日志信息中打印出来
#### 1. 统计函数执行时间
#### 2. 统计函数执行内存信息
#### 3. 统计函数执行过程中是否gc(gc信息对当前统计到的内存信息是否准确做判断辅助)
#### 4. 指定函数入口做探针, 避免监听其它一些不必要的信息
#### 5. 单独抓取SQL执行耗时，及当前执行的SQL语句
# 特性展望
## 结果信息打印到单独的文件中
## 结果信息支持展开与收缩(使用html或者其它方法存储结果)
## 全量信息统计-(一行一个执行时间)









# 在目标项目VM参数中指定对应的agent
-javaagent:D:\c_root\learn\learn\learn-java-source\bee-code-test-field\project-demo\agent-demo\target\agent-demo-1.0-SNAPSHOT.jar=cn.cyuxuan
