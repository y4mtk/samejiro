package com.example.demo.same;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class YogaController {

	@Autowired
	HttpSession session;

	//http://localhost:8080/blackjack
	@RequestMapping ("/yoga")
	public ModelAndView blackjack(ModelAndView mv) {
		mv.setViewName("yoga");
		return mv;
	}

	@PostMapping("/yoga")
	public ModelAndView bjposttop(ModelAndView mv) {

		Integer count = (Integer)session.getAttribute("COUNT");

		if(count == null) {
			count = 0;
		}

		count++;



		session.setAttribute("COUNT", count);


		if(count >=10) {
			//セッションオブジェクトを消滅
			session.invalidate();



		}


	//	mv.setViewName("yoga");
		return mv;

	}


}
