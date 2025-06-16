package jp.co.trainocate.eims.service;

import java.util.List;

import jp.co.trainocate.eims.entity.Employee;
import jp.co.trainocate.eims.form.EmployeeForm;

public interface EmployeeService {
	List<Employee> findByEmpNo(int empno);

	List<Employee> findByEmpName(String keyword);

	List<Employee> findByDeptNo(Integer deptno);

	Employee saveEmployee(Employee employee);

	Employee findByEmployee(int empno);

	void deleteEmployeeById(Integer empno);

	EmployeeForm findByEmpNoAndCopyToEmployeeForm(int empno, EmployeeForm employeeForm);

}
