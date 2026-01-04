package com.wsh.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UIListener implements ActionListener {


    @Override
    public void actionPerformed(ActionEvent e) {
        String s=e.getActionCommand();
        switch (s) {
            case "查询余票":

                break;
            case "查询车次":
                break;
            case "查询站点":
                break;
            case "订票":
                break;
            case "退票":
                break;
            case "车票改签":
                break;
            default:
                break;
        }
    }

}
