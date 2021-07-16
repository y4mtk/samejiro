package com.example.demo.same;

import java.util.List;
import java.util.Optional;

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

	@Autowired
	BankRepository bankRepository;

	@Autowired
	GameRepository gameRepository;

	//http://localhost:8080/quiz
	@RequestMapping ("/quiztop")
	public ModelAndView quiztop(ModelAndView mv) {

		People loginee = (People)session.getAttribute("login");

		List<Bank> playerBank = bankRepository.findByUserCode(loginee.getCode());
		Bank bankAccount = playerBank.get(0);
		List<Bank> JP = bankRepository.findByUserCode(0);
		Bank JPbank = JP.get(0);

		Optional<Game> list2 = gameRepository.findById(4);
		Game game = list2.get();

		if(game.getPrice() > bankAccount.getMoney()) {
			mv.addObject("message", "残高が足りません");
			mv.setViewName("error");
			return mv;
		}

		Bank newMoney = new Bank(bankAccount.getCode(), bankAccount.getUserCode(), bankAccount.getMoney()-game.getPrice(), bankAccount.getLost(), bankAccount.getWon());
		bankRepository.saveAndFlush(newMoney);

		Bank newJP = new Bank(JPbank.getCode(), 0, JPbank.getMoney()+game.getPrice(), 0, 0);
		bankRepository.saveAndFlush(newJP);

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
			session.removeAttribute("RIGHT");
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

		String RightOrWrong = "残念、不正解です";
		mv.addObject("RightOrWrong", RightOrWrong);

		mv.setViewName("quizAnswer");
		return mv;
	}

}
