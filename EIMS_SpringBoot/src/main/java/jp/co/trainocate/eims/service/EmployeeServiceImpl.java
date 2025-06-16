package jp.co.trainocate.eims.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.trainocate.eims.entity.Employee;
import jp.co.trainocate.eims.form.EmployeeForm;
import jp.co.trainocate.eims.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private final EmployeeRepository employeeRepository;

	@Override
	public List<Employee> findByEmpNo(int empno) {
		return employeeRepository.findByempno(empno);
	}

	@Override
	public List<Employee> findByEmpName(String keyword) {
		return employeeRepository.findByEmpNameLike("%" + keyword + "%");
	}

	//JPQLを使わないパターン
	/*public List<Employee> findByEmpName(String keyword) {
		String likeKeyword = "%" + keyword + "%";
		return employeeRepository.findByLnameLikeOrFnameLikeOrLkanaLikeOrFkanaLike(likeKeyword, likeKeyword,
				likeKeyword, likeKeyword);
	}*/
	@Override
	public List<Employee> findByDeptNo(Integer deptno) {
		return employeeRepository.findByDepartmentDeptno(deptno);
	}

	@Override
	public Employee saveEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public Employee findByEmployee(int empno) {
		return employeeRepository.findById(empno).get();
	}

	@Override
	public void deleteEmployeeById(Integer empno) {
		employeeRepository.deleteById(empno);
	}

	@Override
	public EmployeeForm findByEmpNoAndCopyToEmployeeForm(int empno, EmployeeForm employeeForm) {
		Employee employee = employeeRepository.findById(empno).get();
		employeeForm.setEmpno(employee.getEmpno());
		employeeForm.setFname(employee.getFname());
		employeeForm.setLname(employee.getLname());
		employeeForm.setFkana(employee.getFkana());
		employeeForm.setLkana(employee.getLkana());
		employeeForm.setGender(employee.getGender());
		employeeForm.setDeptno(employee.getDeptno());
		employeeForm.setPassword(employee.getPassword());
		return employeeForm;
	}
}
