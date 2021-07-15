package com.example.demo.same;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class QuizController {

	@Autowired
	HttpSession session;

	//http://localhost:8080/quiz
	@RequestMapping ("/quiztop")
	public ModelAndView quiztop(ModelAndView mv) {
		mv.setViewName("quiz");
		return mv;
	}

	@PostMapping("/quiz")
	public ModelAndView quiz(ModelAndView mv) {


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

		mv.setViewName("quiz");
		return mv;
	}

	@RequestMapping (value="/quizAnswer", params="true", method=RequestMethod.POST)
	public ModelAndView quizRight(ModelAndView mv) {
		Integer rightAnswers = (Integer)session.getAttribute("RIGHT");

		if(rightAnswers == null) {
			rightAnswers = 0;
		}
		rightAnswers++;

		session.setAttribute("RIGHT", rightAnswers);

		String RightOrWrong = "正解です！";
		mv.addObject("RightOrWrong", RightOrWrong);

		mv.setViewName("quizAnswer");
		return mv;
	}

	@RequestMapping (value="/quizAnswer", params="wrong", method=RequestMethod.POST)
	public ModelAndView quizWrong(ModelAndView mv) {
		Integer wrongAnswers = (Integer)session.getAttribute("RIGHT");

		if(wrongAnswers == null) {
			wrongAnswers = 0;
		}
		wrongAnswers++;

		session.setAttribute("WRONG", wrongAnswers);

		String RightOrWrong = "残念、不正解です";
		mv.addObject("RightOrWrong", RightOrWrong);

		mv.setViewName("quizAnswer");
		return mv;
	}

}
