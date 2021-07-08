package com.example.demo.same;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GameController {

	@Autowired
	HttpSession session;

	@Autowired
	GameRepository gameRepository;

	@Autowired
	SamejiroRepository samejiroRepository;

	@Autowired
	BankRepository bankRepository;


	//ルール詳細画面表示
	@RequestMapping(value="/join/{code}", method=RequestMethod.POST)
	public ModelAndView showGametop(
			ModelAndView mv,
			@PathVariable("code") int code
	) {

		Optional<Game> game = gameRepository.findById(code);
		Game gameDetail = game.get();
		mv.addObject("game", gameDetail);

		if (code == 1) {
			mv.setViewName("gametop1");
		} else if (code == 2) {
			mv.setViewName("gametop2");
		} else if (code == 3) {
			mv.setViewName("gametop3");
		}

		return mv;
	}

	//結果表示
	@RequestMapping(value="/result/{code}", method=RequestMethod.POST)
	public ModelAndView showResult(
			ModelAndView mv,
			@PathVariable("code") int code,
			@RequestParam("lost") int lost,
			@RequestParam("won") int won
			) {
		Optional<Game> game = gameRepository.findById(code);
		Game gameDetail = game.get();
		mv.addObject("game", gameDetail);

		if(won != 0) {
			mv.addObject("message", "勝ち金が"+ won + "円増えました");
		}
		else if(lost != 0) {
			mv.addObject("message", "負け金が"+ lost + "円増えてしまいました");
		}

		mv.setViewName("result");
		return mv;
	}
}
