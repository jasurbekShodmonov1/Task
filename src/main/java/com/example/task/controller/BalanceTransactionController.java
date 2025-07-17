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


    @GetMapping()
    public ResponseEntity<List<BalanceTransactionResponseDto>> getAll(){
        List<BalanceTransactionResponseDto> transactions = balanceTransactionService.getAll();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("{id}")
    public ResponseEntity<BalanceTransactionResponseDto> getById(@PathVariable UUID id){
        BalanceTransactionResponseDto balanceTransaction = balanceTransactionService.getTransactionById(id);
        return ResponseEntity.ok(balanceTransaction);
    }

    @GetMapping("/byDate")
    public ResponseEntity<List<BalanceTransactionResponseDto>> getByDate(@RequestParam LocalDate date){
        List<BalanceTransactionResponseDto> transactions = balanceTransactionService.getByDate(date);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/byUser")
    public ResponseEntity<List<BalanceTransactionResponseDto>> getByUser(@RequestParam UUID id){
        List<BalanceTransactionResponseDto> transactions = balanceTransactionService.getByUser(id);
        return ResponseEntity.ok(transactions);
    }


    @PostMapping("/{userId}createTransaction")
    public ResponseEntity<BalanceTransactionResponseDto> createTransaction( @Valid @RequestBody BalanceTransactionRequestDto balanceTransactionRequestDto){
        BalanceTransactionResponseDto balanceTransactionResponseDto = balanceTransactionService.createTransaction(balanceTransactionRequestDto);
        return ResponseEntity.ok(balanceTransactionResponseDto);
    }

}
