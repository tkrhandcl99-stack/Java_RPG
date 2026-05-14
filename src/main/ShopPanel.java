package main;

import javax.swing.*;
import java.awt.*;
import character.GameCharacter;
import inventory.Inventory;
import item.*;

public class ShopPanel extends JPanel {

    private GameCharacter player;
    private Inventory inventory;
    private JLabel goldLabel;
    private JTextArea logArea;
    private int nextStage;
    private boolean isEasy;
    private GameFrame frame;

    private String[][] shopItems = {
        {"체력 포션",     "HP +50",       "30",  "소모"},
        {"대형 체력 포션", "HP +100",      "60",  "소모"},
        {"공격력 포션",   "공격력 +20",    "50",  "소모"},
        {"나무 검",       "공격력 +5",     "40",  "무기"},
        {"천 갑옷",       "최대 HP +15",   "40",  "방어구"},
        {"철 검",         "공격력 +10",    "80",  "무기"},
        {"철 갑옷",       "최대 HP +30",   "80",  "방어구"},
        {"미스릴 장갑",   "공격력 +30",    "150", "장갑"}
    };

    public ShopPanel(GameCharacter player, Inventory inventory, int nextStage, boolean isEasy, GameFrame frame) {
        this.player = player;
        this.inventory = inventory;
        this.nextStage = nextStage;
        this.isEasy = isEasy;
        this.frame = frame;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 상단 - 제목 + 골드
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 30));

        JLabel title = new JLabel("⚔ 상점", SwingConstants.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        title.setForeground(Color.YELLOW);

        goldLabel = new JLabel("보유 골드: " + player.getGold() + "G", SwingConstants.RIGHT);
        goldLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        goldLabel.setForeground(new Color(255, 215, 0));

        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(goldLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 가운데 - 아이템 목록 + 로그
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBackground(new Color(30, 30, 30));

        // 아이템 목록
        JPanel itemPanel = new JPanel(new GridLayout(shopItems.length, 1, 5, 5));
        itemPanel.setBackground(new Color(40, 40, 40));
        itemPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "아이템 목록",
            0, 0, new Font("맑은 고딕", Font.BOLD, 13), Color.WHITE));

        for (String[] item : shopItems) {
            JPanel itemRow = new JPanel(new BorderLayout());
            itemRow.setBackground(new Color(40, 40, 40));
            itemRow.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

            JLabel itemLabel = new JLabel(item[0] + " - " + item[1]);
            itemLabel.setForeground(Color.WHITE);
            itemLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

            JButton buyBtn = new JButton(item[2] + "G");
            buyBtn.setFont(new Font("맑은 고딕", Font.BOLD, 12));
            buyBtn.setBackground(new Color(70, 130, 180));
            buyBtn.setForeground(Color.WHITE);
            buyBtn.setPreferredSize(new Dimension(70, 30));
            buyBtn.addActionListener(e -> buyItem(item[0], Integer.parseInt(item[2]), item[3]));

            itemRow.add(itemLabel, BorderLayout.CENTER);
            itemRow.add(buyBtn, BorderLayout.EAST);
            itemPanel.add(itemRow);
        }

        // 로그창
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(20, 20, 20));
        logArea.setForeground(Color.WHITE);
        logArea.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        logArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "구매 로그",
            0, 0, new Font("맑은 고딕", Font.BOLD, 13), Color.WHITE));

        centerPanel.add(itemPanel);
        centerPanel.add(scrollPane);
        add(centerPanel, BorderLayout.CENTER);

        // 하단 - 다음 스테이지 버튼
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(new Color(30, 30, 30));

        JButton nextBtn = new JButton("다음 스테이지로 →");
        nextBtn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        nextBtn.setBackground(new Color(60, 150, 60));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setPreferredSize(new Dimension(200, 50));
        nextBtn.addActionListener(e -> goNextStage());
        btnPanel.add(nextBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void goNextStage() {
        BattlePanel battlePanel = new BattlePanel(frame, player, isEasy, inventory, nextStage);
        frame.setContentPane(battlePanel);
        frame.revalidate();
        frame.repaint();
    }

    private void buyItem(String itemName, int price, String type) {
        // 장비류 중복 구매 방지
        if (type.equals("무기")) {
            if (player.getEquipped("무기") != null) {
                log("이미 무기를 장착하고 있습니다!");
                return;
            }
        } else if (type.equals("방어구")) {
            // 천갑옷 - 방어구 슬롯에 뭔가 있으면 구매 불가
            if (itemName.equals("천 갑옷") && player.getEquipped("방어구") != null) {
                log("이미 방어구를 장착하고 있습니다!");
                return;
            }
            // 철갑옷 - 철갑옷이 이미 있으면 구매 불가
            if (itemName.equals("철 갑옷") && player.getEquipped("방어구") != null
                && player.getEquipped("방어구").getName().equals("철 갑옷")) {
                log("이미 철 갑옷을 장착하고 있습니다!");
                return;
            }
        } else if (type.equals("장갑")) {
            if (player.getEquipped("장갑") != null) {
                log("이미 장갑을 장착하고 있습니다!");
                return;
            }
        }

        // 골드 확인
        if (!player.spendGold(price)) {
            log("골드가 부족합니다! (보유: " + player.getGold() + "G / 필요: " + price + "G)");
            return;
        }

        // 소모 아이템 인벤토리 확인
        if (type.equals("소모") && inventory.isFull()) {
            player.addGold(price);
            log("인벤토리가 가득 찼습니다!");
            return;
        }

        // 아이템 지급
        switch (itemName) {
            case "체력 포션":
                log(inventory.addItem(new HealPotion()));
                break;
            case "대형 체력 포션":
                log(inventory.addItem(new LargeHealPotion()));
                break;
            case "공격력 포션":
                log(inventory.addItem(new AttackPotion()));
                break;
            case "나무 검":
                player.equip("무기", new WoodenSword());
                log(new WoodenSword().use(player));
                break;
            case "천 갑옷":
                player.equip("방어구", new ClothArmor());
                log(new ClothArmor().use(player));
                break;
            case "철 검":
                player.equip("무기", new Weapon());
                log(new Weapon().use(player));
                break;
            case "철 갑옷":
                player.equip("방어구", new Armor());
                log(new Armor().use(player));
                break;
            case "미스릴 장갑":
                player.equip("장갑", new MithrilGlove());
                log(new MithrilGlove().use(player));
                break;
        }

        goldLabel.setText("보유 골드: " + player.getGold() + "G");
        log(itemName + " 구매 완료! (남은 골드: " + player.getGold() + "G)");
    }

    private void log(String message) {
        if (message == null || message.isEmpty()) return;
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}