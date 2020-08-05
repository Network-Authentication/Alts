package net.dev.alts.gui.actions;

import net.dev.alts.gui.*;

import javax.swing.*;
import java.awt.event.*;

public class GoToLoginActions implements ActionListener {
    private final JFrame gui;
    public GoToLoginActions(JFrame gui){
        this.gui = gui;
    }
    public void actionPerformed(ActionEvent e) {
        gui.dispose();
        new LoginGui().LoginGui();
    }
}
