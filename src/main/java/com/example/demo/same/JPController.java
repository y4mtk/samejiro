package com.example.demo.same;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JPController {

	@Autowired
	HttpSession session;

	@Autowired
	GameRepository gameRepository;

	@Autowired
	BankRepository bankRepository;


	@RequestMapping(value="/challenge", method=RequestMethod.POST)
	public ModelAndView jpchallenge(
			ModelAndView mv,
			@RequestParam("JP") int jp,
			@RequestParam("name") String name
	) {
		People player = (People)session.getAttribute("login");

		List<Bank> list = bankRepository.findByUserCode(player.getCode());
		Bank bankAccount = list.get(0);

		Optional<Game> list2 = gameRepository.findById(2);
		Game game = list2.get();

		List<Bank> JP = bankRepository.findByUserCode(0);
		Bank JPbank = JP.get(0);

		if(game.getPrice() > bankAccount.getMoney()) {
			mv.addObject("message", "残高が足りません");
			mv.setViewName("error");
			return mv;
		}

		Bank newMoney = new Bank(bankAccount.getCode(), bankAccount.getUserCode(), bankAccount.getMoney()-game.getPrice(), bankAccount.getLost(), bankAccount.getWon());
		bankRepository.saveAndFlush(newMoney);

		Bank newJP = new Bank(JPbank.getCode(), 0, JPbank.getMoney()+game.getPrice(), 0, 0);
		bankRepository.saveAndFlush(newJP);

		mv.addObject("name", name);
		mv.addObject("JP", jp);
		mv.setViewName("jpchallenge");
		return mv;
	}

	@RequestMapping(value="/challenge/start", method=RequestMethod.POST)
	public ModelAndView challenge(
			ModelAndView mv,
			@RequestParam("JP") int jp,
			@RequestParam("name") String name
	) {
		Integer count = (Integer)session.getAttribute("COUNT");
		if(count == null) {
			count = 0;
		}
		count++;

		if(count >=4) {
			//セッションオブジェクトを消滅
			session.removeAttribute("COUNT");
			session.removeAttribute("slot1");
			session.removeAttribute("slot2");
			session.removeAttribute("slot3");
		}

		session.setAttribute("COUNT", count);

//		String slot1 = null;
//		String slot2 = null;
//		String slot3 = null;

		String slot1 = (String) session.getAttribute("slot1");
		String slot2 = (String) session.getAttribute("slot2");
		String slot3 = (String) session.getAttribute("slot3");

		Random random = new Random();

		if (count == 1) {
			int col1 = random.nextInt(3) + 1;
			switch (col1) {
			case 1:
				slot1 = "サメ太郎";
				break;
			case 2:
				slot1 = "サメジロー。";
				break;
			case 3:
				slot1 = "サメ子";
				break;
			}
		}

		if (count == 2) {
			int col2 = random.nextInt(3) + 1;
			switch (col2) {
			case 1:
				slot2 = "サメ太郎";
				break;
			case 2:
				slot2 = "サメジロー。";
				break;
			case 3:
				slot2 = "サメ子";
				break;
			}
		}

		if (count == 3) {
			int col3 = random.nextInt(3) + 1;
			switch (col3) {
			case 1:
				slot3 = "サメ太郎";
				break;
			case 2:
				slot3 = "サメジロー。";
				break;
			case 3:
				slot3 = "サメ子";
				break;
			}
		}

		session.setAttribute("slot1", slot1);
		session.setAttribute("slot2", slot2);
		session.setAttribute("slot3", slot3);
//		mv.addObject("slot1", slot1);
//		mv.addObject("slot2", slot2);
//		mv.addObject("slot3", slot3);
		mv.addObject("name", name);
		mv.addObject("JP", jp);

		if (count == 3) {
			if (slot1.equals(slot2) && slot1.equals(slot3) && slot2.equals(slot3)) {
				boolean samejiroCheck = true;

				mv.addObject("message", "チャレンジ成功！！");
				mv.addObject("samejiroCheck", samejiroCheck);
			} else {
				boolean samejiroCheck = false;
				mv.addObject("message", "チャレンジ失敗……。");
				mv.addObject("samejiroCheck", samejiroCheck);
			}
		}
		mv.setViewName("jpchallenge");
		return mv;
	}

	@RequestMapping(value="/getPrize", method=RequestMethod.POST)
	public ModelAndView getPrize(ModelAndView mv) {

		List<Bank> JP = bankRepository.findByUserCode(0);
		Bank JPbank = JP.get(0);

		Random random = new Random();
		int r = random.nextInt(3) + 1;
		double prizeR = 0;

		switch(r){
		case 1:
			prizeR = 0.1;
			break;
		case 2:
			prizeR = 0.01;
			break;
		case 3:
			prizeR = 0.001;
			break;
		}

		Optional<Game> list2 = gameRepository.findById(2);
		Game game = list2.get();

		double prize = prizeR * JPbank.getMoney();

		boolean check = true;

		mv.addObject("check", check);
		mv.addObject("prizeR", prizeR);
		mv.addObject("prize", prize);
		mv.addObject("JP", JPbank.getMoney());
		mv.addObject("name", game.getName());
		mv.setViewName("jackpot");
		return mv;
	}

	@RequestMapping(value="/jackpot", method=RequestMethod.POST)
	public ModelAndView jackpot(
			ModelAndView mv,
			@RequestParam("JP") int jp,
			@RequestParam("name") String name
			) {
		boolean check = false;

		mv.addObject("check", check);
		mv.addObject("JP", jp);
		mv.addObject("name", name);
		mv.setViewName("jackpot");
		return mv;
	}
}
