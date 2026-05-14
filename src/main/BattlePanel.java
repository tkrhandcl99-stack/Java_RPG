package main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import character.GameCharacter;
import character.Warrior;
import character.Mage;
import character.Archer;
import monster.Monster;
import monster.Goblin;
import monster.Dragon;
import monster.TreeGuard;
import monster.WildJaewon;
import monster.DarkKnight;
import inventory.Inventory;
import item.HealPotion;
import item.AttackPotion;
import item.Weapon;
import item.Armor;
import item.MithrilGlove;
import item.LargeHealPotion;
import item.WoodenSword;
import item.ClothArmor;

public class BattlePanel extends JPanel {

    private GameFrame frame;
    private GameCharacter player;
    private boolean isEasy;
    private Inventory inventory;
    private ArrayList<Monster> currentMonsters;
    private int currentStage = 1;
    private boolean stageChanged = false;
    private MonsterBook monsterBook;

    // UI 컴포넌트
    private JLabel playerStatus;
    private JTextArea logArea;
    private JPanel monsterPanel;
    private JButton attackBtn, skillBtn, itemBtn, statusBtn;

    // 기본 생성자 - 스테이지 1부터 시작
    public BattlePanel(GameFrame frame, GameCharacter player, boolean isEasy) {
        this.frame = frame;
        this.player = player;
        this.isEasy = isEasy;
        this.monsterBook = new MonsterBook(); // ← 새로 생성

        logArea = new JTextArea();

        this.inventory = new Inventory();
        log(inventory.addItem(new HealPotion()));
        log(inventory.addItem(new HealPotion()));
        log(inventory.addItem(new AttackPotion()));

        initUI();
        startStage(1);
    }

    // 상점에서 돌아올 때 쓰는 생성자
    public BattlePanel(GameFrame frame, GameCharacter player, boolean isEasy, Inventory inventory, int startStage, MonsterBook monsterBook) {
        this.frame = frame;
        this.player = player;
        this.isEasy = isEasy;
        this.inventory = inventory;
        this.monsterBook = monsterBook; // ← 기존 도감 유지

        logArea = new JTextArea();

        initUI();
        startStage(startStage);
    }

    // UI 초기화 공통 메서드
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 상단 - 플레이어 상태
        playerStatus = new JLabel();
        playerStatus.setForeground(Color.YELLOW);
        playerStatus.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        playerStatus.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(playerStatus, BorderLayout.NORTH);

        // 가운데 - 몬스터 + 로그
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        centerPanel.setBackground(new Color(30, 30, 30));

        // 몬스터 패널
        monsterPanel = new JPanel();
        monsterPanel.setBackground(new Color(50, 50, 50));
        monsterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "몬스터",
            0, 0, new Font("맑은 고딕", Font.BOLD, 13), Color.WHITE));
        centerPanel.add(monsterPanel);

        // 로그창
        logArea.setEditable(false);
        logArea.setBackground(new Color(20, 20, 20));
        logArea.setForeground(Color.WHITE);
        logArea.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        logArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "전투 로그",
            0, 0, new Font("맑은 고딕", Font.BOLD, 13), Color.WHITE));
        centerPanel.add(scrollPane);
        add(centerPanel, BorderLayout.CENTER);

        // 하단 - 버튼
        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        btnPanel.setBackground(new Color(30, 30, 30));

        attackBtn = createButton("공격", new Color(180, 60, 60));
        skillBtn = createButton("스킬", new Color(60, 60, 180));
        itemBtn = createButton("아이템", new Color(60, 150, 60));
        statusBtn = createButton("상태 확인", new Color(100, 100, 100));

        attackBtn.addActionListener(e -> onAttack());
        skillBtn.addActionListener(e -> onSkill());
        itemBtn.addActionListener(e -> onItem());
        statusBtn.addActionListener(e -> onStatus());

        btnPanel.add(attackBtn);
        btnPanel.add(skillBtn);
        btnPanel.add(itemBtn);
        btnPanel.add(statusBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(0, 50));
        return btn;
    }

    private void startStage(int stage) {
        stageChanged = true;
        currentStage = stage;
        currentMonsters = new ArrayList<>();

        switch (stage) {
            case 1:
                log("====== 스테이지 1 - 고블린 ======");
                currentMonsters.add(isEasy ? new Goblin(10) : new Goblin());
                currentMonsters.add(isEasy ? new Goblin(10) : new Goblin());
                break;
            case 2:
                log("====== 스테이지 2 ======");
                if (Math.random() < 0.3) {
                    log("야생의 이재원이 나타났다!");
                    log("야생의 이재원의 기습! 조심하세요!");
                    WildJaewon jaewon = new WildJaewon();
                    currentMonsters.add(jaewon);
                    jaewon.attack(player);
                    log("야생의 이재원의 빠따질! " + jaewon.getAttackPower() + " 데미지! 남은 HP: " + player.getHp());
                    if (!player.isAlive()) {
                        setButtonsEnabled(false);
                        int result = JOptionPane.showConfirmDialog(
                            this, "기습에 쓰러졌습니다! 다시 도전하시겠습니까?", "게임 오버",
                            JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            player.heal(player.getMaxHp() / 2);
                            log("체력을 " + player.getMaxHp() / 2 + " 회복하고 재도전합니다!");
                            startStage(currentStage);
                            setButtonsEnabled(true);
                        } else {
                            log("게임을 종료합니다.");
                        }
                    }
                } else {
                    currentMonsters.add(new TreeGuard());
                }
                break;
            case 3:
                log("====== 스테이지 3 - 다크나이트 ======");
                currentMonsters.add(new DarkKnight());
                break;
            case 4:
                log("====== 스테이지 4 - 보스 드래곤 ======");
                currentMonsters.add(isEasy ? new Dragon(20) : new Dragon());
                break;
        }

        refreshUI();
    }

    private void onAttack() {
        ArrayList<Monster> aliveMonsters = getAliveMonsters();
        if (aliveMonsters.isEmpty()) return;

        if (aliveMonsters.size() == 1) {
            attackMonster(currentMonsters.indexOf(aliveMonsters.get(0)));
        } else {
            String[] options = getAliveMonsterNames();
            String choice = (String) JOptionPane.showInputDialog(
                this, "어떤 몬스터를 공격할까요?", "몬스터 선택",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice != null) {
                int index = getMonsterIndex(choice);
                attackMonster(index);
            }
        }
    }

    private void onSkill() {
        ArrayList<Monster> aliveMonsters = getAliveMonsters();
        if (aliveMonsters.isEmpty()) return;

        if (aliveMonsters.size() == 1) {
            skillMonster(currentMonsters.indexOf(aliveMonsters.get(0)));
        } else {
            String[] options = getAliveMonsterNames();
            String choice = (String) JOptionPane.showInputDialog(
                this, "어떤 몬스터에게 스킬을 사용할까요?", "몬스터 선택",
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (choice != null) {
                int index = getMonsterIndex(choice);
                skillMonster(index);
            }
        }
    }

    private ArrayList<Monster> getAliveMonsters() {
        ArrayList<Monster> alive = new ArrayList<>();
        for (Monster m : currentMonsters) {
            if (m.isAlive()) alive.add(m);
        }
        return alive;
    }

    private void onItem() {
        if (inventory.isEmpty()) {
            log("사용할 아이템이 없습니다.");
            return;
        }
        String[] items = getItemNames();
        String choice = (String) JOptionPane.showInputDialog(
            this, "어떤 아이템을 사용할까요?", "아이템 선택",
            JOptionPane.PLAIN_MESSAGE, null, items, items[0]);
        if (choice != null) {
            int index = getItemIndex(choice);
            log(inventory.getItem(index).use(player));
            inventory.removeItem(index);
            refreshUI();
        }
    }

    private void onStatus() {
        log("===== 상태 확인 =====");
        log(player.toString());
        log(player.showStatus());
        log(player.showEquipped());
        log(monsterBook.show());
        log("===================");
    }

    private void attackMonster(int index) {
        Monster monster = currentMonsters.get(index);
        if (!monster.isAlive()) {
            log("이미 쓰러진 몬스터입니다.");
            return;
        }

        stageChanged = false;
        String attackMsg = getPlayerAttackMessage(monster);
        player.attack(monster);
        log(attackMsg);
        log(monster.getName() + " 남은 HP: " + monster.getHp());

        // 궁수 패시브
        if (player instanceof Archer && ((Archer) player).isDoubleAttack() && monster.isAlive()) {
            log("[ 패시브 발동! 연속 공격! ]");
            player.attack(monster);
            log(getPlayerAttackMessage(monster));
            log(monster.getName() + " 남은 HP: " + monster.getHp());
        }

        checkMonsterDead(monster);
        if (!stageChanged && !allMonstersDead() && player.isAlive()) {
            monsterTurn();
        }
        refreshUI();
    }

    private void skillMonster(int index) {
        Monster monster = currentMonsters.get(index);
        if (!monster.isAlive()) {
            log("이미 쓰러진 몬스터입니다.");
            return;
        }

        stageChanged = false;

        if (player instanceof Warrior) {
            if (!player.useMp(10)) {
                log("MP가 부족합니다! (현재 MP: " + player.getMp() + ")");
                return;
            }
            ((Warrior) player).shieldBash(monster);
        } else if (player instanceof Mage) {
            if (!player.useMp(20)) {
                log("MP가 부족합니다! (현재 MP: " + player.getMp() + ")");
                return;
            }
            ((Mage) player).magicExplosion(monster);
        } else if (player instanceof Archer) {
            if (!player.useMp(15)) {
                log("MP가 부족합니다! (현재 MP: " + player.getMp() + ")");
                return;
            }
            ((Archer) player).rapidShot(monster);
        }

        log(getPlayerSkillMessage(monster));
        log(monster.getName() + " 남은 HP: " + monster.getHp());

        // 궁수 패시브 스킬
        if (player instanceof Archer && ((Archer) player).isDoubleAttack() && monster.isAlive()) {
            log("[ 패시브 발동! 연속 스킬! ]");
            ((Archer) player).rapidShot(monster);
            log(getPlayerSkillMessage(monster));
            log(monster.getName() + " 남은 HP: " + monster.getHp());
        }

        // 마법사 패시브
        if (player instanceof Mage) {
            ((Mage) player).recoverMpPassive();
            log("[ 패시브 발동! MP 10 회복! ]");
        }

        checkMonsterDead(monster);
        if (!stageChanged && !allMonstersDead() && player.isAlive()) {
            monsterTurn();
        }
        refreshUI();
    }

    private void checkMonsterDead(Monster monster) {
        if (!monster.isAlive()) {
            log(monster.getName() + "을(를) 처치했습니다!");
            monsterBook.record(monster.getName());
            log(giveRewards(monster));
        }
        if (allMonstersDead()) {
            if (currentStage != 4) {
                if (inventory.isFull()) {
                    log("인벤토리가 가득 차 체력 포션을 받을 수 없습니다!");
                } else {
                    log(inventory.addItem(new HealPotion()));
                }
            }
            stageClear();
        }
    }

    private String giveRewards(Monster monster) {
        String expMsg = "";
        int gold = 0;

        if (monster instanceof Goblin) {
            expMsg = player.gainExp(((Goblin) monster).getExpDrop());
            gold = ((Goblin) monster).getGoldDrop();
        } else if (monster instanceof Dragon) {
            expMsg = player.gainExp(((Dragon) monster).getExpDrop());
            gold = ((Dragon) monster).getGoldDrop();
        } else if (monster instanceof WildJaewon) {
            expMsg = player.gainExp(((WildJaewon) monster).getExpDrop());
            gold = ((WildJaewon) monster).getGoldDrop();
            log(expMsg);
            log("골드 " + gold + "G 획득!");
            player.addGold(gold);
            return equipMithrilGlove();
        } else if (monster instanceof TreeGuard) {
            expMsg = player.gainExp(((TreeGuard) monster).getExpDrop());
            gold = ((TreeGuard) monster).getGoldDrop();
        } else if (monster instanceof DarkKnight) {
            expMsg = player.gainExp(((DarkKnight) monster).getExpDrop());
            gold = ((DarkKnight) monster).getGoldDrop();
        }

        player.addGold(gold);
        log("골드 " + gold + "G 획득!");
        return expMsg;
    }

    private String equipMithrilGlove() {
        MithrilGlove glove = new MithrilGlove();
        if (player.getEquipped("장갑") == null) {
            player.equip("장갑", glove);
            log(glove.use(player));
            return "미스릴 장갑 획득 및 자동 장착!";
        } else {
            return "이미 장갑을 장착하고 있습니다.";
        }
    }

    private void monsterTurn() {
        for (Monster monster : currentMonsters) {
            if (monster.isAlive() && player.isAlive()) {
                String attackMsg = getMonsterAttackMessage(monster);

                // 전사 패시브
                if (player instanceof Warrior && ((Warrior) player).isDefend()) {
                    log("[ 패시브 발동! 공격 무효화! ]");
                    continue;
                }

                monster.attack(player);
                log(attackMsg + " 남은 HP: " + player.getHp());

                if (monster instanceof Goblin) {
                    if (Math.random() < 0.15) {
                        log(player.addStatus("독"));
                    }
                }
            }
        }
        applyPoison();
        player.recoverMp();

        if (!player.isAlive()) {
            setButtonsEnabled(false);

            String message = "<html><div style='text-align:center;'>"
                + "===== 게임 오버 =====<br><br>"
                + "[ 최종 스탯 ]<br>"
                + player.toString() + "<br><br>"
                + "[ 도달한 스테이지 ]<br>"
                + "스테이지 " + currentStage + "<br><br>"
                + "[ 장착 장비 ]<br>"
                + player.showEquipped().replace("\n", "<br>") + "<br><br>"
                + "[ 몬스터 도감 ]<br>"
                + monsterBook.show().replace("\n", "<br>") + "<br><br>"
                + "다시 도전하시겠습니까?"
                + "</div></html>";

            int result = JOptionPane.showConfirmDialog(
                this, new JLabel(message), "게임 오버",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                player.heal(player.getMaxHp() / 2);
                log("체력을 " + player.getMaxHp() / 2 + " 회복하고 재도전합니다!");
                startStage(currentStage);
                setButtonsEnabled(true);
            } else {
                log("게임을 종료합니다.");
            }
        }
    }

    private String getMonsterAttackMessage(Monster monster) {
        if (monster instanceof Goblin) {
            return monster.getName() + "의 할퀴기! " + monster.getAttackPower() + " 데미지!";
        } else if (monster instanceof Dragon) {
            Dragon dragon = (Dragon) monster;
            if (dragon.isStrongAttack()) {
                return monster.getName() + "의 불꽃 폭풍! " + (monster.getAttackPower() + 20) + " 데미지! 강력한 공격!";
            } else {
                return monster.getName() + "의 화염 브레스! " + monster.getAttackPower() + " 데미지!";
            }
        } else if (monster instanceof TreeGuard) {
            return monster.getName() + "의 나무 주먹! " + monster.getAttackPower() + " 데미지!";
        } else if (monster instanceof DarkKnight) {
            DarkKnight dk = (DarkKnight) monster;
            if (dk.isStrongAttack()) {
                return monster.getName() + "의 암흑 폭발! " + (monster.getAttackPower() + 15) + " 데미지! 강력한 공격!";
            } else {
                return monster.getName() + "의 암흑 검격! " + monster.getAttackPower() + " 데미지!";
            }
        } else if (monster instanceof WildJaewon) {
            return monster.getName() + "의 빠따질! " + monster.getAttackPower() + " 데미지!";
        }
        return monster.getName() + "의 공격! " + monster.getAttackPower() + " 데미지!";
    }

    private String getPlayerAttackMessage(Monster monster) {
        if (player instanceof Warrior) {
            return player.getName() + "의 검 공격! " + player.getAttackPower() + " 데미지!";
        } else if (player instanceof Mage) {
            return player.getName() + "의 파이어볼! " + player.getAttackPower() + " 데미지!";
        } else if (player instanceof Archer) {
            return player.getName() + "의 화살 공격! " + player.getAttackPower() + " 데미지!";
        }
        return player.getName() + "의 공격! " + player.getAttackPower() + " 데미지!";
    }

    private String getPlayerSkillMessage(Monster monster) {
        if (player instanceof Warrior) {
            return player.getName() + "의 방패 가격! " + (player.getAttackPower() + 15) + " 데미지!";
        } else if (player instanceof Mage) {
            return player.getName() + "의 마법 폭발! " + (player.getAttackPower() + 15) + " 데미지!";
        } else if (player instanceof Archer) {
            return player.getName() + "의 연속 사격! " + (player.getAttackPower() + 15) + " 데미지!";
        }
        return player.getName() + "의 스킬! " + (player.getAttackPower() + 15) + " 데미지!";
    }

    private void applyPoison() {
        if (player.hasStatus("독")) {
            player.takeDamage(5);
            log(player.getName() + "이(가) 독 데미지 5를 받았습니다! 남은 HP: " + player.getHp());
        }
    }

    private void stageClear() {
        player.removeStatus("독");
        log("모든 상태이상이 해제되었습니다!");
        log(dropEquipment());

        if (currentStage == 4) {
            String clearMessage = "<html><div style='text-align:center;'>"
                + "🎉 게임 클리어! 🎉<br><br>"
                + "[ 최종 스탯 ]<br>"
                + player.toString() + "<br><br>"
                + "[ 장착 장비 ]<br>"
                + player.showEquipped().replace("\n", "<br>") + "<br><br>"
                + "[ 몬스터 도감 ]<br>"
                + monsterBook.show().replace("\n", "<br>")
                + "</div></html>";

            JOptionPane.showMessageDialog(this,
                new JLabel(clearMessage), "축하합니다!",
                JOptionPane.PLAIN_MESSAGE);
            return;
        }

        String[] options = {"휴식 (HP +30, MP 풀회복)", "수련 (공격력 +5)", "이전 스테이지 재도전", "상점"};
        String choice = (String) JOptionPane.showInputDialog(
            this, "다음 행동을 선택하세요.", "스테이지 클리어!",
            JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (choice != null) {
            if (choice.contains("휴식")) {
                player.heal(30);
                player.fullRecoverMp();
                log("휴식! HP +30, MP 풀회복!");
                startStage(currentStage + 1);
            } else if (choice.contains("수련")) {
                player.boostAttack(5);
                log("수련 완료! 공격력 +5!");
                startStage(currentStage + 1);
            } else if (choice.contains("상점")) {
                showShop();
            } else {
                startStage(currentStage);
            }
        }
    }

    // 상점 열기 - monsterBook 전달
    private void showShop() {
        ShopPanel shopPanel = new ShopPanel(player, inventory, currentStage + 1, isEasy, frame, monsterBook);
        frame.setContentPane(shopPanel);
        frame.revalidate();
        frame.repaint();
    }

    private String dropEquipment() {
        int drop = (int)(Math.random() * 100);
        if (drop < 40) {
            Weapon weapon = new Weapon();
            if (player.getEquipped("무기") == null) {
                player.equip("무기", weapon);
                log(weapon.use(player));
                return "무기 [" + weapon.getName() + "] 획득 및 자동 장착!";
            } else {
                return "이미 무기를 장착하고 있어 드랍 아이템을 버렸습니다.";
            }
        } else if (drop < 80) {
            Armor armor = new Armor();
            if (player.getEquipped("방어구") == null) {
                player.equip("방어구", armor);
                log(armor.use(player));
                return "방어구 [" + armor.getName() + "] 획득 및 자동 장착!";
            } else {
                return "이미 방어구를 장착하고 있어 드랍 아이템을 버렸습니다.";
            }
        } else if (drop < 83) {
            MithrilGlove glove = new MithrilGlove();
            if (player.getEquipped("장갑") == null) {
                player.equip("장갑", glove);
                log(glove.use(player));
                return "미스릴 장갑 획득 및 자동 장착!";
            } else {
                return "이미 장갑을 장착하고 있어 드랍 아이템을 버렸습니다.";
            }
        } else {
            return "장비를 획득하지 못했습니다.";
        }
    }

    private boolean allMonstersDead() {
        for (Monster m : currentMonsters) {
            if (m.isAlive()) return false;
        }
        return true;
    }

    private String[] getAliveMonsterNames() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < currentMonsters.size(); i++) {
            Monster m = currentMonsters.get(i);
            if (m.isAlive()) {
                names.add((i + 1) + ". " + m.getName() + " (HP: " + m.getHp() + ")");
            }
        }
        return names.toArray(new String[0]);
    }

    private int getMonsterIndex(String choice) {
        int num = Integer.parseInt(choice.substring(0, 1));
        return num - 1;
    }

    private String[] getItemNames() {
        String[] names = new String[inventory.getSize()];
        for (int i = 0; i < inventory.getSize(); i++) {
            names[i] = inventory.getItem(i).toString();
        }
        return names;
    }

    private int getItemIndex(String choice) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i).toString().equals(choice)) return i;
        }
        return 0;
    }

    private void setButtonsEnabled(boolean enabled) {
        attackBtn.setEnabled(enabled);
        skillBtn.setEnabled(enabled);
        itemBtn.setEnabled(enabled);
        statusBtn.setEnabled(enabled);
    }

    private void refreshUI() {
        playerStatus.setText(player.toString());

        monsterPanel.removeAll();
        for (Monster m : currentMonsters) {
            JLabel label = new JLabel(m.isAlive() ? m.toString() : m.getName() + " [사망]");
            label.setForeground(m.isAlive() ? Color.WHITE : Color.RED);
            label.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
            monsterPanel.add(label);
        }
        monsterPanel.revalidate();
        monsterPanel.repaint();
    }

    public void log(String message) {
        if (message == null || message.isEmpty()) return;
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}