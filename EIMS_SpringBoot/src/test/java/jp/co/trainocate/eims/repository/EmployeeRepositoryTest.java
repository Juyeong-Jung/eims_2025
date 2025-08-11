package jp.co.trainocate.eims.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jp.co.trainocate.eims.entity.Department;
import jp.co.trainocate.eims.entity.Employee;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)

class EmployeeRepositoryTest {

	@Autowired
	private EmployeeRepository empRepo;

	@Autowired
	private DepartmentRepository deptRepo;

	Employee emp1;
	Employee emp2;
	Employee emp3;
	
	// --- 共通テストデータ（3件ずつ） ---
	@BeforeEach
	void setUp() {
		// 既存データを削除してリセット
		empRepo.deleteAll();
		deptRepo.deleteAll();

		// カテゴリを作成
		Department jinji = new Department();
		jinji.setDeptno(110);
		jinji.setDeptname("人事部");
		
		Department keiri = new Department();
		jinji.setDeptno(120);
		jinji.setDeptname("経理部");
		
		
		emp1 = empRepo.save(new Employee("山田", "陽翔", "ヤマダ", "ヒナタ", "password", 1, jinji.getDeptno()));
		emp2 = empRepo.save(new Employee("中田", "結衣", "タナカ", "ユイ", "password", 2, keiri.getDeptno()));
		emp3 = empRepo.save(new Employee("鈴木", "大翔", "スズキ", "ヒロト", "password", 1, jinji.getDeptno()));

	}

	@Test
	void testFindByEmpno() {
		List<Employee> result = empRepo.findByempno(emp1.getEmpno());
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getLname()).isEqualTo("山田");
	}
	
	@Test
	void testFindByEmpNameLike() {
		List<Employee> result = empRepo.findByEmpNameLike("田");
		assertThat(result).hasSize(2);
		assertThat(result).extracting(Employee::getLname).containsExactlyInAnyOrder("山田","中田");
	}
	
	/*@Test
	void testFindByDepartmentDeptno() {
		List<Employee> result = empRepo.findByDepartmentDeptno(110);
		assertThat(result).hasSize(2);
		assertThat(result.get(0).getLname()).isEqualTo("山田");
		assertThat(result.get(0).getDeptno()).isEqualTo(110);
	}*/
}