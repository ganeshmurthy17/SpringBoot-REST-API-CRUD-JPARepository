package com.example.springboot.cruddemo.service;

import com.example.springboot.cruddemo.dao.EmployeeRepository;
import com.example.springboot.cruddemo.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImplementations implements EmployeeService
{
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImplementations(EmployeeRepository theEmployeeRepository)
    {
        employeeRepository = theEmployeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(int id)
    {
        Optional<Employee> result = employeeRepository.findById(id);
        Employee theEmp=null;

        if(result.isPresent()){
            theEmp=result.get();
        }
        else {
            throw new RuntimeException("Did not find employee id - " +id);
        }
        return theEmp;
    }


    @Override
    public Employee save(Employee theEmployee) {
        return employeeRepository.save(theEmployee);
    }


    @Override
    public void deleteById(int id) {
        employeeRepository.deleteById(id);
    }
}
