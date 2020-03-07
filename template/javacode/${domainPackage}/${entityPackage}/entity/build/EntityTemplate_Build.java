package ${source_package_entity_build};

import java.io.Serializable;
import java.sql.Timestamp;
import ${source_package_domain}.util.ConvertTBKey;

/**
 * 自动构建类，请勿在此手动修改代码
 * @ClassName ${source_package_entity_build}.${className?cap_first}Build.java
 * @Time ${.now}
 */
public class ${className?cap_first}Build implements Serializable {
<#list tableInfoList as item>
	private ${item.data_type} ${item.column_name}; // ${item.column_comment}
</#list>

    public static String fields = "<#list tableInfoList as item>t.${item.column_name}<#if item_has_next>,</#if></#list>";
    
<#list tableInfoList as item>
	<#if item.extra == 'auto_increment'>
	public String get${item.column_name?cap_first}() {
		return id;
	}
	<#else>
	public ${item.data_type} get${item.column_name?cap_first}() {
		return ${item.column_name};
	}
	</#if>
    
	public void set${item.column_name?cap_first}(${item.data_type} ${item.column_name}) {
		this.${item.column_name} = ${item.column_name};
    }
</#list>

	@Override
    public String toString() {
        return "[<#list tableInfoList as item>${item.column_name} = " + ${item.column_name} +"<#if item_has_next>,</#if></#list>]";
    }
}
