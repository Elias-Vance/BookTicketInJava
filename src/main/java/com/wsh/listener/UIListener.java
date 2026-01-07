package com.wsh.listener;

import com.wsh.pojo.CommonTrainTicket;
import com.wsh.pojo.HighSpeedTicket;
import com.wsh.pojo.Ticket;
import com.wsh.pojo.User;
import com.wsh.ui.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class UIListener implements ActionListener {
    private BufferedReader reader;
    public static ArrayList<HighSpeedTicket> highSpeedTickets=new ArrayList<>();
    public static ArrayList<CommonTrainTicket> commonTrainTickets=new ArrayList<>();

    public UIListener() throws IOException {
        InputStream inputStream = UIListener.class.getResourceAsStream("/TicketInfo/tickets");
        reader=new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line=reader.readLine())!=null){
            StringTokenizer tokenizer=new StringTokenizer(line);
            int id=Integer.parseInt(tokenizer.nextToken());
            Ticket ticket;
            if((id&1)==0){
                ticket=new HighSpeedTicket();
            }else{
                ticket=new CommonTrainTicket();
            }
            ticket.setId(id);
            ticket.setTrainName(tokenizer.nextToken());
            ticket.setTrainType(tokenizer.nextToken());
            ticket.setStartStation(tokenizer.nextToken());
            ticket.setEndStation(tokenizer.nextToken());
            ticket.setStartTime(LocalDateTime.parse(tokenizer.nextToken()));
            ticket.setEndTime(LocalDateTime.parse(tokenizer.nextToken()));
            ticket.setPrice(Double.parseDouble(tokenizer.nextToken()));
            ticket.setStatus(Boolean.parseBoolean(tokenizer.nextToken()));
            String s1=tokenizer.nextToken();
            String[] s2=s1.split("=");
            ticket.getMap().put(s2[0],Integer.parseInt(s2[1]));
            s1=tokenizer.nextToken();
            s2=s1.split("=");
            ticket.getMap().put(s2[0],Integer.parseInt(s2[1]));
            if (!ticket.isStatus()) {
                if (ticket instanceof HighSpeedTicket) {
                    highSpeedTickets.add((HighSpeedTicket) ticket);
                } else {
                    commonTrainTickets.add((CommonTrainTicket) ticket);
                }
            }
        }
        reader.close();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String s=e.getActionCommand();
        if (s.equals("查询余票")){
            new ShowTicketsUI(this.highSpeedTickets, commonTrainTickets);
            System.out.println(this.highSpeedTickets.hashCode());
        } else if (s.equals("查询车次")) {
            new SearchTrainsUI(this.highSpeedTickets, commonTrainTickets);
        } else if (s.equals("查询站点")) {
            new ShowStationUI(this.highSpeedTickets, commonTrainTickets);
        } else if (s.equals("订票")) {
            new BookTicketUI(this.highSpeedTickets, commonTrainTickets);
        }else if (s.equals("查看车票")) {
            new ShowUserTicketUI(LoginListener.user);
        } else if (s.equals("退票")) {
            new RefundTicketUI(LoginListener.user);
        }

    }

}
