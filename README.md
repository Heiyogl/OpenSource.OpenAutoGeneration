# OpenSource.OpenAutoGeneration

#### 介绍
程序主要实现了通过指定mysql数据库表，结合freemarker模板引擎生成对应的代码逻辑，并内置实例模板

#### 软件架构
软件架构说明



#### 使用说明

## 1、配置文件 
	模板中使用的变量可以分别配置在如下两个配置文件中： 
		datapacking_map.conf：主要用于模板中的数据填充；  
		datatype_map.conf 	：主要用于数据库>Java的数据类型转换，格式为：mysql 类型=Java 类型；  
	以上两个 conf 文件中的 key 值不支持 .   ，请使用  _    进行分隔。 
	
	config.conf 为数据库相关配置文件。 

## 2、系统内置变量  
	系统内置的变量，在模板编写的时候也可以使用；具体的内置变量如下： 
	tableName：（String）数据库表名；  
	className：（String）类名；规则：将表名转换为类名， 比如 t_log 转换后为 Log ，类名首字母应为大写，这里在freemarker的模板里直接转换；  
	pageName：（String）页面名；规则：将表名转换为页面名 比如 t_log 转换后为 log；  
	tableInfoList：（List<TableInfo>）指定数据库表 的信息；字段:column_name,data_type,column_type,column_comment,extra,column_key；  
	tableKey：（String）表的主键字段名；  
	tableKeyType：（String）主键字段类型。 
	
	
## 3、生成规则 
	主包名：模板目录下 ${domainPackage} 主包名由配置文件datapacking_map.conf >> source_package_domain 配置项将 _ 转为 .  生成； 
	业务包名：模板目录下 ${entityPackage} 业务包名为类名（className 系统内置变量）； 
	文件名：类名+模板名+模板后缀。 


## 4、关于路径 
	模板路径存放在系统根目录的 template  文件夹内； 
	生成的代码存放在系统根目录的 generated 文件夹内。 

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 码云特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5.  码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
