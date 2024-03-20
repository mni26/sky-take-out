package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

@Data
//分页查询前端传进来的属性数据进行封装
public class EmployeePageQueryDTO implements Serializable {

    //员工姓名
    private String name;//非必须，查询可以选择是否根据姓名模糊匹配搜索

    //页码
    private int page;//必须

    //每页显示记录数
    private int pageSize;//必须

}
