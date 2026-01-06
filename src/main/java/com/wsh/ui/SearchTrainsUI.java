package com.wsh.ui;

import com.wsh.pojo.CommonTrainTicket;
import com.wsh.pojo.HighSpeedTicket;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchTrainsUI {
    private ArrayList<HighSpeedTicket> highSpeedTickets;
    private ArrayList<CommonTrainTicket> commonTrainTickets;
    private ArrayList<HighSpeedTicket> originalHighSpeedTickets;
    private ArrayList<CommonTrainTicket> originalCommonTrainTickets;
    private JFrame jf;

    public SearchTrainsUI(ArrayList<HighSpeedTicket> list1, ArrayList<CommonTrainTicket> list2){
        this.highSpeedTickets = new ArrayList<>(list1);
        this.commonTrainTickets = new ArrayList<>(list2);
        originalHighSpeedTickets = list1 ;
        originalCommonTrainTickets =list2;
        init();
    }

    public void init() {
        jf = new JFrame("查询车次");
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setLayout(new BorderLayout(10, 10));
        jf.getContentPane().setBackground(Color.WHITE);

        // 标题
        JLabel titleLabel = new JLabel("车次查询结果", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        jf.add(titleLabel, BorderLayout.NORTH);

        // 搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("输入车次:");
        JTextField searchField = new JTextField(15);

        JButton searchButton = new JButton("搜索");
        JButton resetButton = new JButton("重置");

        searchButton.addActionListener(e -> {
            String trainNumber = searchField.getText().trim();
            performSearch(trainNumber);
        });

        resetButton.addActionListener(e -> {
            searchField.setText("");
            resetToOriginalData();
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        jf.add(searchPanel, BorderLayout.NORTH);

        // 中间内容面板（使用垂直 BoxLayout）
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // 高铁/动车部分
        if (!highSpeedTickets.isEmpty()) {
            contentPanel.add(createSectionPanel("高铁", createHighSpeedTableModel()));
        } else {
            contentPanel.add(new JLabel("未找到高铁车次"));
        }
        contentPanel.add(Box.createVerticalStrut(20)); // 间距

        // 普通列车部分
        if (!commonTrainTickets.isEmpty()) {
            contentPanel.add(createSectionPanel("火车", createCommonTrainTableModel()));
        } else {
            contentPanel.add(new JLabel("未找到火车车次"));
        }

        // 放入滚动面板
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jf.add(scrollPane, BorderLayout.CENTER);

        // 窗口设置
        jf.setSize(900, 600);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }


    public JPanel createSectionPanel(String title, DefaultTableModel model) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 180, 255), 1),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("微软雅黑", Font.BOLD, 16),
                new Color(30, 144, 255)
        ));
        panel.setBackground(Color.WHITE);

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        table.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(800, Math.min(model.getRowCount() * 26 + 30, 250)));
        panel.add(tableScroll, BorderLayout.CENTER);

        return panel;
    }

    public DefaultTableModel createHighSpeedTableModel() {
        String[] columns = {"车次", "出发站", "到达站", "出发时间", "到达时间", "一等座剩余数量", "二等座剩余数量"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 只读
            }
        };

        for (HighSpeedTicket t : highSpeedTickets) {
            Object[] row = {
                    t.getTrainName(),
                    t.getStartStation(),
                    t.getEndStation(),
                    t.getStartTime(),
                    t.getEndTime(),
                    t.getMap().get("一等座"),
                    t.getMap().get("二等座")
            };
            model.addRow(row);
        }
        return model;
    }

    public DefaultTableModel createCommonTrainTableModel() {
        String[] columns = {"车次", "出发站", "到达站", "出发时间", "到达时间", "硬座剩余数量", "硬卧剩余数量"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (CommonTrainTicket t : commonTrainTickets) {
            Object[] row = {
                    t.getTrainName(),
                    t.getStartStation(),
                    t.getEndStation(),
                    t.getStartTime(),
                    t.getEndTime(),
                    t.getMap().get("硬座"),
                    t.getMap().get("硬卧")
            };
            model.addRow(row);
        }
        return model;
    }

    public void performSearch(String trainNumber) {
        if (trainNumber.isEmpty()) {
            // 如果输入为空，显示所有车次
            resetToOriginalData();
        } else {
            // 根据输入的车次筛选数据
            ArrayList<HighSpeedTicket> filteredHighSpeed = new ArrayList<>();
            ArrayList<CommonTrainTicket> filteredCommon = new ArrayList<>();

            for (HighSpeedTicket ticket : originalHighSpeedTickets) {
                if (ticket.getTrainName().toLowerCase().contains(trainNumber.toLowerCase())) {
                    filteredHighSpeed.add(ticket);
                }
            }

            for (CommonTrainTicket ticket : originalCommonTrainTickets) {
                if (ticket.getTrainName().toLowerCase().contains(trainNumber.toLowerCase())) {
                    filteredCommon.add(ticket);
                }
            }

            // 更新当前显示的数据
            this.highSpeedTickets = filteredHighSpeed;
            this.commonTrainTickets = filteredCommon;

            // 重新初始化界面
            refreshUI();
        }
    }

    public void resetToOriginalData() {
        this.highSpeedTickets = new ArrayList<>(originalHighSpeedTickets);
        this.commonTrainTickets = new ArrayList<>(originalCommonTrainTickets);
        refreshUI();
    }

    public void refreshUI() {
        // 重新创建内容面板
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // 高铁/动车部分
        if (!highSpeedTickets.isEmpty()) {
            contentPanel.add(createSectionPanel("高铁", createHighSpeedTableModel()));
        } else {
            contentPanel.add(new JLabel("未找到高铁车次"));
        }
        contentPanel.add(Box.createVerticalStrut(20)); // 间距

        // 普通列车部分
        if (!commonTrainTickets.isEmpty()) {
            contentPanel.add(createSectionPanel("火车", createCommonTrainTableModel()));
        } else {
            contentPanel.add(new JLabel("未找到火车车次"));
        }

        // 获取当前的滚动面板并更新内容
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // 仅替换中心组件，保留其他组件
        Component[] components = jf.getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JScrollPane) {
                jf.remove(comp);  // 只移除滚动面板
                break;
            }
        }

        // 重新添加滚动面板到中心位置
        jf.add(scrollPane, BorderLayout.CENTER);
        jf.revalidate();
        jf.repaint();
    }


}
