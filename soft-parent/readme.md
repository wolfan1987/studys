<h1 style="margin: 0 auto; text-align:center;">项目说明</h1>

# 项目目录结构

	soft-parent
	│
	├──module	--- 模块划分
	│    └─ merchant	--- 商家模块
	│			├── merchant-dao		--- 商家平台dao包
	│			│   ├── merchant-dao-api	--- 商家平台dao接口
	│			│   └── merchant-dao-impl	--- 商家平台dao实现
	│			│
	│			├── merchant-service		--- 商家平台service包
	│			│   ├── merchant-service-api
	│			│   └── merchant-service-impl
	│			│
	├──web	--- web划分
	│    └─ merchant-web	--- 商家平台web程序


# 项目所使用的框架及技术

* Servlet 3.0
* Spring 3
* Spring Mvc 3
* Thymeleaf 3
* dubbo 2.8.4
* Mybatis
* jQuery
* Maven
* Netty 4 (已在commons-socket中进行相应操作的封装)
* shiro
* SUI (淘宝的开源手机H5框架：<http://m.sui.taobao.org>)

# spring/dubbo 配置约定

1. spring初始化通过指定的单一入口配置加载。其它文件通过`<import>`标签进行引入。 入口配置文件名称定义为`spring-main.xml`
2. spring bean 默认采用注解方式进行配置。并开启自动扫描。
3. dubbo 服务和引用同样也采用注解方式配置。

# 日志约定
1. 整个项目采用slf4j日志框架进行代码日志处理接口。**有且仅有一种日志处理接口框架**。
2. 采用logback作为日志输出框架。

# SocketDao 使用指南
SocketDao 主要是封装了scoket连接和调用相关的操作，并提供简化的socket请求响应方法。透明化socket连接池化，和socket连接状态管理。

## 配置socket工厂和连接池

``` java
<!--socketClientFactory配置-->
<bean id="socketClientFactory"
      class="com.zjht.solar.commons.socket.netty.spring.NettyClientFactoryFactoryBean"
      init-method="init" destroy-method="destory">
    <!-- 服务端地址 -->
    <property name="address" value="127.0.0.1"/>
    <!-- 服务端端口 -->
    <property name="port" value="4545"/>
    <!-- 连接超时时间，单位毫秒 -->
    <property name="connectTimeout" value="30000"/>
    <!-- 更多参数参见类属性 -->
</bean>

<!--socketClientPool配置-->
<bean id="socketClientPool" class="com.zjht.solar.commons.socket.pool.SocketClientPool"
      init-method="init" destroy-method="destory">
    <property name="clientFactory" ref="socketClientFactory"/>
    <!-- 更多参数参见类属性 -->
</bean>
```

## 编写 业务Dao类

所有业务Dao类都需要继承`com.zjht.solar.commons.socket.dao.AbstractSocketDao`。该类封装了所有socket相关操作及socket连接生命周期管理。业务使用时无需关心socket相关状态。

## GIT提交注释规范
   A. 目的：git提交注释统一规范，方便查阅不同版本间功能变动情况，有利于快速定位问题。

   B. 分类：新增、修改、删除、BUG(ID)、特殊

   C. 注：单功能提交，避免多、杂、乱提交。
   
1. git 新增示例

    新增：新增购买电子卡功能，选择面值（或自定义金额）、卡面进行购买电子卡。新增数据库xxx表，新增xxx配置文件。新增xxx数据字典。

2. git 修改示例

    修改：修改购买电子卡流程，当支付成功后调用C端开卡接口失败时循环N次调用（JOB定时任务）。

3. git 删除示例

    删除：删除订单定义的水印电子卡图片字段，同时删除数据库ecard_order表pan_img_url字段。

4. git BUG(ID)示例

    BUG(5784):BUG已修复完成，未影响到其它功能。

5. git 特殊示例
    
    特殊：因领导需要演示需临时屏蔽支付功能，点击购卡跳跃支付步骤直接开卡。后续需设置关开参数。

5. git 其它情况

    新增：新增JOB定时项目模块schedule-binary（与DAO类似、独立启动）。
    
    修改：修改充值流程，当充值支付成功调用C充值接口失败时循环N次调用(JOB定时任务)。

## 命名规范

- url命名
url采用restful风格结构，命名采用全小写英文定义，多单词组合时采用`-`分隔。 例如：`/admin/upload-file`
- html, js, css 等文件名命名
命名采用全小写英文定义，多单词组合时采用`-`分隔。
- css class 定义命名
命名采用全小写英文定义，多单词组合时采用`-`分隔。
- html 标签id命名
命名采用全小写英文定义，多单词组合时采用`-`分隔。