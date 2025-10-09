package com.example.task.service;


import com.example.task.dto.response.BalanceTransactionResponseDto;
import com.example.task.dto.response.UserResponseDto;
import com.example.task.entity.BalanceTransaction;
import com.example.task.entity.User;
import com.example.task.mapper.BalanceTransactionMapper;
import com.example.task.repository.BalanceTransactionRepository;
import com.example.task.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.module.ResolutionException;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private UserRepository userRepository;

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

    @Test
    void  getByUser_validId_returnsTransaction(){

        UUID userId = UUID.randomUUID();
        User user = new User();

        user.setId(userId);

        BalanceTransaction transaction1 = mock(BalanceTransaction.class);
        BalanceTransaction transaction2 = mock(BalanceTransaction.class);

        UserResponseDto sender = new UserResponseDto(userId, "Alice", 100.0, "alice123");
        UserResponseDto receiver = new UserResponseDto(UUID.randomUUID(), "Bob", 200.0, "bob321");

        BalanceTransactionResponseDto dto1 =
                new BalanceTransactionResponseDto(UUID.randomUUID(), new BigDecimal("10.0"), "coffee", sender, receiver);
        BalanceTransactionResponseDto dto2 =
                new BalanceTransactionResponseDto(UUID.randomUUID(), new BigDecimal("25.0"), "lunch", sender, receiver);


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(balanceTransactionRepository.findBySenderUser(user))
                .thenReturn(List.of(transaction1, transaction2));
        when(balanceTransactionMapper.toDto(transaction1)).thenReturn(dto1);
        when(balanceTransactionMapper.toDto(transaction2)).thenReturn(dto2);

        List<BalanceTransactionResponseDto> result = balanceTransactionService.getByUser(userId);

        assertEquals(2, result.size());
        assertEquals(new BigDecimal("10.0"), result.get(0).amount());
        assertEquals("coffee", result.get(0).description());
        assertEquals("lunch", result.get(1).description());

        verify(userRepository).findById(userId);
        verify(balanceTransactionRepository).findBySenderUser(user);
        verify(balanceTransactionMapper, times(2)).toDto(any(BalanceTransaction.class));
    }

    @Test
    void getByUser_ShouldThrowException_WhenUserNotFound(){
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                ()->balanceTransactionService.getByUser(userId)
        );

        assertEquals("user not found",exception.getMessage());

        verify(balanceTransactionRepository, never()).findBySenderUser(any());
        verify(balanceTransactionMapper, never()).toDto(any());
    }

    @Test
    void getByDate_ShouldReturnTransactions_WhenDateBeforeToday(){
        LocalDate date = LocalDate.now().minusDays(2);
        LocalDate today = LocalDate.now();

        BalanceTransaction transaction = new BalanceTransaction();
        BalanceTransactionResponseDto dto =
                new BalanceTransactionResponseDto(
                        UUID.randomUUID(),
                        BigDecimal.valueOf(100),
                        "Test transaction",
                        null,
                        null
                );

        when(balanceTransactionRepository.findByTimestampBetween(date,today))
                .thenReturn(List.of(transaction));
        when(balanceTransactionMapper.toDto(transaction)).thenReturn(dto);

        List<BalanceTransactionResponseDto> result = balanceTransactionService.getByDate(date);

        assertEquals(1, result.size());
        assertEquals("Test transaction", result.get(0).description());
        verify(balanceTransactionRepository).findByTimestampBetween(date, today);
        verify(balanceTransactionMapper).toDto(transaction);
    }

    @Test
    void getByDate_ShouldReturnEmptyList_WhenDateIsTodayOrAfter(){
        LocalDate today = LocalDate.now();

        List<BalanceTransactionResponseDto>  result = balanceTransactionService.getByDate(today);

        assertTrue(result.isEmpty());
        verifyNoInteractions(balanceTransactionRepository);
        verifyNoInteractions(balanceTransactionMapper);
    }

}
