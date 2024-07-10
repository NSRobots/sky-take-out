package com.sky.service.impl;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 员工添加
     *
     * @param employeeDTO 添加数据对应的实体对象
     * @return
     */
    @Override
    public Integer employeeAdd(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置status
        employee.setStatus(StatusConstant.ENABLE);
        //设置创建时间、更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //设置密码,默认为“123456”
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置创建人，修改人
        //获取当前用户的ID
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        int row = employeeMapper.insert(employee);
        return row;
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO 请求的数据实体对象
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //设置分页插件
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        //获取分页查询到后的数据
        List<Employee> pages = employeeMapper.pageQuery(employeePageQueryDTO);

        return new PageResult(employeePageQueryDTO.getPageSize(), pages);

    }

    /**
     * 员工状态修改
     *
     * @param status
     * @param id
     * @return
     */
    @Override
    public Integer statusForStartOrStop(Integer status, Long id) {
        //lomcok的new对象方式
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                //更新修改时间和修改用户
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        Integer row = employeeMapper.update(employee);
        return row;
    }

    /**
     * 根据id，查询员工信息
     *
     * @param id
     * @return
     */
    @Override
    public Employee employeeQueryById(Long id) {
        Employee employee = employeeMapper.queryById(id);
        return employee;
    }

    /**
     * 更新员工信息
     *
     * @param employeeDTO
     * @return
     */
    @Override
    public Integer employeeUpdate(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //json收到的数据传给employee类
        BeanUtils.copyProperties(employeeDTO, employee);
        //设置修改用户为当前用户
        employee.setUpdateUser(BaseContext.getCurrentId());
        //设置修改时间为当前时间
        employee.setUpdateTime(LocalDateTime.now());
        //注意密码安全
        employee.setPassword("*****");

        return employeeMapper.update(employee);
    }
}
