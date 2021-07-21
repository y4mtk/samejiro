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
	BankRepository bankRepository;

	@Autowired
	PlayerRankRepository playerRankRepository;


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
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/result", method=RequestMethod.POST)
	public ModelAndView showResult(
			ModelAndView mv,
			@RequestParam("prize") double prize,
			@RequestParam("code") int code
			) {

		session.removeAttribute("COUNT");
		session.removeAttribute("RIGHT");
		session.removeAttribute("slot1");
		session.removeAttribute("slot2");
		session.removeAttribute("slot3");

		Optional<Game> game = gameRepository.findById(code);
		Game gameDetail = game.get();
		mv.addObject("game", gameDetail);

		People player = (People) session.getAttribute("login");
		List<Bank> list1 = bankRepository.findByUserCode(player.getCode());
		Bank bankAccount = list1.get(0);

		PlayerRank addNew = new PlayerRank(player.getName(), (int)prize, gameDetail.getCode());
		playerRankRepository.saveAndFlush(addNew);

		List<PlayerRank> sameGame = playerRankRepository.findByGameCodeOrderByPrizeDesc(code);

		mv.addObject("sameGame", sameGame);

		if(code == 2) {
			List<Bank> JP = bankRepository.findByUserCode(0);
			Bank JPbank = JP.get(0);
			Bank newJP = new Bank(JPbank.getCode(), 0, JPbank.getMoney()-(int)prize, 0, 0);
			bankRepository.saveAndFlush(newJP);
		}

		int WonOrLost = (int)prize - gameDetail.getPrice();

		if(code == 3) {
			List<Integer> list = (List<Integer>) session.getAttribute("list");
			WonOrLost = (int)prize - list.get(3)*100;
		}

		if(WonOrLost > 0) {
			int won = WonOrLost;
			mv.addObject("message", "勝ち金が"+ won + " ｼｬｰｸ増えました。");

			Bank newMoney = new Bank(bankAccount.getCode(), bankAccount.getUserCode(), bankAccount.getMoney()+(int)prize, bankAccount.getLost(), bankAccount.getWon()+won);
			bankRepository.saveAndFlush(newMoney);
		}
		else if(WonOrLost == 0) {
			mv.addObject("message", "引き分けです。");
		}
		else {
			int lost = WonOrLost;
			mv.addObject("message", "負け金が"+ lost + " ｼｬｰｸ分増えてしまいました。");

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
