package com.example.demo.same;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BJController {

	@Autowired
	HttpSession session;

	@Autowired
	GameRepository gameRepository;

	@Autowired
	SamejiroRepository samejiroRepository;

	@Autowired
	BankRepository bankRepository;


	@RequestMapping(value="/startBlackJack", method=RequestMethod.POST)
	public ModelAndView bj(ModelAndView mv) {
		People loginee = (People)session.getAttribute("login");

		List<Bank> playerBank = bankRepository.findByUserCode(loginee.getCode());
		Bank bankAccount = playerBank.get(0);

		List <Integer> list = new ArrayList<>();
		session.setAttribute("list",list);

		Optional<Game> list2 = gameRepository.findById(4);
		Game game = list2.get();

		if(game.getPrice() > bankAccount.getMoney()) {
			mv.addObject("message", "残高が足りません");
			mv.setViewName("error");
			return mv;
		}

		Bank newMoney = new Bank(bankAccount.getCode(), bankAccount.getUserCode(), bankAccount.getMoney()-game.getPrice(), bankAccount.getLost(), bankAccount.getWon());
		bankRepository.saveAndFlush(newMoney);

		List <Integer> deck = new ArrayList<>(52);
		session.setAttribute("deck",deck);

		//デッキをシャッフル
		shuffleDeck(deck);

		//プレイヤーの手札リストを生成
		List <String> player = new ArrayList<>();
		session.setAttribute("player",player);
		List <String> dealer = new ArrayList<>();
		session.setAttribute("dealer",dealer);

		//プレイヤーに順番に2枚づつ配る
		player.add(toDescription(deck.get(0)));
		dealer.add(toDescription(deck.get(1)));
		player.add(toDescription(deck.get(2)));
		dealer.add(toDescription(deck.get(3)));

		//手札の合計値を求める
		List<Integer> number = new ArrayList<>();
		session.setAttribute("number",number);
		int handTotal = 0;
		for(int u=0; u<player.size(); u++) {
			if(player.get(u).substring(1).equals("J")) {
				number.add(10);
			}
			else if(player.get(u).substring(1).equals("Q")) {
				number.add(10);
			}
			else if(player.get(u).substring(1).equals("K")) {
				number.add(10);
			}
			else if(player.get(u).substring(1).equals("A")) {
				number.add(1);
			}
			else {
				number.add(Integer.parseInt(player.get(u).substring(1)));
			}
			handTotal += number.get(u);
		}

		List<Integer> numberDealer = new ArrayList<>();
		session.setAttribute("numberDealer",numberDealer);
		int handTotalDealer = 0;
		for(int u=0; u<dealer.size(); u++) {
			if(dealer.get(u).substring(1).equals("J")) {
				numberDealer.add(10);
			}
			else if(dealer.get(u).substring(1).equals("Q")) {
				numberDealer.add(10);
			}
			else if(dealer.get(u).substring(1).equals("K")) {
				numberDealer.add(10);
			}
			else if(dealer.get(u).substring(1).equals("A")) {
				numberDealer.add(1);
			}
			else {
				numberDealer.add(Integer.parseInt(dealer.get(u).substring(1)));
			}
			handTotalDealer += numberDealer.get(u);
		}

		//手札の情報を記録するリスト
		list.add(4);
		list.add(2);
		list.add(2);
		list.add(handTotal);
		list.add(handTotalDealer);
		//{deckCount, playerCount, dealerCount, handTotal, handTotalDealer}

		mv.addObject("list",list);
		mv.addObject("game",game);
		mv.addObject("playerDeck",player);
		mv.addObject("dealerDeck",dealer);
		mv.setViewName("blackjack");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/actionContinue", method=RequestMethod.POST)
	public ModelAndView actionContinue(ModelAndView mv) {

		List<Integer> deck = (List<Integer>) session.getAttribute("deck");
		List<String> playerDeck = (List<String>) session.getAttribute("player");
		List<String> dealerDeck = (List<String>) session.getAttribute("dealer");
		List<Integer> list = (List<Integer>) session.getAttribute("list");
		List<Integer> number = (List<Integer>) session.getAttribute("number");
		List<Integer> numberDealer = (List<Integer>) session.getAttribute("numberDealer");
		Optional<Game> list2 = gameRepository.findById(4);
		Game game = list2.get();


		int deckCount = list.get(0);
		playerDeck.add(toDescription(deck.get(deckCount)));
		list.set(0, deckCount+1); //deckCountに+1
		list.set(1, list.get(1)+1); //playerCountに+1

		int handTotal = 0;
		for(int u=0; u<playerDeck.size(); u++) {
			if(playerDeck.get(u).substring(1).equals("J")) {
				number.add(10);
			}
			else if(playerDeck.get(u).substring(1).equals("Q")) {
				number.add(10);
			}
			else if(playerDeck.get(u).substring(1).equals("K")) {
				number.add(10);
			}
			else if(playerDeck.get(u).substring(1).equals("A")) {
				number.add(1);
			}
			else {
				number.add(Integer.parseInt(playerDeck.get(u).substring(1)));
			}
			handTotal += number.get(u);
		}
		list.set(3, handTotal);

		if(busted(handTotal)) {
			mv.addObject("message", "バーストしてしまいました");

			list.set(3, 0);

			mv.addObject("list",list);
			mv.addObject("game",game);
			mv.addObject("playerDeck",playerDeck);
			mv.addObject("dealerDeck",dealerDeck);
			mv.setViewName("blackjackResult");
			return mv;
		}

		if (list.get(4) <= 17) {
			dealerDeck.add(toDescription(deck.get(list.get(0))));
			list.set(0, list.get(0)+1); //deckCountに+1
			list.set(2, list.get(2)+1); //dealerCountに+1

			int handTotalDealer = 0;
			for (int u = 0; u < dealerDeck.size(); u++) {
				if (dealerDeck.get(u).substring(1).equals("J")) {
					numberDealer.add(10);
				} else if (dealerDeck.get(u).substring(1).equals("Q")) {
					numberDealer.add(10);
				} else if (dealerDeck.get(u).substring(1).equals("K")) {
					numberDealer.add(10);
				} else if (dealerDeck.get(u).substring(1).equals("A")) {
					numberDealer.add(1);
				} else {
					numberDealer.add(Integer.parseInt(dealerDeck.get(u).substring(1)));
				}
				handTotalDealer += numberDealer.get(u);
			}
			list.set(4, handTotalDealer);

			if (busted(handTotalDealer)) {
				mv.addObject("message", "ディーラーがバーストしました");
				list.set(4, 0);

				mv.addObject("list",list);
				mv.addObject("game",game);
				mv.addObject("playerDeck",playerDeck);
				mv.addObject("dealerDeck",dealerDeck);
				mv.setViewName("blackjackResult");
				return mv;
			}
		}
		else {
			mv.addObject("list",list);
			mv.addObject("game",game);
			mv.addObject("playerDeck",playerDeck);
			mv.addObject("dealerDeck",dealerDeck);
			mv.setViewName("blackjackResult");
			return mv;
		}

		mv.addObject("list",list);
		mv.addObject("game",game);
		mv.addObject("playerDeck",playerDeck);
		mv.addObject("dealerDeck",dealerDeck);
		mv.setViewName("blackjack");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/actionEnd", method=RequestMethod.POST)
	public ModelAndView actionEnd(ModelAndView mv) {

		List<Integer> deck = (List<Integer>) session.getAttribute("deck");
		List<String> playerDeck = (List<String>) session.getAttribute("player");
		List<String> dealerDeck = (List<String>) session.getAttribute("dealer");
		List<Integer> list = (List<Integer>) session.getAttribute("list");
		List<Integer> numberDealer = (List<Integer>) session.getAttribute("numberDealer");
		Optional<Game> list2 = gameRepository.findById(4);
		Game game = list2.get();

		if (list.get(4) <= 17) {
			dealerDeck.add(toDescription(deck.get(list.get(0))));
			list.set(0, list.get(0)+1); //deckCountに+1
			list.set(1, list.get(2)+1); //dealerCountに+1

			int handTotalDealer = 0;
			for (int u = 0; u < dealerDeck.size(); u++) {
				if (dealerDeck.get(u).substring(1).equals("J")) {
					numberDealer.add(10);
				} else if (dealerDeck.get(u).substring(1).equals("Q")) {
					numberDealer.add(10);
				} else if (dealerDeck.get(u).substring(1).equals("K")) {
					numberDealer.add(10);
				} else if (dealerDeck.get(u).substring(1).equals("A")) {
					numberDealer.add(1);
				} else {
					numberDealer.add(Integer.parseInt(dealerDeck.get(u).substring(1)));
				}
				handTotalDealer += numberDealer.get(u);
			}
			list.set(4, handTotalDealer);

			if (busted(handTotalDealer)) {
				mv.addObject("message", "ディーラーがバーストしました");
				list.set(4, 0);

				mv.addObject("list",list);
				mv.addObject("game",game);
				mv.addObject("playerDeck",playerDeck);
				mv.addObject("dealerDeck",dealerDeck);
				mv.setViewName("blackjackResult");
				return mv;
			}

			mv.addObject("list",list);
			mv.addObject("game",game);
			mv.addObject("playerDeck",playerDeck);
			mv.addObject("dealerDeck",dealerDeck);
			mv.setViewName("blackjack");
			return mv;
		}
		else {
			mv.addObject("list",list);
			mv.addObject("game",game);
			mv.addObject("playerDeck",playerDeck);
			mv.addObject("dealerDeck",dealerDeck);
			mv.setViewName("blackjackResult");
			return mv;
		}
	}


	private static boolean busted(int point){
		if(point <= 21) {
			return false;
		}
		else {
			return true;
		}
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