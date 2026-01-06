package com.wsh.pojo;

import lombok.Data;

@Data
public class User {
        private String userName;
        private String password;
        private String name;
        private UserTicket  userTicket;
}
