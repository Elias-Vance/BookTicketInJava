package com.wsh.pojo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;

public class CommonTrainTicket extends  Ticket {
    private int id;
    private String trainName;
    private String trainType;
    private String startStation;
    private String endStation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double price;
    private boolean status;
    private HashMap<String,Integer> map=new HashMap<>();
}
