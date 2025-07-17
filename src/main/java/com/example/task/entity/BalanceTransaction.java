package com.example.task.entity;

import com.example.task.entity.base.BaseDomain;
import com.example.task.entity.enums.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class BalanceTransaction  extends BaseDomain<UUID> {

    @Column(precision = 19, scale = 2)
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private Transaction transaction;

    @CreationTimestamp
    private LocalDate timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
