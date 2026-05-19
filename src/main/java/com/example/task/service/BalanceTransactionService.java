package com.example.task.service;


import com.example.task.dto.request.BalanceTransactionRequestDto;
import com.example.task.entity.User;
import com.example.task.entity.BalanceTransaction;
import com.example.task.repository.BalanceTransactionRepository;
import com.example.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BalanceTransactionService {

    private final UserRepository userRepository;
    private final BalanceTransactionRepository balanceTransactionRepository;


    public List<BalanceTransaction> getAll(){
        List<BalanceTransaction> balanceTransactionsAllList = balanceTransactionRepository.findAll();

        return balanceTransactionsAllList.stream().toList();

    }

    public BalanceTransaction getTransactionById(UUID id){
        BalanceTransaction transaction = balanceTransactionRepository.findById(id)
                .orElseThrow(()->new RuntimeException("transaction not found"));

        return transaction;
    }

    public List<BalanceTransaction> getByUser(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("user not found"));

        List<BalanceTransaction> transactions = balanceTransactionRepository.findByUser(user);
        return transactions.stream().toList();
    }
//    public List<BalanceTransaction> getByDate(LocalDate date){
//        LocalDate today =LocalDate.now();
//        if(date.isBefore(today)){
//
//            List<BalanceTransaction> transactions = balanceTransactionRepository.findByDate(date, today);
//
//            return transactions.stream().toList();
//        } else{
//            return Collections.emptyList();
//        }
//    }

    public String addMoney(UUID id, BalanceTransactionRequestDto balanceTransactionRequestDto){


        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBalance(user.getBalance()+balanceTransactionRequestDto.amount());
        userRepository.save(user);

        LocalDate today = LocalDate.now();
        BalanceTransaction balanceTransaction = new BalanceTransaction();
        balanceTransaction.setAmount(balanceTransactionRequestDto.amount());
        balanceTransaction.setDescription(balanceTransactionRequestDto.description());
        balanceTransaction.setTimestamp(today);
        balanceTransaction.setUser(user);
        balanceTransactionRepository.save(balanceTransaction);





        return "Money added successfully";

    }

    public String subtractionMoney(UUID id, BalanceTransactionRequestDto balanceTransactionRequestDto){


        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(user.getBalance() > balanceTransactionRequestDto.amount()) {
            user.setBalance(user.getBalance() - balanceTransactionRequestDto.amount());

            userRepository.save(user);

            LocalDate today = LocalDate.now();
            BalanceTransaction balanceTransaction = new BalanceTransaction();
            balanceTransaction.setAmount(balanceTransactionRequestDto.amount());
            balanceTransaction.setDescription(balanceTransactionRequestDto.description());
            balanceTransaction.setTimestamp(today);
            balanceTransaction.setUser(user);
            balanceTransactionRepository.save(balanceTransaction);

            return "Money added successfully";

        }else {
            return "There is not enough money";
        }


    }
}
