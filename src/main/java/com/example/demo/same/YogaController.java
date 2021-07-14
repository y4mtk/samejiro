package com.example.demo.same;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class YogaController {

	@Autowired
	HttpSession session;

	//http://localhost:8080/blackjack
	@GetMapping ("/yoga")
	public ModelAndView yoga(ModelAndView mv) {
		mv.setViewName("yoga");
		return mv;
	}

	@PostMapping("/yoga")
	public ModelAndView yogatop(ModelAndView mv) {


		Integer count = (Integer)session.getAttribute("COUNT");

		if(count == null) {
			count = 0;
		}

		count++;



		session.setAttribute("COUNT", count);


		if(count >=9) {
			//セッションオブジェクトを消滅
			session.removeAttribute("COUNT");

		}


	//	mv.setViewName("yoga");
		return mv;

	}


}
