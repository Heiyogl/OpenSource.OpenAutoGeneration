简介：
	该程序主要实现了通过指定mysql数据库表，结合freemarker模板引擎生成对应的代码逻辑。
1、配置文件
	模板中使用的变量可以分别配置在如下两个配置文件中：
		datapacking_map.conf：主要用于模板中的数据填充；
		datatype_map.conf 	：主要用于数据库>Java的数据类型转换，格式为：mysql 类型=Java 类型；
	以上两个 conf 文件中的 key 值不支持 .   ，请使用  _    进行分隔。
	
	config.conf 为数据库相关配置文件。

2、系统内置变量
	系统内置的变量，在模板编写的时候也可以使用；具体的内置变量如下：
	tableName：（String）数据库表名；
	className：（String）类名；规则：将表名转换为类名， 比如 t_operate_log 转换后为 operateLog ，类名首字母应为大写，这里在freemarker的模板里直接转换；。
	pageName：（String）页面名；规则：将表名转换为页面名 比如 t_operate_log 转换后为 operate_log；
	tableInfoList：（List<TableInfo>）指定数据库表 的信息；字段：column_name,data_type,column_type,column_comment,extra,column_key；
	tableKey：（String）表的主键字段名；
	tableKeyType：（String）主键字段类型。
	
	
3、生成规则
	主包名：模板目录下 ${domainPackage} 主包名由配置文件datapacking_map.conf >> source_package_domain 配置项将 _ 转为 .  生成；
	业务包名：模板目录下 ${entityPackage} 业务包名为类名（className 系统内置变量）；
	文件名：类名+模板名+模板后缀。


4、关于路径
	模板路径存放在系统根目录的 template  文件夹内；
	生成的代码存放在系统根目录的 generated 文件夹内。

