package com.example.demo.same;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SamejiroRepository extends JpaRepository<Samejiro, Integer> {
}
