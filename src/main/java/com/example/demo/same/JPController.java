package com.example.demo.same;

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
	SamejiroRepository samejiroRepository;

	@Autowired
	BankRepository bankRepository;


	//ルール詳細画面表示
	@RequestMapping(value="/challenge", method=RequestMethod.POST)
	public ModelAndView jpchallenge(
			ModelAndView mv,
			@RequestParam("JP") int jp,
			@RequestParam("name") String name
	) {
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
		String slot1 = null;
		String slot2 = null;
		String slot3 = null;

		Random random = new Random();
		int col1 = random.nextInt(3) + 1;
		switch(col1){
		case 1:
			slot1= "サメ太郎";
			break;
		case 2:
			slot1= "サメジロー。";
			break;
		case 3:
			slot1= "サメ子";
			break;
		}

		int col2 = random.nextInt(3) + 1;
		switch(col2){
		case 1:
			slot2= "サメ太郎";
			break;
		case 2:
			slot2= "サメジロー。";
			break;
		case 3:
			slot2= "サメ子";
			break;
		}

		int col3 = random.nextInt(3) + 1;
		switch(col3){
		case 1:
			slot3= "サメ太郎";
			break;
		case 2:
			slot3= "サメジロー。";
			break;
		case 3:
			slot3= "サメ子";
			break;
		}

		mv.addObject("slot1", slot1);
		mv.addObject("slot2", slot2);
		mv.addObject("slot3", slot3);
		mv.addObject("name", name);
		mv.addObject("JP", jp);

		if(slot1.equals("サメジロー。") && slot2.equals("サメジロー。") && slot3.equals("サメジロー。")) {
			boolean samejiroCheck = true;

			mv.addObject("message", "チャレンジ成功！！");
			mv.addObject("samejiroCheck", samejiroCheck);
		}else {
			mv.addObject("message", "チャレンジ失敗……。");
		}

		mv.setViewName("jpchallenge");
		return mv;
	}
}
