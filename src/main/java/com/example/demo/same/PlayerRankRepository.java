package com.example.demo.same;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRankRepository extends JpaRepository<PlayerRank, Integer> {
	List<PlayerRank> findByGameCodeOrderByPrizeDesc(Integer gameCode);

//	@Query(value = "SELECT code, player_name, prize, game_code, rank() over(order by prize desc) FROM player_rank", nativeQuery = true)
//	List<PlayerRank> findAll2();

}
