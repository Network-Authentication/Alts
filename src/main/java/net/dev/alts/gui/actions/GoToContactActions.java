package net.dev.alts.gui.actions;

import net.dev.alts.gui.*;

import javax.swing.*;
import java.awt.event.*;

public class GoToContactActions implements ActionListener {
    private final JFrame gui;
    public GoToContactActions(JFrame gui){
        this.gui = gui;
    }
    public void actionPerformed(ActionEvent e) {
        gui.dispose();
        new ContactGui().ContactGui();
    }
}
