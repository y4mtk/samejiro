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

		String result1 = null;
		String result2 = null;
		String result3 = null;
		String result4 = null;
		String result5 = null;
		String result6 = null;
		String result7 = null;
		String result8 = null;
		String result9 = null;
		String result10 = null;

		if(count ==1) {
			result1 = "1";
		} else if(count == 2) {
			result2 = "2";
		}else if(count == 3) {
			result3 = "3";
		} else if(count == 4) {
			result4 = "4";
		} else if (count == 5) {
			result5 ="5";
		} else if (count == 6) {
			result6 ="6";
		} else if (count == 7) {
			result7 ="7";
		} else if (count == 8) {
			result8 ="8";
		} else if (count == 9) {
			result9 ="9";
		} else {
			result10 ="10";
		}



		session.setAttribute("COUNT", count);
		mv.addObject("result1",result1);
		mv.addObject("result2",result2);
		mv.addObject("result3",result3);
		mv.addObject("result4",result4);
		mv.addObject("result5",result5);
		mv.addObject("result6",result6);
		mv.addObject("result7",result7);
		mv.addObject("result8",result8);
		mv.addObject("result9",result9);
		mv.addObject("result10",result10);


		if(count >=10) {
			//セッションオブジェクトを消滅
			session.invalidate();

		}



		return mv;

	}


}
