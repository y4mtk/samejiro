package com.example.demo.same;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class AccountController {

	@Autowired
	private HttpSession session;

	@Autowired
	PeopleRepository peopleRepository;

	@Autowired
	SamejiroRepository samejiroRepository;

	@Autowired
	BankRepository bankRepository;

	//http://localhost:8080/
	//index.html(トップページ)を表示する
	@RequestMapping ("/")
	public ModelAndView top(ModelAndView mv) {
		mv.setViewName("index");
		return mv;
	}

	//http://localhost:8080/login
		@RequestMapping("/login")
		public ModelAndView login(ModelAndView mv) {

			List<Samejiro> list = samejiroRepository.findAll();
			int chatNum = list.size();

			Random random = new Random();
			int chat = random.nextInt(chatNum) + 2;

			Optional<Samejiro> list2 = samejiroRepository.findById(chat);
			Samejiro Today = list2.get();

			mv.addObject("samejiroToday",Today.getChat());

			mv.setViewName("login"); //フォワード先
			return mv;
	}

	@PostMapping("/login")
	public ModelAndView log(
			@RequestParam("userid") String userid,
			@RequestParam("password") String password,
			ModelAndView mv) {

		if (userid.isEmpty() ||password.isEmpty()) {// 名前が空の場合にエラーとする
			mv.addObject("message", "未入力の項目があります");
			mv.setViewName("login");
		}else {
			List<People>user = peopleRepository.findByUseridAndPassword(userid, password);

			if(user.size()==0) {
				List<Samejiro> list = samejiroRepository.findAll();
				int chatNum = list.size();

				Random random = new Random();
				int chat = random.nextInt(chatNum) + 2;

				Optional<Samejiro> list2 = samejiroRepository.findById(chat);
				Samejiro Today = list2.get();

				mv.addObject("samejiroToday",Today.getChat());
				mv.addObject("miss","ユーザIDが一致しませんでした");
				mv.setViewName("login");
			}
			else {
				People login = user.get(0);
				session.setAttribute("login", login);
				Optional<Samejiro> samejiro = samejiroRepository.findById(1);
				Samejiro selectSamejiro = samejiro.get();

				mv.addObject("selectChat", selectSamejiro.getChat());
				mv.setViewName("select");
			}
	 }
		return mv;
	}

	//http://localhost:8080/signup
	@RequestMapping("/signup")
		public ModelAndView signup(ModelAndView mv) {
		List<Samejiro> list = samejiroRepository.findAll();
		int chatNum = list.size();

		Random random = new Random();
		int chat = random.nextInt(chatNum) + 2;

		Optional<Samejiro> list2 = samejiroRepository.findById(chat);
		Samejiro Today = list2.get();

		mv.addObject("samejiroToday",Today.getChat());
		mv.setViewName("signup"); //フォワード先
		return mv;
	}

	@PostMapping("/signup")
		public ModelAndView sign(
			@RequestParam("userid") String userid,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			ModelAndView mv) {
		List<Samejiro> list = samejiroRepository.findAll();
		int chatNum = list.size();

		Random random = new Random();
		int chat = random.nextInt(chatNum) + 2;

		Optional<Samejiro> list2 = samejiroRepository.findById(chat);
		Samejiro Today = list2.get();
		mv.addObject("samejiroToday",Today.getChat());


			//未入力項目
			if (userid.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
				mv.addObject("message","未入力の項目があります");

				mv.setViewName("signup");

				return mv;
			}


			//重複しているとき
			List<People> user = peopleRepository.findByEmail(email);
			if(user.size()!=0) {
				mv.addObject("Already1","登録済みのメールアドレスです");
				mv.setViewName("signup");
				return mv;
			}

			List<People> user1 = peopleRepository.findByUserid(userid);
			if(user1.size()!=0) {
				mv.addObject("Already","登録済みのユーザIDです");
				mv.setViewName("signup");
				return mv;
			}

				People people = new People(userid,name,email,password);
				peopleRepository.saveAndFlush(people); //登録完了

				//Bankの初期値30000,0,0とPeopleのコードを紐づける
				int user_code = people.getCode();

				Bank bank = new Bank(user_code, 30000, 0, 0);
				bankRepository.saveAndFlush(bank);

				mv.addObject("Registration", "登録が完了しました");
				mv.setViewName("login"); //フォワード先

		return mv;

	}

	@GetMapping("/remind")
	public ModelAndView remind(ModelAndView mv) {
		mv.setViewName("remind");
		return mv;
	}

	@RequestMapping(value="/remind", params="id", method=RequestMethod.POST)
	public ModelAndView remindID(
			ModelAndView mv,
			@RequestParam("address")String address
			) {
		if(address.isEmpty()) {
			mv.addObject("message", "入力してください");
		}
		else {
			List<People> user = peopleRepository.findByEmail(address);

			if (user.size() == 0) {
				mv.addObject("message", "登録されていないメールアドレスです");
			}
			else {
				mv.addObject("userID", user.get(0).getUserid());
			}
		}
		mv.setViewName("remind");
		return mv;
	}

	@RequestMapping(value="/remind", params="pass", method=RequestMethod.POST)
	public ModelAndView remindPassword(
			ModelAndView mv,
			@RequestParam("address")String address
			) {
		if(address.isEmpty()) {
			mv.addObject("message", "入力してください");
		}
		else {
			List<People> user = peopleRepository.findByEmail(address);

			if (user.size() == 0) {
				mv.addObject("message", "登録されていないメールアドレスです");
			}

			mv.addObject("password", user.get(0).getPassword());
		}
		mv.setViewName("remind");
		return mv;
	}

	//ログアウト
	@RequestMapping("/logout")
	public ModelAndView out(ModelAndView mv) {
	session.removeAttribute("login");

	mv.setViewName("index"); //フォワード先
	return mv;

	}

}
