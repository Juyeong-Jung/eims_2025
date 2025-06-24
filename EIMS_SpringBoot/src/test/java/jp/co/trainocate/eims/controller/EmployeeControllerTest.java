package jp.co.trainocate.eims.controller;

import static org.hamcrest.Matchers.*;

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

	/*@Test
	@DisplayName("検索画面が表示できること")
	void showSearchPage() throws Exception {
	    Mockito.when(departmentService.findAll()).thenReturn(List.of());
	
	    mockMvc.perform(get("/search"))
	           .andExpect(status().isOk())
	           .andExpect(view().name("search"))
	           .andExpect(model().attributeExists("departments"));
	}*/

	@Test
	@DisplayName("検索画面が表示できることと部署名リストの取得")
	void testShowSearchPage() throws Exception {
		List<Department> mockDepartments = List.of(
				new Department(100, "人事部", null),
				new Department(200, "総務部", null),
				new Department(300, "開発部", null)

		);
		Mockito.when(departmentService.findAll()).thenReturn(mockDepartments);

		mockMvc.perform(get("/search"))
				.andExpect(status().isOk())
				.andExpect(view().name("search"))
				.andExpect(model().attributeExists("departments"))
				.andExpect(model().attribute("departments", hasSize(3)));
	}

	@Test
	@DisplayName("社員番号検索が結果画面を返すこと")
	void testSelectByEmpNo() throws Exception {
		List<Employee> mockEmployee = List.of(
				new Employee(10001, "鈴木", "一郎", "スズキ", "イチロウ", "pass", 1, 100, new Department(100, "人事部", null))

		);
		Mockito.when(employeeService.findByEmpNo(10001)).thenReturn(mockEmployee);

		mockMvc.perform(get("/selectByEmpNo").param("empno", "10001"))
				.andExpect(status().isOk())
				.andExpect(view().name("search_result"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("employees", hasSize(1)));
	}

	@Test
	@DisplayName("社員名検索が結果画面を返すこと")
	void selectByEmpName() throws Exception {
		List<Employee> mockEmployee = List.of(
				new Employee(10001, "鈴木", "一郎", "スズキ", "イチロウ", "pass", 1, 100, new Department(100, "人事部", null)),
				new Employee(10002, "鈴木", "一郎", "スズキ", "イチロウ", "pass", 2, 200, new Department(200, "総務部", null)),
				new Employee(10003, "鈴木", "一郎", "スズキ", "イチロウ", "pass", 1, 300, new Department(300, "開発部", null))

		);
		Mockito.when(employeeService.findByEmpName("鈴木"))
				.thenReturn(mockEmployee);

		mockMvc.perform(get("/selectByEmpName").param("keyword", "鈴木"))
				.andExpect(status().isOk())
				.andExpect(view().name("search_result"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("employees", hasSize(3)));
	}

	@Test
	@DisplayName("部署番号検索が結果画面を返すこと")
	void selectByDeptNo() throws Exception {
		List<Employee> mockEmployee = List.of(
				new Employee(10001, "鈴木", "一郎", "スズキ", "イチロウ", "pass", 1, 100, new Department(100, "人事部", null)),
				new Employee(10002, "鈴木", "一郎", "スズキ", "イチロウ", "pass", 2, 100, new Department(200, "総務部", null)),
				new Employee(10003, "鈴木", "一郎", "スズキ", "イチロウ", "pass", 1, 100, new Department(300, "開発部", null))

		);
		Mockito.when(employeeService.findByDeptNo(100)).thenReturn(mockEmployee);

		mockMvc.perform(get("/selectByDeptNo").param("deptno", "100"))
				.andExpect(status().isOk())
				.andExpect(view().name("search_result"))
				.andExpect(model().attributeExists("employees"))
				.andExpect(model().attribute("employees", hasSize(3)));
	}

	@Test
	@DisplayName("新規登録画面が表示できること")
	void showInputPage() throws Exception {
		List<Department> mockDepartments = List.of(
				new Department(100, "人事部", null),
				new Department(200, "総務部", null),
				new Department(300, "開発部", null)

		);
		Mockito.when(departmentService.findAll()).thenReturn(mockDepartments);

		mockMvc.perform(get("/input"))
				.andExpect(status().isOk())
				.andExpect(view().name("input"))
				.andExpect(model().attributeExists("departments"))
				.andExpect(model().attribute("departments", hasSize(3)));
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
