package com.wsh.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserTicket {
    private String userName;
    private int id;
    private String trainName;
    private String startStation;
    private String endStation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String seatType;
    private double price;
}
