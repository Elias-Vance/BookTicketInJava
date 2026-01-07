package com.wsh.ui;

import com.wsh.listener.LoginListener;
import com.wsh.pojo.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BookTicketUI implements ActionListener {
    public JFrame mainFrame;
    private  ArrayList<HighSpeedTicket> highSpeedTickets;
    private  ArrayList<CommonTrainTicket> commonTrainTickets;
    private HighSpeedTicket highSpeedTicket;      // 当前选中的高铁票
    private CommonTrainTicket commonTrainTicket;    // 当前选中的普通车票
    private Ticket selectedTicket;             // 选中的车次号
    private JTextField nameField;
    private String seat;

    public BookTicketUI(ArrayList<HighSpeedTicket> tickets1, ArrayList<CommonTrainTicket> tickets2) {
        this.highSpeedTickets = tickets1;
        this.commonTrainTickets = tickets2;
        initUI();
    }

    private void initUI() {
        mainFrame = new JFrame("订票系统");
        mainFrame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout(10, 10));
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);

        // 搜索面板
        JPanel searchPanel = createSearchPanel();
        mainFrame.add(searchPanel, BorderLayout.NORTH);

        mainFrame.setVisible(true);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel searchLabel = new JLabel("输入车次:");
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("查询");

        searchButton.addActionListener(e -> {
            String trainNumber = searchField.getText().trim();
            findAndDisplayTrain(trainNumber);
        });

        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);

        return panel;
    }

    private void findAndDisplayTrain(String trainNumber) {
        if (trainNumber.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "请输入车次号", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 在高铁列表中查找
        HighSpeedTicket foundHighSpeed = null;
        for (HighSpeedTicket ticket : highSpeedTickets) {
            if (ticket.getTrainName().toLowerCase().equals(trainNumber.toLowerCase())) {
                foundHighSpeed = ticket;
                break;
            }
        }

        // 在普通列车列表中查找
        CommonTrainTicket foundCommon = null;
        for (CommonTrainTicket ticket : commonTrainTickets) {
            if (ticket.getTrainName().toLowerCase().equals(trainNumber.toLowerCase())) {
                foundCommon = ticket;
                break;
            }
        }

        if (foundHighSpeed != null) {
            // 找到高铁
            this.highSpeedTicket = foundHighSpeed;
            this.commonTrainTicket = null;
            this.selectedTicket = foundHighSpeed;
            updateUI(highSpeedTicket);  // 显示高铁信息
        } else if (foundCommon != null) {
            // 找到普通列车
            this.commonTrainTicket = foundCommon;
            this.highSpeedTicket = null;
            this.selectedTicket = foundCommon;
            updateUI(commonTrainTicket);  // 显示火车信息
        } else {
            // 未找到
            JOptionPane.showMessageDialog(mainFrame, "未找到车次: " + trainNumber, "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void updateUI(HighSpeedTicket  ticket){
        mainFrame.getContentPane().removeAll();
        // 车次信息面板
        JPanel infoPanel = createInfoPanel(ticket);
        mainFrame.add(infoPanel, BorderLayout.CENTER);
        // 座位选择面板
        JPanel seatPanel = createSeatPanel();
        mainFrame.add(seatPanel, BorderLayout.WEST);
        // 乘客信息面板
        JPanel passengerPanel = createPassengerPanel();
        mainFrame.add(passengerPanel, BorderLayout.EAST);
        // 按钮面板
        JPanel buttonPanel = createButtonPanel();
        mainFrame.add(buttonPanel, BorderLayout.SOUTH);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void updateUI(CommonTrainTicket  ticket){
        mainFrame.getContentPane().removeAll();
        // 车次信息面板
        JPanel infoPanel = createInfoPanel(ticket);
        mainFrame.add(infoPanel, BorderLayout.CENTER);
        // 座位选择面板
        JPanel seatPanel = createSeatPanel();
        mainFrame.add(seatPanel, BorderLayout.WEST);
        // 乘客信息面板
        JPanel passengerPanel = createPassengerPanel();
        mainFrame.add(passengerPanel, BorderLayout.EAST);
        // 按钮面板
        JPanel buttonPanel = createButtonPanel();
        mainFrame.add(buttonPanel, BorderLayout.SOUTH);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private JPanel createInfoPanel(Ticket  ticket) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "车次信息",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel routeLabel = new JLabel(ticket.getTrainName() +
                " - " + ticket.getStartStation() + " → " + ticket.getEndStation());
        routeLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(routeLabel, gbc);

        JLabel timeLabel = new JLabel("出发时间: " + ticket.getStartTime() +
                " | 到达时间: " + ticket.getEndTime());
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(timeLabel, gbc);

        JLabel priceLabel = new JLabel("票价: ¥" + ticket.getPrice());
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(priceLabel, gbc);

        return panel;
    }

    private JPanel createSeatPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "座位选择",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 创建按钮组实现单选
        ButtonGroup seatGroup = new ButtonGroup();

        // 根据车次类型显示不同的座位选项
        if (highSpeedTicket != null) {
            // 高铁座位选项
            String[] seatTypes = {"一等座", "二等座"};
            for (String seatType : seatTypes) {
                JPanel seatRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel seatLabel = new JLabel(seatType + ": " +
                        highSpeedTicket.getMap().get(seatType) + "张");
                JRadioButton seatRadio = new JRadioButton(seatType);
                seatRadio.addActionListener(this);

                // 将单选按钮添加到按钮组
                seatGroup.add(seatRadio);

                seatRow.add(seatLabel);
                seatRow.add(seatRadio);
                panel.add(seatRow);
            }
        } else if (commonTrainTicket != null) {
            // 火车座位选项
            String[] seatTypes = {"硬座", "硬卧"};
            for (String seatType : seatTypes) {
                JPanel seatRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel seatLabel = new JLabel(seatType + ": " +
                        commonTrainTicket.getMap().get(seatType) + "张");
                JRadioButton seatRadio = new JRadioButton(seatType);
                seatRadio.addActionListener(this);
                // 将单选按钮添加到按钮组
                seatGroup.add(seatRadio);

                seatRow.add(seatLabel);
                seatRow.add(seatRadio);
                panel.add(seatRow);
            }
        } else {
            // 未选择车次时显示提示
            panel.add(new JLabel("请先输入车次号"));
        }
        return panel;
    }


    private JPanel createPassengerPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "乘客信息",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // 姓名
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("姓名:"));
        JTextField nameField = new JTextField(15);
        this.nameField=nameField;
        namePanel.add(nameField);
        panel.add(namePanel);

        // 身份证号
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idPanel.add(new JLabel("身份证:"));
        JTextField idField = new JTextField(15);
        idPanel.add(idField);
        panel.add(idPanel);

        // 手机号
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel.add(new JLabel("手机:"));
        JTextField phoneField = new JTextField(15);
        phonePanel.add(phoneField);
        panel.add(phonePanel);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton bookButton = new JButton("确认订票");
        JButton cancelButton = new JButton("取消");

        bookButton.addActionListener(e -> confirmBooking());
        cancelButton.addActionListener(e -> mainFrame.dispose());

        panel.add(bookButton);
        panel.add(cancelButton);

        return panel;
    }

    private void confirmBooking() {
        if (highSpeedTicket == null && commonTrainTicket == null) {
            JOptionPane.showMessageDialog(mainFrame, "请先选择车次", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        } else if (nameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "请先填写信息", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 简单的订票确认逻辑
        int result = JOptionPane.showConfirmDialog(
                mainFrame,
                "确认订票信息?\n车次: " + selectedTicket.getTrainName() +
                        "\n出发站: " + selectedTicket.getStartStation() +
                        "\n到达站: " + selectedTicket.getEndStation() +
                        "\n出发时间: " + selectedTicket.getStartTime() +
                        "\n到达时间: " + selectedTicket.getEndTime() ,
                "确认订票",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            if(selectedTicket.getMap().get(seat)>0){
                selectedTicket.getMap().put(seat,selectedTicket.getMap().get(seat)-1);
                User user = LoginListener.user;
                user.setName(nameField.getText());
                UserTicket userTicket = new UserTicket();
                userTicket.setId(selectedTicket.getId());
                userTicket.setUserName(user.getUserName());
                userTicket.setTrainName(selectedTicket.getTrainName());
                userTicket.setStartStation(selectedTicket.getStartStation());
                userTicket.setEndStation(selectedTicket.getEndStation());
                userTicket.setStartTime(selectedTicket.getStartTime());
                userTicket.setSeatType(seat);
                userTicket.setPrice(selectedTicket.getPrice());
                userTicket.setEndTime(selectedTicket.getEndTime());
                user.getUserTickets().add(userTicket);
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\javacode\\BookTicket\\src\\main\\resources\\UserInfo\\userTickets", true));
                    writer.write(user.getUserName() + " " + user.getName() +  " "
                            + selectedTicket.getTrainName() + " " + selectedTicket.getStartStation()
                            + " " + selectedTicket.getEndStation() + " " + selectedTicket.getStartTime() + " "
                            + selectedTicket.getEndTime()+ " " + seat+ " " + selectedTicket.getPrice()+" "+selectedTicket.getId());
                    writer.newLine();
                    writer.flush();
                    writer.close();
                    updateTickets();
                    JOptionPane.showMessageDialog(
                            mainFrame,
                            "订票成功!",
                            "成功",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "没有座位啦!",
                        "失败",
                        JOptionPane.WARNING_MESSAGE
                );
            }
            mainFrame.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s=e.getActionCommand();
        seat=s;
    }

    public void updateTickets() throws IOException {
        String filePath = "src/main/resources/TicketInfo/tickets";
        // 读取所有行
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (int i = 0; i < lines.size(); i++) {
            if (i == selectedTicket.getId()) {
                String line = lines.get(i);
                StringTokenizer tokenizer = new StringTokenizer(line);
                StringBuilder stringBuilder = new StringBuilder();
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if (token.startsWith(seat)) {
                        stringBuilder.append(seat).append("=").append(selectedTicket.getMap().get(seat)).append(" ");
                    } else {
                        stringBuilder.append(token).append(" ");
                    }
                }
                lines.set(i, stringBuilder.toString());
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
    }
}
