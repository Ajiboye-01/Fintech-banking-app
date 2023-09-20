package com.fintech.globalBank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreditDebitRequest {
    private String accountNumber;
    private BigDecimal amount;
}
