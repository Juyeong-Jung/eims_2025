package jp.co.trainocate.eims.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer empno;
	
	@Column(length = 20, nullable = false)
	private String lname;
	
	@Column(length = 20, nullable = false)
	private String fname;
	
	@Column(length = 50, nullable = false)
	private String lkana;
	
	@Column(length = 50, nullable = false)
	private String fkana;
	
	@Column(length = 8, nullable = false)
	private String password;
	
	@Column(nullable = false)
	private Integer gender;
	
	@Column(name="deptno")
	private Integer deptno;
	
	@ManyToOne
	@JoinColumn(name = "deptno", referencedColumnName = "deptno", insertable = false, updatable = false)
    private Department department;
	
}
