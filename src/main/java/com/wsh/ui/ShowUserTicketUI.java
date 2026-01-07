package com.wsh.ui;

import com.wsh.listener.UIListener;
import com.wsh.pojo.Ticket;
import com.wsh.pojo.User;
import com.wsh.pojo.UserTicket;

import javax.naming.InitialContext;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ShowUserTicketUI {
    private static User user;
    private int id; //改签的id
    private UserTicket userTicket; //改签的票

    public ShowUserTicketUI(User user) {
        this.user = user;
        init();
    }
    public void init() {
        // 创建主窗口
        javax.swing.JFrame frame = new javax.swing.JFrame("我的购票记录");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // 创建主面板
        javax.swing.JPanel mainPanel = new javax.swing.JPanel(new java.awt.BorderLayout(10, 10));
        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 标题标签
        javax.swing.JLabel titleLabel = new javax.swing.JLabel("我的购票记录", javax.swing.SwingConstants.CENTER);
        titleLabel.setFont(new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 18));
        mainPanel.add(titleLabel, java.awt.BorderLayout.NORTH);

        // 创建表格模型和表格
        String[] columnNames = {"车次", "出发站", "到达站", "出发时间", "到达时间", "座位", "票价"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        ArrayList<UserTicket >  tickets = user.getUserTickets();
        for (UserTicket ticket : tickets) {
            Object[] rowData = {
                    ticket.getTrainName(),
                    ticket.getStartStation(),
                    ticket.getEndStation(),
                    ticket.getStartTime(),
                    ticket.getEndTime(),
                    ticket.getSeatType(),
                    "¥" + ticket.getPrice()
            };
            model.addRow(rowData);
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new java.awt.Font("微软雅黑", java.awt.Font.PLAIN, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton refundButton = new JButton("改签");
        refundButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // 获取选中行的数据
                String trainNumber = (String)model.getValueAt(selectedRow, 0);
                String startStation = (String)model.getValueAt(selectedRow, 1);
                String endStation = (String)model.getValueAt(selectedRow, 2);
                String seatType = (String)model.getValueAt(selectedRow, 5);

                int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "确定要改签 " + trainNumber + " 次列车从 " + startStation + " 到 " + endStation + " 的票吗？",
                        "确认改签",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // 确定ID
                    for(UserTicket ticket : tickets){
                        if(ticket.getTrainName().equals(trainNumber) && ticket.getStartStation().equals(startStation)
                                && ticket.getEndStation().equals(endStation) && ticket.getSeatType().equals(seatType)){
                            userTicket = ticket;
                            id = ticket.getId();
                            tickets.remove(ticket);
                            break;
                        }
                    }
                    try {
                        deleteTicket();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    new BookTicketUI(UIListener.highSpeedTickets, UIListener.commonTrainTickets);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "请先选择要改签的车票");
            }
        });

        // 添加滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(java.awt.FlowLayout.RIGHT));

        // 返回按钮
        JButton backButton = new javax.swing.JButton("返回");
        backButton.addActionListener(e -> {
            frame.dispose();
        });
        buttonPanel.add(backButton);
        buttonPanel.add(refundButton);

        mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public void deleteTicket() throws IOException {
        String filePath = "src/main/resources/UserInfo/userTickets";
        // 读取所有行
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (int i = 0; i < lines.size(); i++) {
            String[] tokens = lines.get(i).split(" ");
            if (id == Integer.parseInt(tokens[tokens.length-1])) {
                lines.remove(i);
                break;
            }
        }
        BufferedWriter  writer=new BufferedWriter(new FileWriter(filePath));
        writer.close();
        writer=new BufferedWriter(new FileWriter(filePath,true));
        for(String s:lines){
            System.out.println(s);
            writer.write(s);
            writer.newLine();
        }
        writer.flush();
        writer.close();

        //把座位还回去
        filePath = "src/main/resources/TicketInfo/tickets";
        // 读取所有行
        lines = Files.readAllLines(Paths.get(filePath));
        for (int i = 0; i < lines.size(); i++) {
            if (i == id) {
                String line = lines.get(i);
                StringTokenizer tokenizer = new StringTokenizer(line);
                StringBuilder stringBuilder = new StringBuilder();
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if (token.startsWith(userTicket.getSeatType())) {
                        stringBuilder.append(userTicket.getSeatType()).append("=").append(Integer.parseInt(token.split("=")[1])+1).append(" ");
                    } else {
                        stringBuilder.append(token).append(" ");
                    }
                }
                lines.set(i, stringBuilder.toString());
                break;
            }
        }
        writer=new BufferedWriter(new FileWriter(filePath));
        writer.close();
        writer=new BufferedWriter(new FileWriter(filePath,true));
        for(String s:lines){
            System.out.println(s);
            writer.write(s);
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

}
