package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import io.swagger.models.auth.In;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 员工添加
     * 1. 过DTO查询请求数据，使用DML添加语句到对应的表
     * 2. 将默认密码123456使用MD5加密后添加进实体
     * 3. 使用BaseContext封装的ThreadLocal，传递当前用户的id
     *
     * @param employeeDTO 添加数据对应的实体对象
     * @return
     */
    Integer employeeAdd(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * 1. 通过DTO查询请求数据，使用SQL查询对应的表
     * 2. 注意分页插件PageHelper的使用
     *
     * @param employeePageQueryDTO 请求的数据实体对象
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
}
