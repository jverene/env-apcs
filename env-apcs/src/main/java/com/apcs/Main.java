package com.apcs;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image icon = Toolkit.getDefaultToolkit().getImage(
                    Main.class.getResource("/com/apcs/icons/icons8-key-50.png") // your resource path
            );
            TrayIcon trayIcon = new TrayIcon(icon, "ENV Password Manager");
            tray.add(trayIcon);
            trayIcon.setImageAutoSize(true); // scales to fit the menu bar
            System.out.println("System tray supported: " + SystemTray.isSupported());
        } catch (AWTException e) {
            System.err.println("System tray not supported: " + e.getMessage());
        }
    }
}