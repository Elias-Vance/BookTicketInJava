package com.wsh.ui;

import com.wsh.listener.UIListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientUI {
    public ClientUI(){
        init();
    }

    public void init(){
        JFrame jf = new JFrame("订票窗口");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setBounds(100,100,500,500);
        jf.setVisible(true);
        jf.setLocationRelativeTo( null);
        // 顶部标题
        JLabel header = new JLabel(" 火车票订票系统", JLabel.CENTER);
        header.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        header.setOpaque(true);
        header.setBackground(new Color(211, 47, 47)); // 铁路红
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // 功能按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        String[] buttons = {
                "查询余票",
                "查询车次",
                "查询站点",
                "订票",
                "退票",
                "车票改签"
        };

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(245, 245, 245));
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            btn.addActionListener(new UIListener());
            buttonPanel.add(btn);
        }

        // 底部状态栏
        JLabel status = new JLabel("© 2026 火车票系统 | 王圣涵组所持有", JLabel.CENTER);
        status.setFont(new Font("SansSerif", Font.PLAIN, 12));
        status.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        status.setForeground(Color.GRAY);

        jf.add(header, BorderLayout.NORTH);
        jf.add(buttonPanel, BorderLayout.CENTER);
        jf.add(status, BorderLayout.SOUTH);
    }



}
