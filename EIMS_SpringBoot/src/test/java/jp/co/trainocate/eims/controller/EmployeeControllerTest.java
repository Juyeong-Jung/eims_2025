package jp.co.trainocate.eims.controller;

/**
 * {@link EmployeeController} のコンポーネントテスト。
 * <p>
 * Controller 層のみを対象とし、サービス層はモック化して挙動を検証する。
 */

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jp.co.trainocate.eims.entity.Department;
import jp.co.trainocate.eims.entity.Employee;
import jp.co.trainocate.eims.form.EmployeeForm;
import jp.co.trainocate.eims.service.DepartmentService;
import jp.co.trainocate.eims.service.EmployeeService;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private DepartmentService departmentService;

    @Test
    @DisplayName("検索画面が表示できること")
    void showSearchPage() throws Exception {
        Mockito.when(departmentService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/search"))
               .andExpect(status().isOk())
               .andExpect(view().name("search"))
               .andExpect(model().attributeExists("departments"));
    }

    @Test
    @DisplayName("社員番号検索が結果画面を返すこと")
    void selectByEmpNo() throws Exception {
        List<Employee> list = List.of(new Employee());
        Mockito.when(employeeService.findByEmpNo(1)).thenReturn(list);

        mockMvc.perform(get("/selectByEmpNo").param("empno", "1"))
               .andExpect(status().isOk())
               .andExpect(view().name("search_result"))
               .andExpect(model().attribute("employees", list));
    }

    @Test
    @DisplayName("社員名検索が結果画面を返すこと")
    void selectByEmpName() throws Exception {
        List<Employee> list = List.of(new Employee());
        Mockito.when(employeeService.findByEmpName("テスト"))
               .thenReturn(list);

        mockMvc.perform(get("/selectByEmpName").param("keyword", "テスト"))
               .andExpect(status().isOk())
               .andExpect(view().name("search_result"))
               .andExpect(model().attribute("employees", list));
    }

    @Test
    @DisplayName("部署番号検索が結果画面を返すこと")
    void selectByDeptNo() throws Exception {
        List<Employee> list = List.of(new Employee());
        Mockito.when(employeeService.findByDeptNo(100)).thenReturn(list);

        mockMvc.perform(get("/selectByDeptNo").param("deptno", "100"))
               .andExpect(status().isOk())
               .andExpect(view().name("search_result"))
               .andExpect(model().attribute("employees", list));
    }

    @Test
    @DisplayName("新規登録画面が表示できること")
    void showInputPage() throws Exception {
        Mockito.when(departmentService.findAll()).thenReturn(List.of(new Department()));

        mockMvc.perform(get("/input"))
               .andExpect(status().isOk())
               .andExpect(view().name("input"))
               .andExpect(model().attributeExists("departments"));
    }

    @Test
    @DisplayName("社員削除が完了画面を返すこと")
    void deleteEmployee() throws Exception {
        Mockito.doNothing().when(employeeService).deleteEmployeeById(1);

        mockMvc.perform(post("/deleteEmployee")
                .param("empno", "1"))
               .andExpect(status().isOk())
               .andExpect(view().name("delete_complete"));
    }

    @Test
    @DisplayName("変更入力画面が表示できること")
    void changeInput() throws Exception {
        EmployeeForm form = new EmployeeForm();
        Mockito.when(employeeService.findByEmpNoAndCopyToEmployeeForm(1, form)).thenReturn(form);
        Mockito.when(departmentService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/changeInput/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("change"))
               .andExpect(model().attributeExists("departments"));
    }

    @Test
    @DisplayName("変更確定が完了画面を返すこと")
    void changeEmployee() throws Exception {
        Mockito.when(employeeService.saveEmployee(Mockito.any(Employee.class)))
               .thenReturn(new Employee());

        mockMvc.perform(post("/changeEmployee")
                .param("empno", "1"))
               .andExpect(status().isOk())
               .andExpect(view().name("change_complete"));
    }
}

