package com.example.task.service;


import com.example.task.dto.request.BalanceTransactionRequestDto;
import com.example.task.dto.response.BalanceTransactionResponseDto;
import com.example.task.entity.User;
import com.example.task.entity.BalanceTransaction;
import com.example.task.mapper.BalanceTransactionMapper;
import com.example.task.repository.BalanceTransactionRepository;
import com.example.task.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.IMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BalanceTransactionService {

    private final UserRepository userRepository;
    private final BalanceTransactionRepository balanceTransactionRepository;
    private final BalanceTransactionMapper balanceTransactionMapper;


    public List<BalanceTransactionResponseDto> getAll() {
        List<BalanceTransaction> balanceTransactionsAllList = balanceTransactionRepository.findAll();

        return balanceTransactionsAllList.stream().map(balanceTransactionMapper::toDto).toList();

    }

    public BalanceTransactionResponseDto getTransactionById(UUID id) {
        BalanceTransaction transaction = balanceTransactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("transaction not found"));

        return balanceTransactionMapper.toDto(transaction);
    }

    public List<BalanceTransactionResponseDto> getByUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("user not found"));

        List<BalanceTransaction> transactions = balanceTransactionRepository.findBySenderUser(user);
        return transactions.stream().map(balanceTransactionMapper::toDto).toList();
    }
    public List<BalanceTransactionResponseDto> getByDate(LocalDate date){
        LocalDate today =LocalDate.now();
        if(date.isBefore(today)){

            List<BalanceTransaction> transactions = balanceTransactionRepository.findByTimestampBetween(date, today);

            return transactions.stream().map(balanceTransactionMapper::toDto).toList();
        } else{
            return Collections.emptyList();
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public BalanceTransactionResponseDto createTransaction(BalanceTransactionRequestDto balanceTransactionRequestDto) {
        User senderUser = userRepository.findById(balanceTransactionRequestDto.senderUserId()).orElseThrow(()->new RuntimeException("user not found"));

        User receiverUser = userRepository.findById(balanceTransactionRequestDto.receiverUserId()).orElseThrow(()->new RuntimeException("user not found"));
        BalanceTransaction balanceTransaction = balanceTransactionMapper.toEntity(balanceTransactionRequestDto);
        BigDecimal amount = balanceTransactionRequestDto.amount();
        balanceTransaction.setSenderUser(senderUser);
        balanceTransaction.setReceiverUser(receiverUser);

//
//        switch (balanceTransactionRequestDto.transaction()){
//            case DEPOSIT :
//                senderUser.setBalance(senderUser.getBalance().add(amount));
//                receiverUser.setBalance(receiverUser.getBalance().subtract(amount));
//                break;
//            case WITHDRAW :
//                if(senderUser.getBalance().compareTo(amount)>0) {
//                    senderUser.setBalance(senderUser.getBalance().subtract(amount));
//                    receiverUser.setBalance(receiverUser.getBalance().add(amount));
//
//                }else {
//                    throw new RuntimeException("There is not enough money");
//                }
//                break;
//            default:
//                throw new RuntimeException("Unsupported transaction type");
//        }

        if (senderUser.getBalance().compareTo(amount) > 0) {
            senderUser.setBalance(senderUser.getBalance().subtract(amount));
            receiverUser.setBalance(receiverUser.getBalance().add(amount));

            userRepository.save(senderUser);
            userRepository.save(receiverUser);
            balanceTransactionRepository.save(balanceTransaction);
            return balanceTransactionMapper.toDto(balanceTransaction);
        } else {
            throw new RuntimeException("Fault");
        }
    }

}
