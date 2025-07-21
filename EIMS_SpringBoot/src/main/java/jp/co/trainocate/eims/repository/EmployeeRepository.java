package jp.co.trainocate.eims.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jp.co.trainocate.eims.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	List<Employee> findByempno(int empno);

	//JPQLを使うパターン
	@Query("SELECT e " +
			"FROM Employee e " +
			"WHERE e.lname LIKE %:keyword% " +
			"   OR e.fname LIKE %:keyword% " +
			"   OR e.lkana LIKE %:keyword% " +
			"   OR e.fkana LIKE %:keyword%")
	List<Employee> findByEmpNameLike(String keyword);
	
	/*	//JPQLを使わないパターン
		List<Employee> findByLnameLikeOrFnameLikeOrLkanaLikeOrFkanaLike(String lname, String fname, String lkana,
				String fkana);*/

    List<Employee> findByDepartmentDeptno(Integer deptno);
}
