package com.example.demo.same;

import java.util.ArrayList;
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

	//http://localhost:8080/
	//index.html(トップページ)を表示する
	@RequestMapping ("/")
	public ModelAndView index(ModelAndView mv) {

		mv.setViewName("index"); //フォワード先
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

		@SuppressWarnings("unchecked")
		List<People>alluser =(ArrayList<People>)session.getAttribute("People");
		for (People user : alluser) {
			String _Userid =user.getUserid();
			String _Password = user.getPassword();
			session.setAttribute("login",user);


			String name = user.getName();

			if( userid.equals( _Userid)  &&  password.equals(_Password)){
				mv.addObject("People",user);
				mv.addObject("alluser",alluser);
				mv.addObject("name","こんにちは、"+ name +"さん");
				mv.setViewName("select");

			return mv;
			}
		}

		if(userid.equals("") || password.equals("")) {
			mv.addObject("message", "未入力の項目があります");
			mv.setViewName("login");
		} else {
			mv.addObject("miss","ユーザIDとパスワードが一致しませんでした");
			mv.setViewName("login");
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
		@SuppressWarnings("unchecked")

		List<People> alluser =(List<People>) session.getAttribute("People");
		//セッションスコープに何も設定されていなければ作成
		if(alluser == null) {
			alluser = new ArrayList<>();
			session.setAttribute("People", alluser);
		}

		People p = new People();

		for (People user : alluser) {
			String _Userid =user.getUserid();
			String _Email = user.getEmail();

			if (userid.equals(_Userid)) {
				mv.addObject("Already","登録済みのユーザIDです");
				mv.setViewName("signup");

				return mv;
			}else if(email.equals(_Email)) {
				mv.addObject("Already2","登録済みのメールアドレスです");
				mv.setViewName("signup");

				return mv;
			}
		}

		if(userid.equals("")||  name.equals("")|| email.equals("")|| password.equals("")) {
			//未入力の場合
			mv.addObject("message", "未入力の項目があります");
			mv.setViewName("signup"); //フォワード先
		} else {
			// 書き込み情報をnewしてリストに追加
			p.setUserid(userid);
			p.setName(name);
			p.setEmail(email);
			p.setPassword(password);

			//１つの情報
			alluser.add(p);


		mv.addObject("People",alluser);
		mv.addObject("Registration","登録が完了しました");
		mv.setViewName("login"); //フォワード先
	 }


		return mv;
	}

	@RequestMapping("/logout")
	public ModelAndView out(ModelAndView mv) {
	session.removeAttribute("login");

	mv.setViewName("index"); //フォワード先
	return mv;

	}

}
