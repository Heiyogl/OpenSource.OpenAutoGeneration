package com.heiyogl.generation.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * 配置文件工具类
 * @ClassName： com.heiyogl.generation.util.Config.java
 * @Author： heiyogl
 */
public class Config {
	public static String SYSTEM_ROOT = System.getProperty("user.dir");//系统路径
	static HashMap<String, HashMap<String, Object>> prop = new HashMap<String, HashMap<String, Object>>();
	

	private static HashMap<String, Object> getConfigFile(String configName){
		if (null != prop.get(configName)) {
			return prop.get(configName);
		}
		
		HashMap<String, Object> props = new HashMap<String, Object>();
		
		String path = SYSTEM_ROOT + "\\" + configName;

		if (!new File(path).exists()) {
		    path = SYSTEM_ROOT + "\\src\\" + configName;
        }
		
//		System.out.println("Config Path:" + path);
		
		if (!new File(path).exists()) {
            System.out.println(configName + " is not exit..");
            System.exit(0);
        }
		
		
		try {
            List<String> strList = FileUtil.readFileToList(path);
            
            for (String str : strList) {
                str = str.trim();
                if (str.startsWith("#") || "".equals(str)) {
                    continue;
                }
                String kv[] = str.split("=");
                props.put(kv[0].trim(), kv[1].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		prop.put(configName, props);
		
		return props;
	}
	
	public static HashMap<String, Object> getAllConfig(String configName) {
		return getConfigFile(configName);
	}
	
    public static String getConfig(String configName, String key) {
        return (String) getConfigFile(configName).get(key);
    }

    public static String getConfig(String configName, String key, String defaultvalue) {
        String r = (String) getConfigFile(configName).get(key);
        if (r != null) {
            return r;
        }
        return defaultvalue;
    }

    public static int getPropertyOfint(String configName, String key) {
        return getPropertyOfint(configName, key, 0);
    }

    public static int getPropertyOfint(String configName, String key, int defaultValue) {
        String value = getConfig(configName, key);
        if (value == null)
            return defaultValue;
        return Integer.valueOf(value);
    }

    public static boolean getBooleanConfig(String configName, String key) {
        if ("true".equals(getConfig(configName, key))) {
            return true;
        } else {
            return false;
        }
    }
}
