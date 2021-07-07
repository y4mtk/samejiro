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
	@RequestMapping ("/")
	public ModelAndView top(ModelAndView mv) {
		@SuppressWarnings("unchecked")
		List<USER>allReg =(ArrayList<USER>)session.getAttribute("USER");

		if (allReg == null) {
			session.invalidate();
		}
		mv.addObject("login",allReg);

		mv.setViewName("top"); //フォワード先
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
				@RequestParam("ID") String ID,
				@RequestParam("PW") String PW,
				ModelAndView mv) {

			@SuppressWarnings("unchecked")
			List<USER>allReg =(ArrayList<USER>)session.getAttribute("USER");
			for (USER user : allReg) {
				String _ID =user.getID();
				String _PW = user.getPW();
				session.setAttribute("login",user);


				String NAME = user.getNAME();

				if(ID.equals(_ID)  &&  PW.equals(_PW)){
					mv.addObject("USER",user);
					mv.addObject("allReg",allReg);
					mv.addObject("NAME","こんにちは、"+ NAME +"さん");
					mv.setViewName("main");

				return mv;
				}
			}

			if(ID.equals("") || PW.equals("")) {
				mv.addObject("message", "未入力の項目があります");
				mv.setViewName("login");
			} else {
				mv.addObject("mes","ユーザIDとパスワードが一致しませんでした");
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
				@RequestParam("ID") String ID,
				@RequestParam("NAME") String NAME,
				@RequestParam("PW") String PW,
				ModelAndView mv) {
		@SuppressWarnings("unchecked")

		List<USER> allReg =(List<USER>) session.getAttribute("USER");
		//セッションスコープに何も設定されていなければ作成
		if(allReg == null) {
			allReg = new ArrayList<>();
			session.setAttribute("USER", allReg);
		}

		USER s = new USER();

		for (USER user : allReg) {
			String _ID =user.getID();

			if (ID.equals(_ID)) {
				mv.addObject("m","登録済みのユーザIDです");
				mv.setViewName("signup");

				return mv;
			}
		}

		if(ID.equals("")||  NAME.equals("")|| PW.equals("")) {
			//未入力の場合
			mv.addObject("message", "未入力の項目があります");
			mv.setViewName("signup"); //フォワード先
		} else {
			// 書き込み情報をnewしてリストに追加
			s.setID(ID);
			s.setNAME(NAME);
			s.setPW(PW);

			//１つの情報
			allReg.add(s);


		mv.addObject("USER",allReg);
		mv.addObject("u","登録が完了しました");
		mv.setViewName("login"); //フォワード先
		}


		return mv;
	}

		//http://localhost:8080/main
				@RequestMapping("/main")
				public String main() {
					return ("main");

				}

				@RequestMapping("/logout")
				public ModelAndView out(ModelAndView mv) {
				session.removeAttribute("login");

				mv.setViewName("top"); //フォワード先
				return mv;
			}

}
