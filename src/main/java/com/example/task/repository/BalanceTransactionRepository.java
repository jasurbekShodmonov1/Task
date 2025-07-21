package com.example.task.repository;

import com.example.task.entity.BalanceTransaction;
import com.example.task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, UUID> {
    List<BalanceTransaction> findBySenderUser(User user);
    List<BalanceTransaction> findByReceiverUser(User user);
    List<BalanceTransaction> findByTimestampBetween(LocalDate startDate, LocalDate endDate);

//        List<BalanceTransaction> findAllByTimestamp(LocalDate date);
}
