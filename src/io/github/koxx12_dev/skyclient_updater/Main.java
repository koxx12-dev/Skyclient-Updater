package io.github.koxx12_dev.skyclient_updater;


import org.apache.commons.lang3.SystemUtils;

import java.awt.*;
import java.io.Console;
import java.io.IOException;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class Main{

    public static void main(String[] args) throws IOException {
        Console console = System.console();

        if (SystemUtils.IS_OS_WINDOWS) {
            if (console == null && !GraphicsEnvironment.isHeadless()){
                String fileloc = Main.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
                Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + fileloc.replaceAll("%20"," ") + "\""});
            } else {
                Windows.main(new String[0]);
                System.out.println("Program has ended, please type 'exit' to close the console");
            }
        } else if (SystemUtils.IS_OS_LINUX) {
            if (console == null && !GraphicsEnvironment.isHeadless()){
                javax.swing.JOptionPane.showMessageDialog( null, "In this os\n you need to run it in terminal","Error",ERROR_MESSAGE);
            } else {
                Linux.main(new String[0]);
            }
        } else if (SystemUtils.IS_OS_MAC) {
            if (console == null && !GraphicsEnvironment.isHeadless()){
                javax.swing.JOptionPane.showMessageDialog( null, "In this os\n you need to run it in terminal","Error",ERROR_MESSAGE);
            } else {
                MacOS.main(new String[0]);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog( null, "Looks like ur OS isnt supported","Error",ERROR_MESSAGE);
            System.exit(0);
        }
    }
}