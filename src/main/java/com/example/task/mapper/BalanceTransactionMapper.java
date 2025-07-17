package com.example.task.mapper;


import com.example.task.dto.request.BalanceTransactionRequestDto;
import com.example.task.dto.response.BalanceTransactionResponseDto;
import com.example.task.entity.BalanceTransaction;
import com.example.task.entity.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BalanceTransactionMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", ignore = true)

    BalanceTransaction toEntity(BalanceTransactionRequestDto  balanceTransactionRequestDto);

    BalanceTransactionResponseDto toDto(BalanceTransaction balanceTransaction);
}
