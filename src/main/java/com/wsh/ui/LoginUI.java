package com.wsh.ui;

import com.wsh.listener.LoginListener;

import javax.swing.*;
import java.awt.*;

public class LoginUI {
    public LoginUI(){
        init();
    }
    public void init(){
        JFrame jf = new JFrame("登录窗口");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setBounds(100,100,500,500);
        jf.setLocationRelativeTo( null);
        jf.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        // 使用背景面板
        BackgroundPanel backgroundPanel = new BackgroundPanel("D:\\javacode\\BookTicket\\src\\main\\resources\\Picture\\login2.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        mainPanel.setOpaque(false); // 设置为透明
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);
        jf.add(backgroundPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 组件间距
        // 标题
        JLabel titleLabel = new JLabel("用户登录");
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 30));
        titleLabel.setForeground(new Color(223, 11, 11)); // 红
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        // 用户名
        JLabel userLabel = new JLabel("用户名:");
        userLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 14));
        userLabel.setForeground(new Color(27, 1, 1));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        mainPanel.add(userLabel, gbc);
        JTextField usernameField = new JTextField(15);
        usernameField.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(usernameField, gbc);
        // 密码
        JLabel pwdLabel = new JLabel("密  码:");
        pwdLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        pwdLabel.setForeground(new Color(29, 1, 6));
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(pwdLabel, gbc);
        JTextField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 2;
        mainPanel.add(passwordField, gbc);
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JButton loginBtn = new JButton("登录");
        JButton registerBtn = new JButton("注册");
        // 设置按钮样式
        styleButton(loginBtn);
        styleButton(registerBtn);
        LoginListener loginListener = new LoginListener(usernameField, passwordField, jf);
        loginBtn.addActionListener(loginListener);
        registerBtn.addActionListener(loginListener);
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);
        jf.setVisible(true);
    }

    private static void styleButton(JButton button) {
        button.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 40));
        if ("登录".equals(button.getText())) {
            button.setBackground(new Color(211, 47, 47)); // 红色
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(240, 240, 240));
            button.setForeground(Color.BLACK);
        }
    }

}
