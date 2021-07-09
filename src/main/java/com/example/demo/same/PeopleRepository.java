package com.example.demo.same;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleRepository extends JpaRepository<People,Integer>{
	List<People> findByUserid(String userid);
	List<People> findByEmail(String email);
	List<People> findByUseridAndPassword(String userid, String password);
}
