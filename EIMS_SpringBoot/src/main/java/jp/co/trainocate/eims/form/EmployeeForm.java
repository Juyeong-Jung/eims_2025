package jp.co.trainocate.eims.form;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeForm {
	
	private Integer empno;
	
    @NotBlank(message = "氏は必須項目です")
    @Size(max = 10, message = "氏は10文字以内で入力してください")
    private String lname;

    @NotBlank(message = "名は必須項目です")
    @Size(max = 10, message = "名は10文字以内で入力してください")
    private String fname;

    @NotBlank(message = "氏（カナ）は必須項目です")
    @Size(max = 10, message = "氏（カナ）は10文字以内で入力してください")
    private String lkana;

    @NotBlank(message = "名（カナ）は必須項目です")
    @Size(max = 10, message = "名（カナ）は10文字以内で入力してください")
    private String fkana;

    @NotBlank(message = "パスワードは必須項目です")
    @Size(max = 8, message = "パスワードは8文字以内で入力してください")
    private String password;

    @NotNull(message = "性別は必須項目です")
    private Integer gender;

    private Integer deptno;
}
