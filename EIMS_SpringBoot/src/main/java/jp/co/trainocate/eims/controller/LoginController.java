package jp.co.trainocate.eims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jp.co.trainocate.eims.entity.Employee;
import jp.co.trainocate.eims.form.LoginForm;
import jp.co.trainocate.eims.service.EmployeeService;

@Controller
public class LoginController {
	
	@Autowired
	EmployeeService empService;
	
	// ログイン画面表示
    @GetMapping("/login")
    public String showLogin(LoginForm loginForm, Model model,HttpSession session) {

       return "login";
    }
    
    @PostMapping("/login")
    public String doLogin(@Valid LoginForm loginForm,BindingResult bdResult ,Model model, HttpSession session) {
    	if (bdResult.hasErrors()) {
    		return "login";
    	}
    	
    	Employee employee = empService.findByEmpnoAndPassword(loginForm.getEmpno(), loginForm.getPassword());
    	if (employee == null) {
    		model.addAttribute("loginError", "正しい社員番号とパスワードを入力してください");
    		return "login";
    	}
    	
    	session.setAttribute("user", employee);
    	
    	return "redirect:/";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
    	session.invalidate();
    	
    	return "redirect:/login";
    }
}
