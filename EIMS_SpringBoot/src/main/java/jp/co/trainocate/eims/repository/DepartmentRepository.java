package jp.co.trainocate.eims.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.trainocate.eims.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
