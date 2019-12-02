package Saves;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Dialog.*;

public class SaveMenu extends JDialog {

    static SaveLoadButton[] saveArray = new SaveLoadButton[3];
    static ActionListener saveListener;
    static ActionListener loadListener;

    public SaveMenu() {
        setTitle("Save/Load Game");
        setModalityType(ModalityType.APPLICATION_MODAL);
        setSize(550, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        createButtons();
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setVisible(false);
    }

    public void showSaveMenu() {
      if(saveListener == null)
        saveListener = new SaveListener();
      for(int i = 0; i < 3; i++) {
        if(saveArray[i].getActionCommand().equals("unset")) {
          saveArray[i].setActionCommand("save" + i);
        } else {
          saveArray[i].removeListener(loadListener);
        }
        saveArray[i].addListener(saveListener);
      }
      setTitle("Save Game");
      this.setVisible(true);
    }

    public void showLoadMenu() {
      if(loadListener == null)
        loadListener = new LoadListener();
        for(int i = 0; i < 3; i++) {
          if(saveArray[i].getActionCommand().equals("unset")) {
            saveArray[i].setActionCommand("save" + i);
          } else {
            saveArray[i].removeListener(saveListener);
          }
          saveArray[i].addListener(loadListener);
        }
      setTitle("Load Game");
      this.setVisible(true);
    }

    public void createButtons() {
      for(int i = 0; i < 3; i++) {
        add(Box.createVerticalStrut(20));
        saveArray[i] = new SaveLoadButton("Empty");
        add(saveArray[i]);
      }
      add(Box.createVerticalStrut(20));
    }
}