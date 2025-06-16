package jp.co.trainocate.eims.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import jp.co.trainocate.eims.entity.Employee;
import jp.co.trainocate.eims.form.EmployeeForm;
import jp.co.trainocate.eims.service.EmployeeService;

@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/index")
	public String index() {
		return "index";
	}

       @GetMapping("/search")
       public String showSearchPage() {
               return "search";
       }

	@GetMapping("/selectByEmpNo")
	public String selectByEmpNo(Integer empno, Model model) {
		if (empno != null) {
			List<Employee> employees = employeeService.findByEmpNo(empno);
			model.addAttribute("employees", employees);
			return "search_result";
		}
		return "search";
	}

	@GetMapping("/selectByEmpName")
	public String selectByEmpName(String keyword, Model model) {
		List<Employee> employees = employeeService.findByEmpName(keyword);
		model.addAttribute("employees", employees);
		return "search_result";
	}

	@GetMapping("/selectByDeptNo")
	public String selectByDeptNo(Integer deptno, Model model) {
		List<Employee> employees = employeeService.findByDeptNo(deptno);
		model.addAttribute("employees", employees);
		return "search_result";
	}


    @GetMapping("/input")
    public String showInputPage(EmployeeForm employeeForm) {
        return "input";
    }



	@PostMapping("/inputConfirm")
	public String confirmRegistration(@Valid EmployeeForm employeeForm, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "input";
		}
		model.addAttribute("employeeForm", employeeForm);
		return "input_confirm";
	}

	@PostMapping("/saveEmployee")
	public String saveEmployee(EmployeeForm employeeForm, ModelMapper modelMapper) {
		Employee employee = modelMapper.map(employeeForm, Employee.class);
		employee = employeeService.saveEmployee(employee);
		employeeForm.setEmpno(employee.getEmpno());
		return "input_complete";
	}

       @GetMapping("/deleteConfirm/{empno}")
       public String deleteConfirm(@PathVariable("empno") Integer empno, Model model) {
		Employee employee = employeeService.findByEmployee(empno);
		model.addAttribute("employee", employee);
		return "delete_confirm";
	}

	@PostMapping("/deleteEmployee")
	public String deleteEmployee(EmployeeForm employeeForm, Model model) {
		employeeService.deleteEmployeeById(employeeForm.getEmpno());
		return "delete_complete";
	}

       @GetMapping("/changeInput/{empno}")
       public String changeInput(@PathVariable("empno") int empno, EmployeeForm employeeForm, Model model) {
		employeeForm = employeeService.findByEmpNoAndCopyToEmployeeForm(empno, employeeForm);
		return "change";
	}

	@PostMapping("/changeConfirm")
	public String changeConfirm(@Valid EmployeeForm employeeForm, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "change";
		}
		model.addAttribute("employeeForm", employeeForm);
		return "change_confirm";
	}

	@PostMapping("/changeEmployee")
	public String changeEmployee(EmployeeForm employeeForm, ModelMapper modelMapper) {
		Employee employee = modelMapper.map(employeeForm, Employee.class);
		employee = employeeService.saveEmployee(employee);
		return "change_complete";
	}
}
