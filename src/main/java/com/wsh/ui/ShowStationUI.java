package com.wsh.ui;

import com.wsh.pojo.CommonTrainTicket;
import com.wsh.pojo.HighSpeedTicket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShowStationUI {
    private ArrayList<HighSpeedTicket> highSpeedTickets;
    private ArrayList<CommonTrainTicket> commonTrainTickets;
    private ArrayList<HighSpeedTicket> originalHighSpeedTickets;
    private ArrayList<CommonTrainTicket> originalCommonTrainTickets;
    private JFrame mainFrame;
    private JTextField startStationNameField;
    private JTextField endStationNameField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public ShowStationUI(ArrayList<HighSpeedTicket> tickets1, ArrayList<CommonTrainTicket> tickets2) {
        highSpeedTickets=tickets1;
        commonTrainTickets=tickets2;
        originalHighSpeedTickets = new ArrayList<>(tickets1);
        originalCommonTrainTickets = new ArrayList<>(tickets2);
        initUI();
    }

    public void initUI() {
        mainFrame = new JFrame("站点查询");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout(10, 10));
        mainFrame.setSize(1000, 500);
        mainFrame.setLocationRelativeTo(null);

        // 标题
        JLabel titleLabel = new JLabel("站点查询", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        mainFrame.add(titleLabel, BorderLayout.NORTH);

        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JLabel searchLabel1 = new JLabel("输入出发站点名称:");
        startStationNameField = new JTextField(20);

        JLabel searchLabel2=new JLabel("输入到达站点名称:");
        endStationNameField = new JTextField(20);

        JButton searchButton = new JButton("查询");
        JButton clearButton = new JButton("清空");

        searchButton.addActionListener(e -> performSearch());
        clearButton.addActionListener(e -> {
            startStationNameField.setText("");
            endStationNameField.setText("");
            displayAllStations();
        });

        searchPanel.add(searchLabel1);
        searchPanel.add(startStationNameField);
        searchPanel.add(searchLabel2);
        searchPanel.add(endStationNameField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        mainFrame.add(searchPanel, BorderLayout.NORTH);

        // 结果表格
        setupResultTable();

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainFrame.add(scrollPane, BorderLayout.CENTER);

        // 显示初始所有站点
        displayAllStations();

        mainFrame.setVisible(true);
    }

    private void setupResultTable() {
        String[] columns = {"车次", "出发站", "到达站", "出发时间", "到达时间", "一等座剩余数量", "二等座剩余数量"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 只读
            }
        };

        resultTable = new JTable(tableModel);
        resultTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        resultTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        resultTable.setRowHeight(25);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void performSearch() {
        String startStationName = startStationNameField.getText().trim();
        String endStationName = endStationNameField.getText().trim();
        if (startStationName.isEmpty() && endStationName.isEmpty()) {
            displayAllStations();
            return;
        }

        ArrayList<HighSpeedTicket> filteredHighSpeedTickets = new ArrayList<>();
        ArrayList<CommonTrainTicket> filteredCommonTrainTickets = new ArrayList<>();

        // 搜索匹配的站点
        if(startStationName.isEmpty()){
            for (HighSpeedTicket t : originalHighSpeedTickets) {
                if (t.getEndStation().toLowerCase().contains(endStationName.toLowerCase())) {
                    filteredHighSpeedTickets.add(t);
                }
            }
            for (CommonTrainTicket t : originalCommonTrainTickets) {
                if (t.getEndStation().toLowerCase().contains(endStationName.toLowerCase())) {
                    filteredCommonTrainTickets.add(t);
                }
            }
        }else if(endStationName.isEmpty()){
            for (HighSpeedTicket t : originalHighSpeedTickets) {
                if (t.getStartStation().toLowerCase().contains(startStationName.toLowerCase())) {
                    filteredHighSpeedTickets.add(t);
                }
            }
            for (CommonTrainTicket t : originalCommonTrainTickets) {
                if (t.getStartStation().toLowerCase().contains(startStationName.toLowerCase())) {
                    filteredCommonTrainTickets.add(t);
                }
            }
        }else{
            for (HighSpeedTicket t : originalHighSpeedTickets) {
                if (t.getStartStation().toLowerCase().contains(startStationName.toLowerCase()) && t.getEndStation().toLowerCase().contains(endStationName.toLowerCase())) {
                    filteredHighSpeedTickets.add(t);
                }
            }
            for (CommonTrainTicket t : originalCommonTrainTickets) {
                if (t.getStartStation().toLowerCase().contains(startStationName.toLowerCase()) && t.getEndStation().toLowerCase().contains(endStationName.toLowerCase())) {
                    filteredCommonTrainTickets.add(t);
                }
            }
        }

        highSpeedTickets=filteredHighSpeedTickets;
        commonTrainTickets=filteredCommonTrainTickets;

        // 显示搜索结果
        displayFilteredStations();
    }

    private void displayAllStations() {
        tableModel.setRowCount(0);
        for (HighSpeedTicket t : originalHighSpeedTickets) {
            Object[] row = {t.getTrainName(),
                    t.getStartStation(),
                    t.getEndStation(),
                    t.getStartTime(),
                    t.getEndTime(),
                    t.getMap().get("一等座"),
                    t.getMap().get("二等座")};
            tableModel.addRow(row);
        }
        for (CommonTrainTicket t : originalCommonTrainTickets) {
            Object[] row = {t.getTrainName(),
                    t.getStartStation(),
                    t.getEndStation(),
                    t.getStartTime(),
                    t.getEndTime(),
                    t.getMap().get("硬座"),
                    t.getMap().get("硬卧")};
            tableModel.addRow(row);
        }
    }

    private void clearResults() {
        tableModel.setRowCount(0);
    }

    public void displayFilteredStations(){
        tableModel.setRowCount(0);
        for (HighSpeedTicket t : highSpeedTickets) {
            Object[] row = {t.getTrainName(),
                    t.getStartStation(),
                    t.getEndStation(),
                    t.getStartTime(),
                    t.getEndTime(),
                    t.getMap().get("一等座"),
                    t.getMap().get("二等座")};
            tableModel.addRow(row);
        }
        for (CommonTrainTicket t : commonTrainTickets) {
            Object[] row = {t.getTrainName(),
                    t.getStartStation(),
                    t.getEndStation(),
                    t.getStartTime(),
                    t.getEndTime(),
                    t.getMap().get("硬座"),
                    t.getMap().get("硬卧")};
            tableModel.addRow(row);
        }
    }

}
