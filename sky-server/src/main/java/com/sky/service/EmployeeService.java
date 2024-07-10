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

    /**
     * 修改员工状态
     * 1. 注意使用@PathVariable标识路径请求参数
     * 2. 根据ID查询当前员工，并修改其状态
     * 3. 修改可以写一个通用的update，方便后期直接调用
     * 4. 记得将UpdateTime更新！
     */
    Integer statusForStartOrStop(Integer status, Long id);

    /**
     * 根据id，查询员工信息
     *
     * @param id
     * @return
     */
    Employee employeeQueryById(Long id);

    /**
     * 更新员工信息
     *
     * @param employeeDTO
     * @return
     */
    Integer employeeUpdate(EmployeeDTO employeeDTO);
}
