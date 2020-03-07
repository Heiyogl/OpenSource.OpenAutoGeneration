package ${source_package_mapper_build};

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import ${source_package_entity}.${className?cap_first};

/**
 * 自动构建类，请勿在此手动修改代码
 * @ClassName ${source_package_mapper_build}.${className?cap_first}MapperBuild.java
 * @Time ${.now}
 */
@Mapper
public interface ${className?cap_first}MapperBuild {
	// 通过主键ID查询
    public ${className?cap_first} find${className?cap_first}By${tableKey}(String strId);

    // 单条新增，返回受影响的行数
    public int insert${className?cap_first}(${className?cap_first} item);
    
    // 批量新增，返回受影响的行数
    public int insert${className?cap_first}Batch(List<${className?cap_first}> list);
    
    // 单条修改，返回受影响的行数
    public int update${className?cap_first}(${className?cap_first} item);
    
    // 分页查询
    public List<${className?cap_first}> query${className?cap_first}s(HashMap<String, Object> param);
    
    // 删除操作（逻辑删除）	-  通过主键及创建人所属组织ID 修改数据状态
    public int update${className?cap_first}Cstatus(${className?cap_first} item);
}
