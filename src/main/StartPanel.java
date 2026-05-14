package main;

import javax.swing.*;
import java.awt.*;
import character.GameCharacter;
import character.Warrior;
import character.Mage;
import character.Archer;

public class StartPanel extends JPanel {

    private GameFrame frame;
    private JTextField nameField;
    private JRadioButton warriorBtn, mageBtn, archerBtn;
    private JRadioButton easyBtn, hardBtn;

    public StartPanel(GameFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));

        // 타이틀
        JLabel title = new JLabel("Java RPG", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 36));
        title.setForeground(Color.YELLOW);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        // 가운데 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1, 5, 5));
        centerPanel.setBackground(new Color(30, 30, 30));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 100));

        // 캐릭터 선택 버튼 먼저 초기화
        warriorBtn = new JRadioButton("전사 (HP:150 / 공격력:25 / MP:30)");
        mageBtn = new JRadioButton("마법사 (HP:100 / 공격력:40 / MP:60)");
        archerBtn = new JRadioButton("궁수 (HP:120 / 공격력:30 / MP:40)");

        styleRadioButton(warriorBtn);
        styleRadioButton(mageBtn);
        styleRadioButton(archerBtn);

        warriorBtn.setSelected(true);

        ButtonGroup charGroup = new ButtonGroup();
        charGroup.add(warriorBtn);
        charGroup.add(mageBtn);
        charGroup.add(archerBtn);

        // 캐릭터 패널 - 가운데 정렬
        JPanel charPanel = new JPanel();
        charPanel.setLayout(new BoxLayout(charPanel, BoxLayout.Y_AXIS));
        charPanel.setBackground(new Color(30, 30, 30));
        charPanel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel charLabel = new JLabel("캐릭터 선택");
        charLabel.setForeground(Color.WHITE);
        charLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        charLabel.setAlignmentX(CENTER_ALIGNMENT);

        warriorBtn.setAlignmentX(CENTER_ALIGNMENT);
        mageBtn.setAlignmentX(CENTER_ALIGNMENT);
        archerBtn.setAlignmentX(CENTER_ALIGNMENT);

        charPanel.add(charLabel);
        charPanel.add(Box.createVerticalStrut(5));
        charPanel.add(warriorBtn);
        charPanel.add(mageBtn);
        charPanel.add(archerBtn);

        // 이름 입력 - 가운데 정렬
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.setBackground(new Color(30, 30, 30));
        JLabel nameLabel = new JLabel("이름 입력: ");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        nameField = new JTextField(15);
        nameField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        // 난이도 선택 - 가운데 정렬
        easyBtn = new JRadioButton("Easy");
        hardBtn = new JRadioButton("Hard");
        styleRadioButton(easyBtn);
        styleRadioButton(hardBtn);
        easyBtn.setSelected(true);

        ButtonGroup diffGroup = new ButtonGroup();
        diffGroup.add(easyBtn);
        diffGroup.add(hardBtn);

        JPanel diffPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        diffPanel.setBackground(new Color(30, 30, 30));
        JLabel diffLabel = new JLabel("난이도: ");
        diffLabel.setForeground(Color.WHITE);
        diffLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        diffPanel.add(diffLabel);
        diffPanel.add(easyBtn);
        diffPanel.add(hardBtn);

        // 시작 버튼 - 가운데 정렬
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(new Color(30, 30, 30));
        JButton startBtn = new JButton("시작하기");
        startBtn.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        startBtn.setBackground(new Color(70, 130, 180));
        startBtn.setForeground(Color.WHITE);
        startBtn.setPreferredSize(new Dimension(200, 50));
        startBtn.addActionListener(e -> startGame());
        btnPanel.add(startBtn);

        centerPanel.add(charPanel);
        centerPanel.add(namePanel);
        centerPanel.add(diffPanel);
        centerPanel.add(btnPanel);

        add(centerPanel, BorderLayout.CENTER);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // 배경 이미지 불러오기
        try {
            java.io.File imgFile = new java.io.File("images/background.jpg");
            if (imgFile.exists()) {
                java.awt.image.BufferedImage bg = javax.imageio.ImageIO.read(imgFile);
                g2d.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        } catch (Exception e) {
            // 이미지 없으면 기존 그라디언트 배경
            GradientPaint gp = new GradientPaint(
                0, 0, new Color(5, 5, 20),
                0, getHeight(), new Color(20, 20, 50));
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        // 어두운 오버레이 추가 (글자 잘 보이게)
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void styleRadioButton(JRadioButton btn) {
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 30, 30));
        btn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
    }

    private void startGame() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름을 입력하세요!", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }

        GameCharacter player;
        if (warriorBtn.isSelected()) {
            player = new Warrior(name);
        } else if (mageBtn.isSelected()) {
            player = new Mage(name);
        } else {
            player = new Archer(name);
        }

        boolean isEasy = easyBtn.isSelected();
        frame.showBattlePanel(player, isEasy);
    }
}