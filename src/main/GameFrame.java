package main;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("Java RPG");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null); // 화면 가운데 띄우기

        // 시작 화면으로 시작
        showStartPanel();

        setVisible(true);
    }

    public void showStartPanel() {
        StartPanel startPanel = new StartPanel(this);
        setContentPane(startPanel);
        revalidate();
        repaint();
    }

    public void showBattlePanel(character.GameCharacter player, boolean isEasy) {
        BattlePanel battlePanel = new BattlePanel(this, player, isEasy);
        setContentPane(battlePanel);
        revalidate();
        repaint();
    }
}