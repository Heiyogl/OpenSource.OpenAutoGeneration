package ${source_package_service_build};

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import ${source_package_entity}.${className?cap_first};
import ${source_package_mapper}.${className?cap_first}Mapper;
import cc.com.util.lang.Convert;


/**
 * 自动构建类，请勿在此手动修改代码
 * @ClassName ${source_package_service_build}.${className?cap_first}ServiceBuild.java
 * @Time ${.now}
 */
@Component
public class ${className?cap_first}ServiceBuild {
	@Autowired
    protected ${className?cap_first}Mapper ${className?uncap_first}Mapper;

    // 单条新增，返回ID
    public String insert(${className?cap_first} item){
        int result = -1;
        try {
        	// 设置主键UUID
        	if (null == item.getId() || "".equals(item.getId())) {
                item.setId(UUID.randomUUID().toString());
            }
        	
        	//返回受影响的行数
            result = ${className?uncap_first}Mapper.insert${className?cap_first}(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result >= 0) {
            return item.getId();
        } else {
            return "-1";
        }
    }
    
	// 批量新增，返回受影响的行数
    public int insertBatch(List<${className?cap_first}> list){
        return ${className?uncap_first}Mapper.insert${className?cap_first}Batch(list);
    }
    
    // 单条修改，返回受影响的行数
    public int update(${className?cap_first} item){
        return ${className?uncap_first}Mapper.update${className?cap_first}(item);
    }
    
    // 通过主键ID 查询一条信息
    public ${className?cap_first} findByid(String strId){
        return ${className?uncap_first}Mapper.find${className?cap_first}Byid(strId);
    }

	// 分页查询 	pageNo 当前页码,pageSize 每页显示条数,param 查询条件
    public PageInfo<${className?cap_first}> findItemByPage(int pageNo, int pageSize, HashMap<String, Object> param) {
        PageHelper.startPage(pageNo, pageSize);
        List<${className?cap_first}> ${className?uncap_first}s = ${className?uncap_first}Mapper.query${className?cap_first}s(param);
        PageInfo<${className?cap_first}> pageInfo = new PageInfo<${className?cap_first}>(${className?uncap_first}s);
        return pageInfo;
    }
    
    // 删除操作	-  通过主键及创建人所属组织ID 修改数据状态
    public int deleteByid(String id, String c_orgId){
    	${className?cap_first} item = new ${className?cap_first}();
    	item.setId(id);
    	item.setCreate_by_orgId(c_orgId);
    	
        return ${className?uncap_first}Mapper.update${className?cap_first}Cstatus(item);
    }
}
