package com.wsh.ui;

import com.wsh.pojo.CommonTrainTicket;
import com.wsh.pojo.HighSpeedTicket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ShowTicketsUI {
    private ArrayList<HighSpeedTicket> highSpeedTickets;
    private ArrayList<CommonTrainTicket> commonTrainTickets;

    public ShowTicketsUI(ArrayList<HighSpeedTicket> list1, ArrayList<CommonTrainTicket> list2){
        this.highSpeedTickets = list1;
        this.commonTrainTickets = list2;
        init();
    }

    public void init() {
        JFrame jf = new JFrame("查询余票");
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 关闭时不退出整个程序
        jf.setSize(1000, 500);
        jf.setLocationRelativeTo(null);
        jf.setLayout(new BorderLayout());

        // 顶部标题
        JLabel titleLabel = new JLabel("车票余票信息", JLabel.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(211, 47, 47)); // 铁路红
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // 表头
        String[] columns = {"车次", "出发站", "到达站", "发车时间", "到达时间", "有票","硬座/一等座票数", "硬卧/二等座票数"};
        //数据
        int c = columns.length;
        int h=highSpeedTickets.size()+commonTrainTickets.size();
        Object[][] data = new Object[h][c];
        for(int i=0;i<data.length;i++){
            if((i&1)==0){
                HighSpeedTicket ticket = highSpeedTickets.get(i/2);
                data[i][0] = ticket.getTrainName();
                data[i][1] = ticket.getStartStation();
                data[i][2] = ticket.getEndStation();
                data[i][3] = ticket.getStartTime();
                data[i][4] = ticket.getEndTime();
                data[i][5]= ticket.isStatus()?"否":"是";
                data[i][6] = ticket.getMap().get("一等座");
                data[i][7] = ticket.getMap().get("二等座");
            }else{
                System.out.println(1);
                CommonTrainTicket ticket = commonTrainTickets.get(i/2);
                data[i][0] = ticket.getTrainName();
                data[i][1] = ticket.getStartStation();
                data[i][2] = ticket.getEndStation();
                data[i][3] = ticket.getStartTime();
                data[i][4] = ticket.getEndTime();
                data[i][5]= ticket.isStatus()?"否":"是";
                data[i][6] = ticket.getMap().get("硬座");
                data[i][7] = ticket.getMap().get("硬卧");
            }
        }

        // 创建表格模型
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 不可编辑
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 13));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 底部按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JButton backButton = new JButton("返回");
        backButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        backButton.addActionListener(e -> jf.dispose()); // 关闭当前窗口

        JButton bookButton = new JButton("订票");
        bookButton.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        bookButton.setBackground(new Color(211, 47, 47));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);
        //bookButton.addActionListener(new BookActionListener(table));

        buttonPanel.add(backButton);
        buttonPanel.add(bookButton);

        // 组装界面
        jf.add(titleLabel, BorderLayout.NORTH);
        jf.add(scrollPane, BorderLayout.CENTER);
        jf.add(buttonPanel, BorderLayout.SOUTH);

        jf.setVisible(true);
    }
}
