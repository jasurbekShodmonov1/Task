package com.example.task.controller;


import com.example.task.dto.request.BalanceTransactionRequestDto;
import com.example.task.dto.response.BalanceTransactionResponseDto;
import com.example.task.entity.BalanceTransaction;
import com.example.task.service.BalanceTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/balanceTransaction/v1")
@RequiredArgsConstructor
public class BalanceTransactionController {

    private  final BalanceTransactionService balanceTransactionService;


//    @GetMapping()
//    public ResponseEntity<List<BalanceTransaction>> getAll(){
//        List<BalanceTransaction> transactions = balanceTransactionService.getAll();
//        return ResponseEntity.ok(transactions);
//    }
//
//    @GetMapping("{id}")
//    public ResponseEntity<BalanceTransaction> getById(@PathVariable UUID id){
//        BalanceTransaction balanceTransaction = balanceTransactionService.getTransactionById(id);
//        return ResponseEntity.ok(balanceTransaction);
//    }

//    @GetMapping()
//    public ResponseEntity<List<BalanceTransaction>> getByDate(@RequestParam LocalDate date){
//        List<BalanceTransaction> transactions = balanceTransactionService.getByDate(date);
//        return ResponseEntity.ok(transactions);
//    }

//    @GetMapping("/byUser")
//    public ResponseEntity<List<BalanceTransaction>> getByUser(@RequestParam UUID id){
//        List<BalanceTransaction> transactions = balanceTransactionService.getByUser(id);
//        return ResponseEntity.ok(transactions);
//    }


    @PostMapping("/{userId}createTransaction")
    public ResponseEntity<BalanceTransactionResponseDto> createTransaction( @Valid @RequestBody BalanceTransactionRequestDto balanceTransactionRequestDto){
        BalanceTransactionResponseDto balanceTransactionResponseDto = balanceTransactionService.createTransaction(balanceTransactionRequestDto);
        return ResponseEntity.ok(balanceTransactionResponseDto);
    }

}
