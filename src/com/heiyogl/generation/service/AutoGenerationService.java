package com.heiyogl.generation.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.heiyogl.generation.AutoGeneration;
import com.heiyogl.generation.entity.TableInfo;
import com.heiyogl.generation.util.Config;
import com.mysql.jdbc.Statement;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * @ClassName： com.heiyogl.generation.service.AutoGenerationService.java
 * @Author： heiyogl
 */
public class AutoGenerationService {
    private static String url;
    private static String user;
    private static String pwd;
    private static String tableName;
    private static String dbName;
    
    private static String templateDir;
    private static String generatedDir;
    
    //数据类型转换
    private static HashMap<String, Object> DATATYPE_MAP = new HashMap<String, Object>();
    
    //用于模板中的数据填充
    private static HashMap<String, Object> TEMP_DATA_MAP = new HashMap<String, Object>();
    
    //配置项
    private static Properties properties = new Properties();
    

    static {
        InputStream inputStream = AutoGeneration.class.getClassLoader().getResourceAsStream(AutoGeneration.CONFIG_NAME);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //数据库链接信息
        url = properties.getProperty("mysql.uri");//"jdbc:mysql://127.0.0.1:3306/information_schema";
        user = properties.getProperty("mysql.user");//"root";
        pwd = properties.getProperty("mysql.pwd");//"root";
        //需要生成的目标表名
        tableName = properties.getProperty("target.tableName");//"user";
        dbName = properties.getProperty("target.dbName");//"test";
        
        System.out.println("初始化信息...");
        System.out.println(url + "@" + user + ":" + dbName + "." + tableName);
        
        
        //模板路径
        templateDir = System.getProperty("user.dir") + "\\template";
        //生成文件路径
        generatedDir = System.getProperty("user.dir") + "\\generated";
        
    }
    
    public AutoGenerationService(HashMap<String, Object> TEMP_DATA_MAP, HashMap<String, Object> DATATYPE_MAP) {
        this.TEMP_DATA_MAP = TEMP_DATA_MAP;
        this.DATATYPE_MAP = DATATYPE_MAP;
    }

    // 执行代码生成
    public void startGeneration(List<String> templatePaths) throws IOException, TemplateException, ClassNotFoundException, SQLException {
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");//解决freemarker模板读取后出现乱码的问题  
        
        String className = dealTableName();
        String entityName = dealClassName(className); // 对象名
        String pageName = dealPageName();
        
        List<TableInfo> tableInfoList = getTableInfo(tableName, dbName);
        
        TEMP_DATA_MAP.put("className", className); // 类名
        TEMP_DATA_MAP.put("tableName", tableName); // 数据库表名
        TEMP_DATA_MAP.put("pageName", pageName); // 页面名
        
        TEMP_DATA_MAP.put("tableInfoList", tableInfoList); // 指定数据库表 的信息
        
        System.out.println("开始生成...");
        for (String str : templatePaths) {
        	File file = new File(str);
        	if (!file.exists()) {
				continue;
			}
        	buildSource(TEMP_DATA_MAP, configuration, entityName, str);
        	
        }
        System.out.println(generatedDir);
        System.out.println("执行完毕..");
    }
    
    
    /**
     * 
     * @param TEMP_DATA_MAP
     * @param configuration 模板的配置对象
     * @param entityName 对象名
     * @param template 输出文件所用模板路径及名称
     * @param sourceRoot 目标文件根目录
     */
    private void buildSource(Map<String, Object> TEMP_DATA_MAP, Configuration configuration, String entityName, String template){
        try {
            String templateRoot = new File(template).getParent(); // 模板目录
            String templateName = new File(template).getName(); // 模板名称
            
            String sourceRoot = templateRoot.replace(templateDir, generatedDir);
            String domainPackage = Config.getConfig("datapacking_map.conf", "source_package_domain").replaceAll("\\.", "\\\\");
            String entityPackage = entityName;
            sourceRoot = sourceRoot.replace("${domainPackage}", domainPackage);
            sourceRoot = sourceRoot.replace("${entityPackage}", entityPackage);
            
            //创建输出文件目录
            creatDirs(sourceRoot);
            
            
            File docFile = new File(sourceRoot + "\\" + entityName + templateName);
            Writer docout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile),"UTF-8"));
            
            
            // 设置模板文件路径
            configuration.setDirectoryForTemplateLoading(new File(templateRoot));
            
            //输出文件
            Template temp = configuration.getTemplate(templateName);
            temp.setEncoding("UTF-8");
            temp.process(TEMP_DATA_MAP, docout);
            docout.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 获取指定数据库表 的信息
     * @param tableName
     * @param dbName
     * @return
     */
    private List<TableInfo> getTableInfo(String tableName, String dbName){
        ResultSet resultSet = null;
        ResultSet resultSetList = null;
        Statement statement = null;
        Connection conn = null;
        
        List<TableInfo> list = new ArrayList<TableInfo>();
        
        try {
            conn = DriverManager.getConnection(url, user, pwd);
            statement = (Statement) conn.createStatement();
            //获取表信息
            String sql = "select column_name,data_type,column_type,column_comment,extra,column_key from columns where table_name='" + tableName + "' and table_schema='" + dbName + "';";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setColumn_name(resultSet.getString("column_name"));
                tableInfo.setData_type(getDataType(resultSet.getString("data_type"))); // 转换Mysql所对应的Java数据类型
                tableInfo.setColumn_type(resultSet.getString("column_type"));
                tableInfo.setColumn_comment(resultSet.getString("column_comment"));
                tableInfo.setExtra(resultSet.getString("extra"));
                tableInfo.setColumn_key(resultSet.getString("column_key"));
                
                if (tableInfo.getColumn_key().equals("PRI")) {
                    TEMP_DATA_MAP.put("tableKey", tableInfo.getColumn_name()); // 表的主键字段名
                    TEMP_DATA_MAP.put("tableKeyType", tableInfo.getData_type()); // 主键字段类型
                }
                
                list.add(tableInfo);
            }

            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if (null != resultSetList) resultSetList.close();
                if (conn != null) conn.close();
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
        
        return list;
    }
    

    //将表名转换为类名 比如 t_operate_log 转换后为 operateLog ,类名首字母应为大写，这里在freemarker的模板里直接转换 
    private String dealTableName() {
        String className = tableName.toLowerCase().substring(tableName.indexOf("_") + 1);
        String charAfterLine = String.valueOf(className.charAt((className.indexOf("_") + 1)));
        String convertedChar = charAfterLine.toUpperCase();
        className = className.replace("_" + charAfterLine, convertedChar);
        return className;
    }

    //将类名转换为文件名，java公共类名与其文件名应该相同，这里将首字母转换为大写 如operateLog 转换后为 OperateLog
    private String dealClassName(String className) {
        String first = className.substring(0, 1).toUpperCase();
        String rest = className.substring(1, className.length());
        String fileName = new StringBuffer(first).append(rest).toString();
        return fileName;
    }
    
    //将表名转换为页面名 比如 t_operate_log 转换后为 operate_log
    private String dealPageName(){
        return tableName.substring(tableName.indexOf("_") + 1);
    }
    
    
    //数据类型转换
    private String getDataType(String data_type){
        return DATATYPE_MAP.get(data_type).toString();
    }
    
    //创建文件目录
    private void creatDirs(String path){
        File dirs = new File(path);
        //检查目录是否存在，不存在则创建
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
    }
}
