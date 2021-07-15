package com.example.demo.same;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class QuizController {

	@Autowired
	HttpSession session;

	//http://localhost:8080/quiz
	@GetMapping ("/quiz")
	public ModelAndView quiz(ModelAndView mv) {
		mv.setViewName("quiz");
		return mv;
	}

	@PostMapping("/quiz")
	public ModelAndView quiztop(ModelAndView mv) {


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

		return mv;

	}

	@RequestMapping ("/quizAnswer")
	public ModelAndView quizAnswer(ModelAndView mv) {

		int count = (int)session.getAttribute("COUNT");


		mv.setViewName("quizAnswer");
		return mv;
	}


}
