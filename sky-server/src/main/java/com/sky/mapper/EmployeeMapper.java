package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface EmployeeMapper{

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 添加员工
     * @param employee
     */
    @Insert("insert into sky_take_out.employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values" +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Employee employee);

    /**
     * 分页查询
     * @param name
     * @return
     */
    Page<Employee> pageQuery(String name);

    /**
     * 员工账号启动停用
     * @param employee
     */
//  update通用方法，传入的entity对象哪个属性不为null，就根据entity对象id更改其对应的值
    void update(Employee employee);


    @Select("select * from sky_take_out.employee where id = #{id};")
    Employee getById(Long id);
}