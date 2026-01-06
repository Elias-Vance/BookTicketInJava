package com.wsh.listener;

import com.wsh.ui.ClientUI;

import javax.net.ssl.KeyManager;
import javax.swing.*;
import javax.swing.text.Keymap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Set;

public class LoginListener implements ActionListener {
    private  JTextField userName;
    private  JTextField password;
    private HashMap<String,String> userInfo=new HashMap<>();
    private BufferedReader reader;
    private BufferedWriter writer;
    private JFrame jframe;

    public LoginListener(JTextField userName, JTextField password, JFrame jframe) {
        this.userName=userName;
        this.password=password;
        this.jframe=jframe;
        try {
            reader=new BufferedReader(new FileReader("D:\\javacode\\BookTicket\\src\\main\\resources\\UserInfo\\users"));
            String line;
            while ((line=reader.readLine())!=null){
                String[] info=line.split("[=]");
                userInfo.put(info[0],info[1]);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("未找到用户信息文件");
        } catch (IOException ex) {
            System.out.println("用户信息文件写入失败");
        }finally {
            try {
                reader.close();
                System.out.println("用户信息文件关闭成功");
                Set<String> keys=userInfo.keySet();
                for (String key:keys){
                    System.out.println(key+"--"+userInfo.get(key));
                }
            } catch (IOException ex) {
                System.out.println("用户信息文件关闭失败");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s=e.getActionCommand();
        String userName=this.userName.getText();
        String password=this.password.getText();
        switch (s) {
            case "登录":
                System.out.println("点击了登录");
                if (userInfo.containsKey(userName)){
                    if (userInfo.get(userName).equals(password)){
                        System.out.println("登录成功");
                        jframe.setVisible(false);
                        try {
                            new ClientUI();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }else{
                        System.out.println("密码错误");
                        JOptionPane.showMessageDialog(null, "密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }else {
                    System.out.println("用户不存在");
                    JOptionPane.showMessageDialog(null, "用户不存在！", "错误", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "注册":
                System.out.println("点击了注册");
                try {
                    writer=new BufferedWriter(new FileWriter("D:\\javacode\\BookTicket\\src\\main\\resources\\UserInfo\\users", true));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (userInfo.containsKey(userName)){
                    JOptionPane.showMessageDialog(null, "用户已存在！", "错误", JOptionPane.ERROR_MESSAGE);
                }else{
                    if(password.equals("")){
                        JOptionPane.showMessageDialog(null, "密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                    }else {
                        try {
                            writer.newLine();
                            writer.write(userName + "=" + password);
                            writer.flush();
                            userInfo.put(userName,password);
                            JOptionPane.showMessageDialog(null, "注册成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                            writer.close();
                        } catch (IOException ex) {
                            System.out.println("用户信息文件写入失败");
                        }
                    }
                }
                break;
        }
    }
}
