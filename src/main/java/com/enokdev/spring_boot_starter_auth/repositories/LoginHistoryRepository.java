package com.enokdev.spring_boot_starter_auth.repositories;

import com.enokdev.spring_boot_starter_auth.entities.LoginHistory;
import com.enokdev.spring_boot_starter_auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    List<LoginHistory> findByUserOrderByTimestampDesc(User user);
}