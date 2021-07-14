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
		People loginee = (People)session.getAttribute("login");

		List<Bank> playerBank = bankRepository.findByUserCode(loginee.getCode());
		Bank bankAccount = playerBank.get(0);

		if(tip > bankAccount.getMoney()) {
			mv.addObject("message", "残高が足りません");
			mv.setViewName("error");
			return mv;
		}
		else if (tip % 100 != 0) {
			mv.addObject("message", "換金エラーです");
			mv.setViewName("error");
			return mv;
		}

		Bank newMoney = new Bank(bankAccount.getCode(), bankAccount.getUserCode(), bankAccount.getMoney()-tip, bankAccount.getLost(), bankAccount.getWon());
		bankRepository.saveAndFlush(newMoney);

		List <Integer> deck = new ArrayList<>(52);
		session.setAttribute("deck",deck);
		List <Integer> list = new ArrayList<>();
		session.setAttribute("list",list);

		//デッキをシャッフル
		shuffleDeck(deck);

		//プレイヤーの手札リストを生成
		List <String> player = new ArrayList<>();
		session.setAttribute("player",player);
		List <String> cpu = new ArrayList<>();
		session.setAttribute("cpu",cpu);

		//プレイヤーに順番に5枚配る
		player.add(toDescription(deck.get(0)));
		cpu.add(toDescription(deck.get(1)));
		player.add(toDescription(deck.get(2)));
		cpu.add(toDescription(deck.get(3)));
		player.add(toDescription(deck.get(4)));
		cpu.add(toDescription(deck.get(5)));
		player.add(toDescription(deck.get(6)));
		cpu.add(toDescription(deck.get(7)));
		player.add(toDescription(deck.get(8)));
		cpu.add(toDescription(deck.get(9)));

		//今デッキの何枚使ったかの記録するカウント
		int deckCount = 10;

		list.add((tip/100)-10);
		list.add(90);
		list.add(20);
		//{player, cpu, pot}

		Optional<Game> list2 = gameRepository.findById(3);
		Game game = list2.get();

		mv.addObject("deckCount",deckCount);
		mv.addObject("game",game);
		mv.addObject("list",list);
		mv.addObject("playerDeck",player);
		mv.setViewName("poker");
		return mv;
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value="/action", method=RequestMethod.POST)
	public ModelAndView action(
			ModelAndView mv,
			@RequestParam("check") int check[],
			@RequestParam("deckCount") int deckCount,
			@RequestParam("bet") int bet
			) {

		List<Integer> deck = (List<Integer>) session.getAttribute("deck");
		List<String> playerDeck = (List<String>) session.getAttribute("player");
		List<String> cpuDeck = (List<String>) session.getAttribute("cpu");
		List<Integer> list = (List<Integer>) session.getAttribute("list");

		if(bet > list.get(0)) {
			mv.addObject("message", "チップ不足です");
			mv.setViewName("poker");
			return mv;
		}

		int CPUbet;
		if(bet > list.get(1)) {
			CPUbet = list.get(1);
		}
		else {
			CPUbet = bet;
		}

		list.set(0, list.get(0)-bet);
		list.set(1, list.get(1)-CPUbet);
	    list.set(2, list.get(2)+bet+CPUbet);

	    ArrayList<Integer> checked = new ArrayList<>();
	    for(int i : check) {
	    	checked.add(i);
	    }
		if(checked.contains(6)) {

		}
		else {
			if (checked.contains(1)) {
				playerDeck.set(0, toDescription(deck.get(deckCount)));
				deckCount += 1;
			}
			if (checked.contains(2)) {
				playerDeck.set(1, toDescription(deck.get(deckCount)));
				deckCount += 1;
			}
			if (checked.contains(3)) {
				playerDeck.set(2, toDescription(deck.get(deckCount)));
				deckCount += 1;
			}
			if (checked.contains(4)) {
				playerDeck.set(3, toDescription(deck.get(deckCount)));
				deckCount += 1;
			}
			if (checked.contains(5)) {
				playerDeck.set(4, toDescription(deck.get(deckCount)));
				deckCount += 1;
			}

//			for(int i=0; i<check.length-1; i++) {
//				playerDeck.set(i, toDescription(deck.get(deckCount + i)));
//				deckCount+=i+1;
//			}
		}


		Random rand = new Random();
	    int randomValue = rand.nextInt(5); //CPU何枚交換する？
	    switch(randomValue){
	    case 0:
	    	cpuDeck.set(0, toDescription(deck.get(deckCount)));
	    	deckCount+=1;
	    	break;
	    case 1:
	    	cpuDeck.set(0, toDescription(deck.get(deckCount)));
	    	cpuDeck.set(1, toDescription(deck.get(deckCount+1)));
	    	deckCount+=2;
	    	break;
	    case 2:
	    	cpuDeck.set(0, toDescription(deck.get(deckCount)));
	    	cpuDeck.set(1, toDescription(deck.get(deckCount+1)));
	    	cpuDeck.set(2, toDescription(deck.get(deckCount+2)));
	    	deckCount+=3;
	    	break;
	    case 3:
	    	cpuDeck.set(0, toDescription(deck.get(deckCount)));
	    	cpuDeck.set(1, toDescription(deck.get(deckCount+1)));
	    	cpuDeck.set(2, toDescription(deck.get(deckCount+2)));
	    	cpuDeck.set(3, toDescription(deck.get(deckCount+3)));
	    	deckCount+=4;
	    	break;
	    case 4:
	    	cpuDeck.set(0, toDescription(deck.get(deckCount)));
	    	cpuDeck.set(1, toDescription(deck.get(deckCount+1)));
	    	cpuDeck.set(2, toDescription(deck.get(deckCount+2)));
	    	cpuDeck.set(3, toDescription(deck.get(deckCount+3)));
	    	cpuDeck.set(4, toDescription(deck.get(deckCount+4)));
	    	break;
	    case 5:
	    	break;
	    }

	    Optional<Game> list2 = gameRepository.findById(3);
		Game game = list2.get();

		mv.addObject("game",game);
		mv.addObject("list",list);
		mv.addObject("playerDeck",playerDeck);
		mv.addObject("cpuDeck",cpuDeck);
		mv.setViewName("pokerShowDown");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/pokerResult", method=RequestMethod.POST)
	public ModelAndView pokerResult(ModelAndView mv, @RequestParam("bet") int bet) {


		List<String> playerDeck = (List<String>) session.getAttribute("player");
		List<String> cpuDeck = (List<String>) session.getAttribute("cpu");
		List<Integer> list = (List<Integer>) session.getAttribute("list");

		if(bet > list.get(0)) {
			mv.addObject("message", "チップ不足です");
			mv.setViewName("pokerShowDown");
			return mv;
		}

		int CPUbet;
		if(bet > list.get(1)) {
			CPUbet = list.get(1);
		}
		else {
			CPUbet = bet;
		}

		list.set(0, list.get(0)-bet);
		list.set(1, list.get(1)-CPUbet);
	    list.set(2, list.get(2)+bet+CPUbet);

		int rank;

		List<String> suit = new ArrayList<>();
		for(int u=0; u<playerDeck.size(); u++) {
			suit.add(playerDeck.get(u).substring(0, 1));
		}

		List<Integer> number = new ArrayList<>();
		for(int u=0; u<playerDeck.size(); u++) {
			if(playerDeck.get(u).substring(1).equals("J")) {
				number.add(11);
			}
			else if(playerDeck.get(u).substring(1).equals("Q")) {
				number.add(12);
			}
			else if(playerDeck.get(u).substring(1).equals("K")) {
				number.add(13);
			}
			else if(playerDeck.get(u).substring(1).equals("A")) {
				number.add(14);
			}
			else {
				number.add(Integer.parseInt(playerDeck.get(u).substring(1)));
			}
		}

		Collections.sort(number);

		int number_max =0;

		for(int i=0;i<number.size();i++) {
			for(int j=i+1; j<number.size(); j++) {
				if(number.get(i)==number.get(j)) {
					number_max ++;
				}
			}
		}

		boolean flush = false;
		if(suit.get(0).equals(suit.get(1))&&
				suit.get(1).equals(suit.get(2))&&
				suit.get(2).equals(suit.get(3))&&
				suit.get(3).equals(suit.get(4))) {
			flush = true;
		}

		boolean straight = false;
		if (number.get(0) == 14) {
			number.set(0, 1);

			if (number.get(0) + 1 == number.get(1) &&
					number.get(1) + 1 == number.get(2) &&
					number.get(2) + 1 == number.get(3) &&
					number.get(3) + 1 == number.get(4)) {
				straight = true;
				number.set(0, 14);
			}
		}
		else if(number.get(0)+1 == number.get(1)&&
				number.get(1)+1 == number.get(2)&&
				number.get(2)+1 == number.get(3)&&
				number.get(3)+1 == number.get(4)) {
			straight = true;
		}

		if(flush == true) {
			//スーツ揃い
			if(straight == true) {
				if(number.get(4)==13) {
					rank = 1; //ロイヤルストレートフラッシュ
				}
				rank = 2; //ストレートフラッシュ
			}
			rank = 5; //フラッシュ
		}
		else if(straight == true) {
			rank = 6; //ストレート
		}
		else if(number_max==6) {
			//ペア系
			rank = 3; //4カード
		}
		else if(number_max==4) {
			rank = 4; //フルハウス
		}
		else if (number_max==3) {
			rank = 7; //3カード
		}
		else if(number_max==2) {
			rank = 8; //ツーペア
		}
		else if (number_max==1) {
			rank = 9; //ワンペア
		}
		else {

			rank = 10; //ハイカード
		}

		//ここからコンピュータの判定
		int rankCPU;

		List<String> suitCPU = new ArrayList<>();
		for(int u=0; u<cpuDeck.size(); u++) {
			suitCPU.add(cpuDeck.get(u).substring(0, 1));
		}

		List<Integer> numberCPU = new ArrayList<>();
		for(int u=0; u<cpuDeck.size(); u++) {
			if(cpuDeck.get(u).substring(1).equals("J")) {
				numberCPU.add(11);
			}
			else if(cpuDeck.get(u).substring(1).equals("Q")) {
				numberCPU.add(12);
			}
			else if(cpuDeck.get(u).substring(1).equals("K")) {
				numberCPU.add(13);
			}
			else if(cpuDeck.get(u).substring(1).equals("A")) {
				numberCPU.add(14);
			}
			else {
				numberCPU.add(Integer.parseInt(cpuDeck.get(u).substring(1)));
			}
		}
		Collections.sort(numberCPU);

		int number_maxCPU =0;

		for(int i=0;i<numberCPU.size();i++) {
			for(int j=i+1; j<numberCPU.size(); j++) {
				if(numberCPU.get(i)==numberCPU.get(j)) {
					number_maxCPU ++;
				}
			}
		}

		boolean flushCPU = false;
		if(suitCPU.get(0).equals(suitCPU.get(1))&&
				suitCPU.get(1).equals(suitCPU.get(2))&&
				suitCPU.get(2).equals(suitCPU.get(3))&&
				suitCPU.get(3).equals(suitCPU.get(4))) {
			flushCPU = true;
		}

		boolean straightCPU = false;
		if (numberCPU.get(0) == 14) {
			numberCPU.set(0, 1);

			if (numberCPU.get(0) + 1 == numberCPU.get(1) &&
					numberCPU.get(1) + 1 == numberCPU.get(2) &&
					numberCPU.get(2) + 1 == numberCPU.get(3) &&
					numberCPU.get(3) + 1 == numberCPU.get(4)) {
				straightCPU = true;
				numberCPU.set(0, 14);
			}
		} else if (numberCPU.get(0) + 1 == numberCPU.get(1) &&
				numberCPU.get(1) + 1 == numberCPU.get(2) &&
				numberCPU.get(2) + 1 == numberCPU.get(3) &&
				numberCPU.get(3) + 1 == numberCPU.get(4)) {
			straightCPU = true;
		}


		if(flushCPU == true) {
			//スーツ揃い
			if(straightCPU == true) {
				if(numberCPU.get(4)==13) {
					rankCPU = 1; //ロイヤルストレートフラッシュ
				}
				rankCPU = 2; //ストレートフラッシュ
			}
			rankCPU = 5; //フラッシュ
		}
		else if(straightCPU == true) {
			rankCPU = 6; //ストレート
		}
		else if(number_maxCPU==6) {
			//ペア系
			rankCPU = 3; //4カード
		}
		else if(number_maxCPU==4) {
			rankCPU = 4; //フルハウス
		}
		else if (number_maxCPU==3) {
			rankCPU = 7; //3カード
		}
		else if(number_maxCPU==2) {
			rankCPU = 8; //ツーペア
		}
		else if (number_maxCPU==1) {
			rankCPU = 9; //ワンペア
		}
		else {

			rankCPU = 10; //ハイカード
		}

		String winner = null;
		String hand = null;
		long prize = 0;


		switch(rank) {
		case 1:
			hand = "ROYAL STRAIGHT FLUSH";
			prize = 1000 * list.get(2);
			break;
		case 2:
			hand = "STRAIGHT FLUSH";
			prize = 100 * list.get(2);
			break;
		case 3:
			hand = "FOUR OF A KIND";
			prize = 50 * list.get(2);
			break;
		case 4:
			hand = "FULL HOUSE";
			prize = 25 * list.get(2);
			break;
		case 5:
			hand = "FLUSH";
			prize = 20 * list.get(2);
			break;
		case 6:
			hand = "STRAIGHT";
			prize = 15 * list.get(2);
			break;
		case 7:
			hand = "THREE OF A KIND";
			prize = 10 * list.get(2);
			break;
		case 8:
			hand = "TWO PAIR";
			prize = 5 * list.get(2);
			break;
		case 9:
			hand = "ONE PIAR";
			prize = 2 * list.get(2);
			break;
		case 10:
			hand = "HIGH CARD";
			prize = list.get(2);
			break;
		}

		if(rank<rankCPU) {
			winner = "あなたの勝ちです";
			switch(rank) {
			case 1:
				hand = "ROYAL STRAIGHT FLUSH";
				prize = 1000 * list.get(2);
				break;
			case 2:
				hand = "STRAIGHT FLUSH";
				prize = 100 * list.get(2);
				break;
			case 3:
				hand = "FOUR OF A KIND";
				prize = 50 * list.get(2);
				break;
			case 4:
				hand = "FULL HOUSE";
				prize = 25 * list.get(2);
				break;
			case 5:
				hand = "FLUSH";
				prize = 20 * list.get(2);
				break;
			case 6:
				hand = "STRAIGHT";
				prize = 15 * list.get(2);
				break;
			case 7:
				hand = "THREE OF A KIND";
				prize = 10 * list.get(2);
				break;
			case 8:
				hand = "TWO PAIR";
				prize = 5 * list.get(2);
				break;
			case 9:
				hand = "ONE PIAR";
				prize = 2 * list.get(2);
				break;
			case 10:
				hand = "HIGH CARD";
				prize = list.get(2);
				break;
			}

		}
		else if(rank==rankCPU) {
			winner = "引き分けです";
			prize = list.get(2)/2;
		}
		else {
			winner = "CPUの勝ちです";
			prize =0;
		}

		String handCPU = null;
		switch(rankCPU) {
		case 1:
			handCPU = "ROYAL STRAIGHT FLUSH";
			break;
		case 2:
			handCPU = "STRAIGHT FLUSH";
			break;
		case 3:
			handCPU = "FOUR OF A KIND";
			break;
		case 4:
			handCPU = "FULL HOUSE";
			break;
		case 5:
			handCPU = "FLUSH";
			break;
		case 6:
			handCPU = "STRAIGHT";
			break;
		case 7:
			handCPU = "THREE OF A KIND";
			break;
		case 8:
			handCPU = "TWO PAIR";
			break;
		case 9:
			handCPU = "ONE PIAR";
			break;
		case 10:
			handCPU = "HIGH CARD";
			break;
		}

		Optional<Game> list2 = gameRepository.findById(2);
		Game game = list2.get();

		People loginee = (People)session.getAttribute("login");
		List<Bank> playerBank = bankRepository.findByUserCode(loginee.getCode());
		Bank bankAccount = playerBank.get(0);
		Bank newMoney = new Bank(bankAccount.getCode(), bankAccount.getUserCode(), bankAccount.getMoney()+(list.get(0)*100), bankAccount.getLost(), bankAccount.getWon());
		bankRepository.saveAndFlush(newMoney);

		mv.addObject("list", list);
		mv.addObject("game",game);
		mv.addObject("prize", prize);
		mv.addObject("winner", winner);
		mv.addObject("hand", hand);
		mv.addObject("handCPU", handCPU);
		mv.addObject("cpuDeck", cpuDeck);
		mv.addObject("playerDeck", playerDeck);
		mv.setViewName("pokerResult");
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