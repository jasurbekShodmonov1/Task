package com.example.task.entity;

import com.example.task.entity.base.BaseDomain;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class BalanceTransaction  extends BaseDomain<UUID> {

    private Double amount;

    private String description;

    private LocalDate timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
