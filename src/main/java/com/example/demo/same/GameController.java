package com.example.demo.same;

import java.util.List;
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
	@RequestMapping(value="/join/{code}", method=RequestMethod.GET)
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
			List<Bank> bankJackPot = bankRepository.findByUserCode(0);
			Bank JackPot = bankJackPot.get(0);
			mv.addObject("JP", JackPot);
			mv.setViewName("gametop2");
		} else if (code == 3) {
			mv.setViewName("gametop3");
		} else if (code == 4) {
			mv.setViewName("gametop4");
		} else if (code == 5) {
			mv.setViewName("gametop5");
		}

		return mv;
	}

	//結果表示
	@RequestMapping(value="/result", method=RequestMethod.POST)
	public ModelAndView showResult(
			ModelAndView mv,
			@RequestParam("prize") double prize,
			@RequestParam("code") int code
			) {


		session.removeAttribute("COUNT");

		Optional<Game> game = gameRepository.findById(code);
		Game gameDetail = game.get();
		mv.addObject("game", gameDetail);

		if(code == 2) {
			List<Bank> JP = bankRepository.findByUserCode(0);
			Bank JPbank = JP.get(0);
			Bank newJP = new Bank(JPbank.getCode(), 0, JPbank.getMoney()-(int)prize, 0, 0);
			bankRepository.saveAndFlush(newJP);
		}

		People player = (People) session.getAttribute("login");
		List<Bank> list = bankRepository.findByUserCode(player.getCode());
		Bank bankAccount = list.get(0);

		int WonOrLost = (int)prize - gameDetail.getPrice();
		if(WonOrLost > 0) {
			int won = WonOrLost;
			mv.addObject("message", "勝ち金が"+ won + "円増えました。");

			Bank newMoney = new Bank(bankAccount.getCode(), bankAccount.getUserCode(), bankAccount.getMoney()+(int)prize, bankAccount.getLost(), bankAccount.getWon()+won);
			bankRepository.saveAndFlush(newMoney);
		}
		else if(WonOrLost == 0) {
			mv.addObject("message", "引き分けです。");
		}
		else {
			int lost = WonOrLost;
			mv.addObject("message", "負け金が"+ lost + "円分増えてしまいました。");

			Bank newMoney = new Bank(bankAccount.getCode(), bankAccount.getUserCode(), bankAccount.getMoney()+(int)prize, bankAccount.getLost()+lost, bankAccount.getWon());
			bankRepository.saveAndFlush(newMoney);

			if(code == 4) {
				List<Bank> JP = bankRepository.findByUserCode(0);
				Bank JPbank = JP.get(0);
				Bank newJP = new Bank(JPbank.getCode(), 0, JPbank.getMoney()+(int)prize, 0, 0);
				bankRepository.saveAndFlush(newJP);
			}
		}

		mv.addObject("prize", (int)prize);
		mv.addObject("bankAccount", bankAccount);
		mv.setViewName("result");
		return mv;
	}
}
