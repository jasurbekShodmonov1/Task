package com.example.task.service;


import com.example.task.dto.response.BalanceTransactionResponseDto;
import com.example.task.dto.response.UserResponseDto;
import com.example.task.entity.BalanceTransaction;
import com.example.task.mapper.BalanceTransactionMapper;
import com.example.task.repository.BalanceTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.module.ResolutionException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BalanceTransactionServiceTest {

    @Mock
    private BalanceTransactionRepository balanceTransactionRepository;

    @Mock
    private BalanceTransactionMapper balanceTransactionMapper;

    @InjectMocks
    private BalanceTransactionService balanceTransactionService;

    @Test
    void testGetAllBalanceTransaction(){
        BalanceTransaction mockBalanceTransaction1 = mock(BalanceTransaction.class);
        BalanceTransaction mockBalanceTransaction2 = mock(BalanceTransaction.class);
        List<BalanceTransaction> transactionList = List.of(mockBalanceTransaction1, mockBalanceTransaction2);

        UserResponseDto sender1 = new UserResponseDto(UUID.randomUUID(), "Alice", 500.0,"alice123");
        UserResponseDto receiver1 = new UserResponseDto(UUID.randomUUID(), "Bob", 300.0, "bob123");

        UserResponseDto sender2= new UserResponseDto(UUID.randomUUID(), "Alex", 400.0, "alex123");
        UserResponseDto receiver2 = new UserResponseDto(UUID.randomUUID(), "Jack", 300.0, "jack123");

        BalanceTransactionResponseDto dto1 = new BalanceTransactionResponseDto(UUID.randomUUID(), new BigDecimal("100.0"), "Payment for lunch",sender1, receiver1);
        BalanceTransactionResponseDto dto2 = new BalanceTransactionResponseDto(UUID.randomUUID(), new BigDecimal("150.0"), "Refund", sender2, receiver2);

        when(balanceTransactionRepository.findAll()).thenReturn(transactionList);
        when(balanceTransactionMapper.toDto(mockBalanceTransaction1)).thenReturn(dto1);
        when(balanceTransactionMapper.toDto(mockBalanceTransaction2)).thenReturn(dto2);

        List<BalanceTransactionResponseDto> result = balanceTransactionService.getAll();

        assertEquals(2, result.size());

        BalanceTransactionResponseDto first = result.get(0);
        assertEquals("Payment for lunch", first.description());
        assertEquals(new BigDecimal("100.0"), first.amount());
        assertEquals("Alice", first.senderUser().fullName());
        assertEquals("Bob", first.receiverUser().fullName());

        BalanceTransactionResponseDto second = result.get(1);
        assertEquals("Refund", second.description());
        assertEquals(new BigDecimal("150.0"),second.amount());
        assertEquals("Alex", second.senderUser().fullName());
        assertEquals("Jack", second.receiverUser().fullName());

        verify(balanceTransactionRepository).findAll();
        verify(balanceTransactionMapper).toDto(mockBalanceTransaction1);
        verify(balanceTransactionMapper).toDto(mockBalanceTransaction2);
    }

    @Test
    void getTransactionById_validId_returnsTransaction(){
        UUID id = UUID.randomUUID();
        BalanceTransaction mockTransaction = mock(BalanceTransaction.class);

        UserResponseDto sender = new UserResponseDto(UUID.randomUUID(), "Alice", 100.0, "alice123");
        UserResponseDto receiver = new UserResponseDto(UUID.randomUUID(), "Bob", 200.0, "bob321");

        BalanceTransactionResponseDto responseDto = new BalanceTransactionResponseDto(id, new BigDecimal("50.0"), "Lunch payment", sender,receiver);

        when(balanceTransactionRepository.findById(id)).thenReturn(Optional.of(mockTransaction));
        when(balanceTransactionMapper.toDto(mockTransaction)).thenReturn(responseDto);

        BalanceTransactionResponseDto result = balanceTransactionService.getTransactionById(id);

        assertNotNull(result);
        assertEquals("Lunch payment",result.description());
        assertEquals(new BigDecimal("50.0"), result.amount());
        assertEquals("Alice", result.senderUser().fullName());
        assertEquals("Bob", result.receiverUser().fullName());

        verify(balanceTransactionRepository).findById(id);
        verify(balanceTransactionMapper).toDto(mockTransaction);
    }

    @Test
    void getTransactionById_ShouldThrowException_WhenTransactionNotFound(){
        UUID id =UUID.randomUUID();


        when(balanceTransactionRepository.findById(id)).thenReturn(Optional.empty());

         RuntimeException exception = assertThrows(
                 RuntimeException.class,
                 ()->balanceTransactionService.getTransactionById(id)
         );

         assertEquals("transaction not found", exception.getMessage());

         verify(balanceTransactionMapper,never()).toDto(any());
    }
}
