package com.example.demo.same;

import java.util.ArrayList;
import java.util.Collections;
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
public class PokerController {

	@Autowired
	HttpSession session;

	@Autowired
	GameRepository gameRepository;

	@Autowired
	SamejiroRepository samejiroRepository;

	@Autowired
	BankRepository bankRepository;


	@RequestMapping(value="/startPoker", method=RequestMethod.POST)
	public ModelAndView card(
			ModelAndView mv,
			@RequestParam("tip")int tip
			) {
		List <Integer> deck = new ArrayList<>(52);

		//デッキをシャッフル
		shuffleDeck(deck);

		//プレイヤーの手札リストを生成
		List <String> player = new ArrayList<>();
		List <String> cpu = new ArrayList<>();

		//プレイヤーに順番に二枚配る
		player.add(toDescription(deck.get(0)));
		cpu.add(toDescription(deck.get(1)));
		player.add(toDescription(deck.get(2)));
		cpu.add(toDescription(deck.get(3)));

		//今デッキの何枚使ったかの記録するカウント
		int deckCount = 4;

		//チップの定義
		int list[] = {tip-10, 100-10, 20, 0, 0};
		//{player, cpu, pot, needtoCall, foldFlag}
		//foldFlagは0:foldしてない、1:playerがfold、2:cpuがfold

		Optional<Game> list2 = gameRepository.findById(2);
		Game game = list2.get();

		mv.addObject("game",game);
		mv.addObject("list",list);
		mv.addObject("playerDeck",player);
		mv.setViewName("poker");
		return mv;
	}

	@RequestMapping(value="/action", method=RequestMethod.POST)
	public ModelAndView checkBet(
			ModelAndView mv,
			@RequestParam(name="check", defaultValue="") String check1,
			@RequestParam("bet") int bet,
			@RequestParam(name="call", defaultValue="") String call1,
			@RequestParam("bet") int raise,
			@RequestParam(name="fold", defaultValue="") String fold1,
			@RequestParam("list") int list[]
			) {

		boolean check = "1".equals(check1);
		boolean call = "1".equals(call1);
		boolean fold = "1".equals(fold1);

		if(list[3] == 0) {
			if(check == true) {
				return cpuAction(mv, list);
			}
			else if(fold == true) {
				list[4] = 1;
				mv.setViewName("pokerResult");
				return mv;
			}

			else if(bet <= list[0]  &&  bet >= list[2]) {
				list[0] -= bet;
				list[2] += bet;
			}
			else {//ベット額が持ってる分を越えてる場合とポットより少ないときは
				mv.addObject("message","ベット額が不正です。正しい値を入力してください");
				mv.setViewName("poker");
				return mv;
			}
		        return cpuAction(mv, list);
		}

		else{
			if(call == true) {
				return cpuAction(mv, list);
			}
			else if(fold == true) {
				list[4] = 1;
				mv.setViewName("pokerResult");
				return mv;
			}

			else if(raise <= list[0]  &&  raise >= list[3]) {
				list[0] -= raise;
				list[2] += raise;
			}
			else {//ベット額が持ってる分を越えてる場合とポットより少ないときは
				mv.addObject("message","レイズ額が不正です。正しい値を入力してください");
				mv.setViewName("poker");
				return mv;
			}
		        return cpuAction(mv, list);
		}

		    }

	public ModelAndView cpuAction(
			ModelAndView mv,
			@RequestParam("list") int list[]) {
		Random rand = new Random();
	    int randomValue = rand.nextInt(100);//乱数を生成

	    if(list[3] == 0) {//check or bet
	        switch(randomValue%2) {
	        case 0://チェックする
	        	mv.addObject("message","CPUはcheckしました");
	            break;
	        case 1://ベット
	            int bet = list[2];//ベット額はポットサイズで固定
	            mv.addObject("message","CPUは"+bet+"$ベットしました。");
	            //手持ちチップを減らし、ポットをふやす、needtoCallも増やす
	            list[1] = list[1] - bet;
	            list[2] = list[2] + bet;
	            list[3] = bet;
	            break;
	        }
	    }else {//call or raise or fold
	            switch(randomValue % 3) {
	            case 0://コールする
	            	mv.addObject("message","CPUはcallしました。");
	                //手持ちチップを減らしポットをふやす。needtocallをリセット
	                list[1] = list[1] - list[3];
	                list[2] = list[2] + list[3];
	                list[3] = 0;
	                break;
	            case 1:
	                int raise = list[3] * 2;
	                if(raise >= list[1]) {
	                    raise = list[1];
	                    mv.addObject("message","CPUはAll-inです。");
	                }else {
	                	mv.addObject("message","CPUは"+raise+"$にレイズしました。");
	                }
	                //needtocall-raise = 次のneedtocall
	                list[1] = list[1] - raise;
	                list[2] = list[2] + raise;
	                list[3] = list[3] - raise;
	                break;
	            case 2:
	            	mv.addObject("message","CPUはfoldしました");
	                list[4] = 2;
	                break;
	            }
	        }
	    mv.setViewName("poker");
		return mv;
		    }


	  public static void shuffleDeck(List<Integer> deck) {
	        // リストに1-52の連番を代入
	        for (int i = 1; i <= 52; i++) {
	            deck.add(i);
	        }
	        // デッキをシャッフル
	        Collections.shuffle(deck);
	    }

	  private static String toSuit(int cardNumber) {
	        switch((cardNumber-1)/13) {
	        case 0:
	        	return "♣";
	        case 1:
	        	return "♦";
	        case 2:
	        	return "♥";
	        case 3:
	        	return "♠";
	        default:
	        	return "例外です";
	        }
	    }

	  private static int toNumber(int cardNumber) {
	        int number = cardNumber % 13;
	        if(number==0) {
	        	number = 13;
	        }
	        return number;
	    }

	  private static String toRank(int number) {
	        switch(number) {
	        case 1:
	        	return "A";
	        case 11:
	        	return "J";
	        case 12:
	        	return "Q";
	        case 13:
	        	return "K";
	        default:
	        	String str = String.valueOf(number);
	        	return str;
	        }
	    }

	  private static String toDescription(int cardNumber) {
	        String rank = toRank(toNumber(cardNumber));
	        String suit = toSuit(cardNumber);

	        return suit + rank;
	        }
	    }


