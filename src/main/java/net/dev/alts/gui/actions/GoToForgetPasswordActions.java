package net.dev.alts.gui.actions;

import net.dev.alts.gui.*;

import javax.swing.*;
import java.awt.event.*;

public class GoToForgetPasswordActions implements ActionListener {
    private final JFrame gui;
    public GoToForgetPasswordActions(JFrame gui){
        this.gui = gui;
    }
    public void actionPerformed(ActionEvent e) {
        gui.dispose();
        new ForgetPasswordGui().ForgetPasswordGui();
    }
}
