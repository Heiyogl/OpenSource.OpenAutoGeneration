package com.heiyogl.generation.entity;

/**
 * 表信息存储 实体
 * @ClassName： com.heiyogl.generation.entity.TableInfo.java
 * @Author： heiyogl
 */
public class TableInfo {
    private String column_name;

    private String data_type;

    private String column_type;

    private String column_comment;

    private String extra;
    
    private String column_key;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getColumn_comment() {
        return column_comment;
    }

    public void setColumn_comment(String column_comment) {
        this.column_comment = column_comment;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getColumn_type() {
        return column_type;
    }

    public void setColumn_type(String column_type) {
        this.column_type = column_type;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getColumn_key() {
        return column_key;
    }

    public void setColumn_key(String column_key) {
        this.column_key = column_key;
    }
}
