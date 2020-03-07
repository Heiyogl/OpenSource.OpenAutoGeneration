package ${source_package_controller_build};

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageInfo;
import ${source_package_domain}.controller.BaseController;
import ${source_package_entity}.${className?cap_first};
import ${source_package_service}.${className?cap_first}Service;
import cc.com.util.lang.Convert;

/**
 * 自动构建类，请勿在此手动修改代码
 * @ClassName ${source_package_controller_build}.${className?cap_first}ControllerBuild.java
 * @Time ${.now}
 */
@Component
public class ${className?cap_first}ControllerBuild extends BaseController {
	@Autowired
    private ${className?cap_first}Service ${className?uncap_first}Service;

	/**
     * 页面跳转：跳转到${className?cap_first}列表页 /${className?uncap_first}/${pageName}_list.html
     * <br/>Author: heiyogl
     */
    public String toList(String pageRoot) {
        return pageRoot + "/${className?uncap_first}/${pageName}_list";
    }
    
    public String toEdit(String pageRoot) {
        return pageRoot + "/${className?uncap_first}/${pageName}_edit";
    }
    
     /**
     * 通过多个参数查询(带分页)
     * @param param @RequestBody HashMap<String, Object> param  将页面参数直接封装到Map 计划中
     * <br/>URL:
     */
    public PageInfo getList(HashMap<String, Object> param){
        int pageNo = Convert.getInteger(null == param.get("pageNo") ? "1" : param.get("pageNo"));
        int pageSize = Convert.getInteger(null == param.get("pageSize") ? "10" : param.get("pageSize"));
        param.put("c_status", "0"); // 默认只查询数据状态为正常的数据
        param.put("c_orgId", getCurrentUserOrgId()); // 默认只查询自己组织内的数据
        PageInfo<${className?cap_first}> pageInfo = ${className?uncap_first}Service.findItemByPage(pageNo, pageSize, param);
        return pageInfo;
    }
    
    //单条保存，返回自增长ID的Str码
    public String insert(${className?cap_first} item){
    	// 默认属性赋值
        item.setCreate_by(getCurrentUserId());// 创建人ID
        item.setCreate_by_orgId(getCurrentUserOrgId());// 创建人所属组织ID
        item.setCreate_time(new Timestamp(System.currentTimeMillis()));// 创建时间
        item.setUpdate_time(new Timestamp(System.currentTimeMillis()));// 最后修改时间
        item.setDel_flag(0);// 数据状态
                
        return ${className?uncap_first}Service.insert(item);
    }
    
    //批量保存，返回受影响的行数
    public int insertBatch(List<${className?cap_first}> list){
        return ${className?uncap_first}Service.insertBatch(list);
    }
    
    // 单条修改，返回受影响的行数
    public int update(${className?cap_first} item){
    	// 默认属性赋值
        item.setUpdate_time(new Timestamp(System.currentTimeMillis()));// 最后修改时间
        //设置当前登录用户的组织ID，作为修改条件使用
        item.setCreate_by_orgId(getCurrentUserOrgId());
        
        return ${className?uncap_first}Service.update(item);
    }
    
    //通过主键ID 查询一条信息
    public ${className?cap_first} findByid(String strId, String c_orgId){
        ${className?cap_first} item = ${className?uncap_first}Service.findByid(strId);
        // 判断数据权限
        if (null != item &&  item.getCreate_by_orgId().equals(c_orgId)) {
            return item;
        } else {
            return new ${className?cap_first}();
        }
    }
    
	// 删除操作（逻辑删除）	-  通过主键及创建人所属组织ID 修改数据状态
    public int deleteByid(String id){
        return ${className?uncap_first}Service.deleteByid(id, getCurrentUserOrgId());
    }
}
