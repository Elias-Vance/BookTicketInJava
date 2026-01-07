package com.wsh.pojo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class User {
        private String userName;
        private String password;
        private String name;
        private ArrayList<UserTicket> userTickets=new ArrayList<>();
}
