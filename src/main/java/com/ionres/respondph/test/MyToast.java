package com.ionres.respondph.test;

import javax.swing.JWindow;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;

public class MyToast {
    public static void showToast(Component parent, String message, int duration) {
        JWindow toast = new JWindow();
        JLabel label = new JLabel(message);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 170));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        toast.add(label);
        toast.pack();

        Point location = parent.getLocationOnScreen();
        toast.setLocation(location.x + parent.getWidth()/2 - toast.getWidth()/2,
                          location.y + parent.getHeight() - 100);
        toast.setVisible(true);

        new Timer(duration, e -> toast.dispose()).start();
    }
}
