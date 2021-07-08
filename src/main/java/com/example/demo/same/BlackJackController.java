package com.example.demo.same;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BlackJackController {

	@Autowired
	HttpSession session;

	//http://localhost:8080/blackjack
	@RequestMapping ("/blackjack")
	public ModelAndView blackjack(ModelAndView mv) {
		mv.setViewName("blackjack");
		return mv;
	}

	@PostMapping("/blackjack")
	public ModelAndView bjposttop(ModelAndView mv) {

		//1回目　ユーザ側カードランダム選択
		Random r = new Random();
		int trump = r.nextInt(13) + 1;
		Integer card;

		if(trump == 1) {
			card = 1;
		} else if(trump == 2) {
			card = 2;
		} else if (trump == 3) {
			card = 3;
		} else if(trump == 4) {
			card = 4;
		} else if (trump == 5) {
			card = 5;
		} else if(trump == 6) {
			card = 6;
		} else if (trump == 7) {
			card = 7;
		} else if(trump == 8) {
			card = 8;
		} else if (trump == 9) {
			card = 9;
		} else if(trump == 10) {
			card = 10;
		} else if (trump == 11) {
			card = 10;
		} else if(trump == 12) {
			card = 10;
		} else  {
			card = 10;
		}

//---------------------------------------------------------------//

		//１回目　ディーラー側カードランダム選択
		Random pr = new Random();
		int ptrump = pr.nextInt(13) + 1;
		Integer pcard;

		if(ptrump == 1) {
			pcard = 1;
		} else if(ptrump == 2) {
			pcard = 2;
		} else if (ptrump == 3) {
			pcard = 3;
		} else if(ptrump == 4) {
			pcard = 4;
		} else if (ptrump == 5) {
			pcard = 5;
		} else if(ptrump == 6) {
			pcard = 6;
		} else if (ptrump == 7) {
			pcard = 7;
		} else if(ptrump == 8) {
			pcard = 8;
		} else if (ptrump == 9) {
			pcard = 9;
		} else if(ptrump == 10) {
			pcard = 10;
		} else if (ptrump == 11) {
			pcard = 10;
		} else if(ptrump == 12) {
			pcard = 10;
		} else  {
			pcard = 10;
		}

		@SuppressWarnings("unchecked")
		List<BlackJack> blackjack =(List<BlackJack>)session.getAttribute("BlackJack");
		//セッションスコープに何も設定させていなければ設定
		if(blackjack == null) {
			blackjack = new ArrayList<>();
			session.setAttribute("BlackJack", blackjack);
		}

		//カード情報をnewにしてリストに追加
		BlackJack b = new BlackJack();
		b.setCard(card);
		b.setPcard(pcard);

		//1つの情報
		blackjack.add(b);

		mv.addObject("blackjack",blackjack);

		mv.addObject("card",card);
		mv.addObject("pcard",pcard);

		//１回目の結果表示　もう一度引く画面
		mv.setViewName("blackjack2turn");

		return  mv;
	}


}
