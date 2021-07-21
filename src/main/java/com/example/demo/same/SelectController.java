package com.example.demo.same;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SelectController {

	@Autowired
	HttpSession session;

	@Autowired
	GameRepository gameRepository;

	@Autowired
	BankRepository bankRepository;

	@Autowired
	SamejiroRepository samejiroRepository;

	//選択画面表示
	@RequestMapping(value="/select")
	public ModelAndView select(ModelAndView mv) {
		Optional<Samejiro> samejiro = samejiroRepository.findById(1);
		Samejiro selectSamejiro = samejiro.get();

		mv.addObject("selectChat", selectSamejiro.getChat());

		mv.setViewName("select");
		return mv;
	}

	//全ゲームを表示
	@RequestMapping(value="/select/game")
	public ModelAndView showGames(ModelAndView mv) {
		List<Game> gameList = gameRepository.findAll();
		mv.addObject("games", gameList);

		mv.setViewName("list");
		return mv;
	}

	//ゲーム詳細画面へ
	@RequestMapping(value="/select/game/{code}", method=RequestMethod.GET)
	public ModelAndView showGame(
			ModelAndView mv,
			@PathVariable("code") int code) {
		Optional<Game> game = gameRepository.findById(code);
		Game gameDetail = game.get();
		mv.addObject("game", gameDetail);

		mv.setViewName("rules");
		return mv;
	}

	//残高確認画面へ
	@RequestMapping(value="/select/bank")
	public ModelAndView showBank(ModelAndView mv) {

		People player = (People) session.getAttribute("login");
		List<Bank> list = bankRepository.findByUserCode(player.getCode());
		Bank bankAccount = list.get(0);

		mv.addObject("bankAccount", bankAccount);
		mv.setViewName("bank");
		return mv;
	}
}
