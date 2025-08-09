package jp.co.trainocate.eims.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import jp.co.trainocate.eims.entity.Department;
import jp.co.trainocate.eims.entity.Employee;
import jp.co.trainocate.eims.form.EmployeeForm;
import jp.co.trainocate.eims.service.DepartmentService;
import jp.co.trainocate.eims.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private ModelMapper modelMapper;

    private Department createDepartment(int deptno, String name) {
        Department dept = new Department();
        dept.setDeptno(deptno);
        dept.setDeptname(name);
        return dept;
    }

    private Employee createEmployee(int empno, int deptno) {
        Employee emp = new Employee();
        emp.setEmpno(empno);
        emp.setDeptno(deptno);
        return emp;
    }

    @Test
    void index_ReturnsIndexView() throws Exception {
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void showSearchPage_ReturnsSearchWithDepartments() throws Exception {
        List<Department> departments = List.of(createDepartment(1, "Sales"));
        when(departmentService.findAll()).thenReturn(departments);

        mockMvc.perform(get("/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"))
                .andExpect(model().attribute("departments", departments));
    }

    @Test
    void selectByEmpNo_WithParam_ReturnsResult() throws Exception {
        List<Employee> employees = List.of(createEmployee(1, 1));
        when(employeeService.findByEmpNo(1)).thenReturn(employees);

        mockMvc.perform(get("/selectByEmpNo").param("empno", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("search_result"))
                .andExpect(model().attribute("employees", employees));
    }

    @Test
    void selectByEmpNo_NoParam_ReturnsSearch() throws Exception {
        mockMvc.perform(get("/selectByEmpNo"))
                .andExpect(status().isOk())
                .andExpect(view().name("search"));
    }

    @Test
    void selectByEmpName_ReturnsResult() throws Exception {
        List<Employee> employees = List.of(createEmployee(1, 1));
        when(employeeService.findByEmpName("foo")).thenReturn(employees);

        mockMvc.perform(get("/selectByEmpName").param("keyword", "foo"))
                .andExpect(status().isOk())
                .andExpect(view().name("search_result"))
                .andExpect(model().attribute("employees", employees));
    }

    @Test
    void selectByDeptNo_ReturnsResult() throws Exception {
        List<Employee> employees = List.of(createEmployee(1, 1));
        when(employeeService.findByDeptNo(1)).thenReturn(employees);

        mockMvc.perform(get("/selectByDeptNo").param("deptno", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("search_result"))
                .andExpect(model().attribute("employees", employees));
    }

    @Test
    void showInputPage_ReturnsInputWithDepartments() throws Exception {
        List<Department> departments = List.of(createDepartment(1, "Sales"));
        when(departmentService.findAll()).thenReturn(departments);

        mockMvc.perform(get("/input"))
                .andExpect(status().isOk())
                .andExpect(view().name("input"))
                .andExpect(model().attribute("departments", departments));
    }

    @Test
    void confirmRegistration_WhenValidationFails_ReturnsInput() throws Exception {
        List<Department> departments = List.of(createDepartment(1, "Sales"));
        when(departmentService.findAll()).thenReturn(departments);

        mockMvc.perform(post("/inputConfirm")
                .param("lname", "")
                .param("fname", "Taro")
                .param("lkana", "ヤマダ")
                .param("fkana", "タロウ")
                .param("password", "pass")
                .param("gender", "1")
                .param("deptno", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("input"))
                .andExpect(model().attribute("departments", departments));
    }

    @Test
    void confirmRegistration_WhenValid_ReturnsConfirm() throws Exception {
        Department department = createDepartment(1, "Sales");
        when(departmentService.findById(1)).thenReturn(department);

        mockMvc.perform(post("/inputConfirm")
                .param("lname", "Yamada")
                .param("fname", "Taro")
                .param("lkana", "ヤマダ")
                .param("fkana", "タロウ")
                .param("password", "pass")
                .param("gender", "1")
                .param("deptno", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("input_confirm"))
                .andExpect(model().attribute("department", department));
    }

    @Test
    void saveEmployee_SavesAndReturnsComplete() throws Exception {
        Department department = createDepartment(1, "Sales");
        Employee mapped = new Employee();
        Employee saved = new Employee();
        saved.setEmpno(1);
        when(modelMapper.map(any(EmployeeForm.class), eq(Employee.class))).thenReturn(mapped);
        when(employeeService.saveEmployee(mapped)).thenReturn(saved);
        when(departmentService.findById(1)).thenReturn(department);

        mockMvc.perform(post("/saveEmployee")
                .param("lname", "Yamada")
                .param("fname", "Taro")
                .param("lkana", "ヤマダ")
                .param("fkana", "タロウ")
                .param("password", "pass")
                .param("gender", "1")
                .param("deptno", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("input_complete"))
                .andExpect(model().attribute("department", department));
    }

    @Test
    void deleteConfirm_ReturnsEmployeeInfo() throws Exception {
        Employee employee = createEmployee(1, 1);
        Department department = createDepartment(1, "Sales");
        when(employeeService.findByEmployee(1)).thenReturn(employee);
        when(departmentService.findById(1)).thenReturn(department);

        mockMvc.perform(get("/deleteConfirm/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("delete_confirm"))
                .andExpect(model().attribute("employee", employee))
                .andExpect(model().attribute("department", department));
    }

    @Test
    void deleteEmployee_PerformsDeleteAndReturnsComplete() throws Exception {
        Department department = createDepartment(1, "Sales");
        when(departmentService.findById(1)).thenReturn(department);

        mockMvc.perform(post("/deleteEmployee")
                .param("empno", "1")
                .param("deptno", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("delete_complete"))
                .andExpect(model().attribute("department", department));

        verify(employeeService).deleteEmployeeById(1);
    }

    @Test
    void changeInput_ReturnsFormAndDepartments() throws Exception {
        EmployeeForm form = new EmployeeForm();
        form.setEmpno(1);
        form.setLname("Yamada");
        form.setFname("Taro");
        form.setLkana("ヤマダ");
        form.setFkana("タロウ");
        form.setPassword("pass");
        form.setGender(1);
        form.setDeptno(1);
        List<Department> departments = List.of(createDepartment(1, "Sales"));
        when(employeeService.findByEmpNoAndCopyToEmployeeForm(eq(1), any(EmployeeForm.class))).thenReturn(form);
        when(departmentService.findAll()).thenReturn(departments);

        mockMvc.perform(get("/changeInput/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("change"))
                .andExpect(model().attribute("employeeForm", form))
                .andExpect(model().attribute("departments", departments));
    }

    @Test
    void changeConfirm_WhenValidationFails_ReturnsChange() throws Exception {
        List<Department> departments = List.of(createDepartment(1, "Sales"));
        when(departmentService.findAll()).thenReturn(departments);

        mockMvc.perform(post("/changeConfirm")
                .param("lname", "")
                .param("fname", "Taro")
                .param("lkana", "ヤマダ")
                .param("fkana", "タロウ")
                .param("password", "pass")
                .param("gender", "1")
                .param("deptno", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("change"))
                .andExpect(model().attribute("departments", departments));
    }

    @Test
    void changeConfirm_WhenValid_ReturnsConfirm() throws Exception {
        Department department = createDepartment(1, "Sales");
        when(departmentService.findById(1)).thenReturn(department);

        mockMvc.perform(post("/changeConfirm")
                .param("lname", "Yamada")
                .param("fname", "Taro")
                .param("lkana", "ヤマダ")
                .param("fkana", "タロウ")
                .param("password", "pass")
                .param("gender", "1")
                .param("deptno", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("change_confirm"))
                .andExpect(model().attribute("department", department));
    }

    @Test
    void changeEmployee_UpdatesAndReturnsComplete() throws Exception {
        Department department = createDepartment(1, "Sales");
        Employee mapped = new Employee();
        Employee saved = new Employee();
        saved.setEmpno(1);
        when(modelMapper.map(any(EmployeeForm.class), eq(Employee.class))).thenReturn(mapped);
        when(employeeService.saveEmployee(mapped)).thenReturn(saved);
        when(departmentService.findById(1)).thenReturn(department);

        mockMvc.perform(post("/changeEmployee")
                .param("empno", "1")
                .param("lname", "Yamada")
                .param("fname", "Taro")
                .param("lkana", "ヤマダ")
                .param("fkana", "タロウ")
                .param("password", "pass")
                .param("gender", "1")
                .param("deptno", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("change_complete"))
                .andExpect(model().attribute("department", department));
    }
}
