package ${source_package_controller};

import java.sql.Timestamp;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import ${source_package_domain}.comm.aop.LoggerManage;
import ${source_package_domain}.domain.entity.${className?cap_first};
import ${source_package_domain}.domain.entity.result.ExceptionMsg;
import ${source_package_domain}.domain.entity.result.ResponseData;


@Controller
public class ${className?cap_first}Controller extends ${source_package_controller_build}.${className?cap_first}ControllerBuild {
	private String pageRoot = "/manager";// 页面存放根目录

	/**
     * 页面跳转：跳转到${className?cap_first}列表页 /${className?uncap_first}/${pageName}_list.html
     * <br/>Author: heiyogl
     */
    @RequestMapping("/${className?uncap_first}/list")
    public String toList() {
        return super.toList(pageRoot);
    }
    
    @RequestMapping("/${className?uncap_first}/edit")
    public String toEdit(Model model) {
        String strId = getRequest().getParameter("id");
        
        // 如果传输过来的id为空值，则视为新建；否则加载改条信息，视为修改。
        if (null == strId || "".equals(strId)) {
            model.addAttribute("${className?uncap_first}", new ${className?cap_first}());
        } else {
            ${className?cap_first} ${className?uncap_first} = super.findByid(strId, getCurrentUserOrgId());
            model.addAttribute("${className?uncap_first}", ${className?uncap_first});
        }

        return super.toEdit(pageRoot);
    }
    
    /**
     * 通过多个参数查询(带分页)
     * @param param @RequestBody HashMap<String, Object> param  将页面参数直接封装到Map 计划中
     * 获取 Get 方式传输的参数，可使用 getRequest().getParameter(" "); 方法进行获取
     * <br/>URL:
     */
    @ResponseBody
    @RequestMapping(value = "/${className?uncap_first}.lists", method = RequestMethod.POST)
    @LoggerManage(description = "/${className?uncap_first}.lists 通过多个参数查询，返回PageInfo集合")
    public PageInfo getList(@RequestBody HashMap<String, Object> param){
        return super.getList(param);
    }
    
    /**
     * 新增记录
     * @param ${className?cap_first} 页面封装的实体
     * @return 
     * <br/>URL:
     */
    @ResponseBody
    @RequestMapping(value = "/${className?uncap_first}.create", method = RequestMethod.POST)
    @LoggerManage(description = "/${className?uncap_first}.create")
    public ResponseData create(@RequestBody ${className?cap_first} item, HttpServletRequest request){
        // 单条保存，返回ID
        String strID = super.insert(item);
        if ("-1".equals(strID)) {
            return new ResponseData(ExceptionMsg.FAILED);
        } else {
            // 返回ID
            return new ResponseData(ExceptionMsg.SUCCESS, strID);
        }
    }
    
    
    /**
     * 修改记录
     * @param ${className?cap_first} 页面封装的实体
     * @return 
     * <br/>URL:
     */
    @ResponseBody
    @RequestMapping(value = "/${className?uncap_first}.modify", method = RequestMethod.POST)
    @LoggerManage(description = "/${className?uncap_first}.modify")
    public ResponseData modify(@RequestBody ${className?cap_first} item, HttpServletRequest request){
        // 判断是否是同一组织。是，则允许修改；否则无权限修改。
        ${className?cap_first} _item = super.findByid(item.getId(), getCurrentUserOrgId()); 
        if ("".equals(_item.getId())) {
            return new ResponseData(ExceptionMsg.NO_AUTHORITY);// 无权限操作
        }
        
        // 单条修改，返回受影响的行数
        int result = super.update(item);
        if (result > 0) {
            return new ResponseData(ExceptionMsg.SUCCESS);
        } else {
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }
    
    /**
     * 删除操作（逻辑删除）	-  通过主键及创建人所属组织ID 修改数据状态
     * @param ${className?cap_first} 页面封装的实体
     * @return 
     * <br/>URL:
     */
    @ResponseBody
    @RequestMapping(value = "/${className?uncap_first}.deleteByid", method = RequestMethod.POST)
    @LoggerManage(description = "/${className?uncap_first}.deleteByid 通过ID删除接口")
    public ResponseData deleteByid(){
    	String strId = getRequest().getParameter("id");
        if (null == strId) {
            return new ResponseData(ExceptionMsg.FAILED);
        } else {
            int result = super.deleteByid(strId);
            if (result <= 0) {
                return new ResponseData(ExceptionMsg.FAILED);
            } else {
                return new ResponseData(ExceptionMsg.SUCCESS);
            }
        }
    }
}
