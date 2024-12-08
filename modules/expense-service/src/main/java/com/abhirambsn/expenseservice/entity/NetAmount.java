package com.abhirambsn.expenseservice.entity;

import org.springframework.data.annotation.Id;

public class NetAmount {
    @Id
    private String id;
    private Double netAmount;
}
