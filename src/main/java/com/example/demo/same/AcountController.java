package com.example.demo.same;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class AcountController {

	@Autowired
	private HttpSession session;

	@Autowired
	PeopleRepository peopleRepository;

	@Autowired
	BankRepository bankRepository;

	//http://localhost:8080/
	//index.html(トップページ)を表示する
	@RequestMapping ("/")
	public ModelAndView top(ModelAndView mv) {
		mv.setViewName("index");
		return mv;
	}

	//http://localhost:8080/login
		@RequestMapping("/login")
		public ModelAndView login(ModelAndView mv) {
			mv.setViewName("login"); //フォワード先
			return mv;
	}

	@PostMapping("/login")
	public ModelAndView log(
			@RequestParam("userid") String userid,
			@RequestParam("password") String password,
			ModelAndView mv) {

		if (userid.isEmpty() ||password.isEmpty()) {// 名前が空の場合にエラーとする
			mv.addObject("message", "未入力の項目があります");
			mv.setViewName("login");
		}else {
		List<People>user = peopleRepository.findByUserid(userid);

		boolean check = false;

		for(People p : user) {
		 if (userid.equals(p.getUserid()) && password.equals(p.getPassword())) {
			 check = true;
			 People login = new People(userid, p.getName(), password);
			 session.setAttribute("login", login);
			 break;
		 }
		}

		if(check == true) {

			mv.setViewName("select");
		}
		else {

			mv.addObject("miss","ユーザIDが一致しませんでした");
			 mv.setViewName("login");
		 }
	 }
		return mv;
	}

	//http://localhost:8080/signup
	@RequestMapping("/signup")
		public ModelAndView signup(ModelAndView mv) {
		mv.setViewName("signup"); //フォワード先
		return mv;
	}

	@PostMapping("/signup")
		public ModelAndView sign(
			@RequestParam("userid") String userid,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			ModelAndView mv) {


			//未入力項目
			if (userid.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
				mv.addObject("message","未入力の項目があります");
				mv.setViewName("signup");
			}


			//重複しているとき
			List<People> user = peopleRepository.findByEmail(email);
			int check = 1;

			for(People p : user) {
				if (userid.equals(p.getUserid())) {
					check = 2;
				} else if(email.equals(p.getEmail())) {
					check = 3;
				}
			}

			if(check == 2) {
				mv.addObject("Already1","登録済みのユーザIDです");
				mv.setViewName("signup");
			} else if(check == 3) {
				mv.addObject("Already","登録済みのメールアドレスです");
				mv.setViewName("signup");
			}
			else {
				People people = new People(userid,name,email,password);
				peopleRepository.saveAndFlush(people); //登録完了

				//Bankの初期値30000,0,0とPeopleのコードを紐づける
				int user_code = people.getCode();

				Bank bank = new Bank(user_code, 30000, 0, 0);
				bankRepository.saveAndFlush(bank);

				mv.addObject("Registration", "登録が完了しました");
				mv.setViewName("login"); //フォワード先
			}

		return mv;

	}


	//ログアウト
	@RequestMapping("/logout")
	public ModelAndView out(ModelAndView mv) {
	session.removeAttribute("login");

	mv.setViewName("index"); //フォワード先
	return mv;

	}

}
