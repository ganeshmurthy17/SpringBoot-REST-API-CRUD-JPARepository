package com.example.springboot.cruddemo.rest;

import com.example.springboot.cruddemo.entity.Employee;
import com.example.springboot.cruddemo.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRestController
{
    //private EmployeeDAO employeeDAO;
    private EmployeeService employeeService;

    private ObjectMapper objectMapper;

//    public EmployeeRestController(EmployeeDAO theEmployeeDao)
//    {
//        employeeDAO = theEmployeeDao;
//    }
    @Autowired
    public EmployeeRestController(EmployeeService theEmployeeService, ObjectMapper theObjectMapper)
    {
        employeeService = theEmployeeService;
        objectMapper = theObjectMapper;
    }

    @GetMapping("/employees")
    public List<Employee> findAll()
    {
        List<Employee> list = employeeService.findAll();
        return list;
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployeeById(@PathVariable int employeeId)
    {
        Employee byId = employeeService.findById(employeeId);

        if(byId==null)
        {
            throw new RuntimeException("EmplyeeId is not found - "+byId);
        }
        return byId;
    }

    @PostMapping("/employees")
    public Employee saveEmp(@RequestBody Employee theEmployee)
    {
        theEmployee.setId(0);

        Employee save = employeeService.save(theEmployee);

        return save;
    }

    @PutMapping("/employees")
    public Employee updateEmp(@RequestBody Employee theEmployee)
    {
        Employee update = employeeService.save(theEmployee);
        return update;
    }

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmp(@PathVariable int employeeId)
    {
        Employee byId = employeeService.findById(employeeId);

        if(byId==null)
        {
            throw new RuntimeException("EmployeeId Not Found - "+employeeId);
        }

        employeeService.deleteById(employeeId);
        return "Deleted Employee ID - "+employeeId;
    }

    @PatchMapping("/employees/{employeeId}")
    public Employee patchEmp(@PathVariable int employeeId,
                             @RequestBody Map<String,Object> patchPayload)
    {
        Employee tempEmp = employeeService.findById(employeeId);

        if(tempEmp==null)
        {
            throw new RuntimeException("Employee Id Not Found - "+employeeId);
        }

        if(patchPayload.containsKey("id"))
        {
            throw new RuntimeException("Employee Id is not allowed in RequestBody");
        }

        Employee patchEmp= apply(patchPayload,tempEmp);

        Employee empp = employeeService.save(patchEmp);

        return empp;
    }

    private Employee apply(Map<String, Object> patchPayload, Employee tempEmp)
    {
        // Convert Employee Object to a JSON Object Node
        ObjectNode empNode = objectMapper.convertValue(tempEmp, ObjectNode.class);

        // Convert the PatchPayload to a JSON Object Node
        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        // Merge the patch updates into the employee node
        empNode.setAll(patchNode);

        return objectMapper.convertValue(empNode,Employee.class);
    }
}
