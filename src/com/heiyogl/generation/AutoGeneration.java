package com.heiyogl.generation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.heiyogl.generation.service.AutoGenerationService;
import com.heiyogl.generation.util.Config;
import com.heiyogl.generation.util.FileUtil;

import freemarker.template.TemplateException;

/**
 * 主程序入口
 * @ClassName： com.heiyogl.generation.AutoGeneration.java
 * @Author： heiyogl
 */
public class AutoGeneration {
	public static String CONFIG_NAME = "config.conf";
	
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, TemplateException {
        String root = System.getProperty("user.dir");
        String templateRoot = root + "\\template";
    	
        // 获取所有模板的路径
        List<String> templatePaths = FileUtil.ergodicGetFilePath(templateRoot);


        //用于模板中的数据填充
        HashMap<String, Object> TEMP_DATA_MAP = Config.getAllConfig("datapacking_map.conf");
		
        //数据类型转换
        HashMap<String, Object> DATATYPE_MAP = Config.getAllConfig("datatype_map.conf");
		
		
        AutoGenerationService autoGenerationService = new AutoGenerationService(TEMP_DATA_MAP, DATATYPE_MAP);
        autoGenerationService.startGeneration(templatePaths);
    }
}
