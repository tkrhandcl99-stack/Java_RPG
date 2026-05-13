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

public class BattlePanel extends JPanel {

    private GameFrame frame;
    private GameCharacter player;
    private boolean isEasy;
    private Inventory inventory;
    private ArrayList<Monster> currentMonsters;
    private int currentStage = 1;
    private boolean stageChanged = false; // 스테이지 변경 여부 플래그

    // UI 컴포넌트
    private JLabel playerStatus;
    private JTextArea logArea;
    private JPanel monsterPanel;
    private JButton attackBtn, skillBtn, itemBtn, statusBtn;

    public BattlePanel(GameFrame frame, GameCharacter player, boolean isEasy) {
        this.frame = frame;
        this.player = player;
        this.isEasy = isEasy;

        logArea = new JTextArea();

        // 인벤토리 세팅
        this.inventory = new Inventory();
        log(inventory.addItem(new HealPotion()));
        log(inventory.addItem(new HealPotion()));
        log(inventory.addItem(new AttackPotion()));

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

        // 스테이지 1 시작
        startStage(1);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(0, 50));
        return btn;
    }

    // 스테이지 시작 - stageChanged 플래그 설정
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
                    // 기습 공격 실제로 적용
                    jaewon.attack(player);
                    log("야생의 이재원의 빠따질! " + jaewon.getAttackPower() + " 데미지! 남은 HP: " + player.getHp());
                    // 기습으로 죽었을 경우 체크
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

    // 살아있는 몬스터만 반환
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
        log("===================");
    }

    private void attackMonster(int index) {
        Monster monster = currentMonsters.get(index);
        if (!monster.isAlive()) {
            log("이미 쓰러진 몬스터입니다.");
            return;
        }

        stageChanged = false; // 공격 시작 전 초기화
        String attackMsg = getPlayerAttackMessage(monster);
        player.attack(monster);
        log(attackMsg);
        log(monster.getName() + " 남은 HP: " + monster.getHp());

        checkMonsterDead(monster);

        // 스테이지 안 바뀌고 클리어 안됐고 플레이어 살아있을 때만 반격
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

        stageChanged = false; // 스킬 시작 전 초기화

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

        checkMonsterDead(monster);

        // 스테이지 안 바뀌고 클리어 안됐고 플레이어 살아있을 때만 반격
        if (!stageChanged && !allMonstersDead() && player.isAlive()) {
            monsterTurn();
        }
        refreshUI();
    }

    private void checkMonsterDead(Monster monster) {
        if (!monster.isAlive()) {
            log(monster.getName() + "을(를) 처치했습니다!");
            log(giveExp(monster));
        }
        if (allMonstersDead()) {
            // 몬스터 전부 처치 시 포션 지급
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

    private String giveExp(Monster monster) {
        if (monster instanceof Goblin) return player.gainExp(((Goblin) monster).getExpDrop());
        else if (monster instanceof Dragon) return player.gainExp(((Dragon) monster).getExpDrop());
        else if (monster instanceof WildJaewon) {
            String expMsg = player.gainExp(((WildJaewon) monster).getExpDrop());
            log(expMsg);
            return equipMithrilGlove();
        }
        else if (monster instanceof TreeGuard) return player.gainExp(((TreeGuard) monster).getExpDrop());
        else if (monster instanceof DarkKnight) return player.gainExp(((DarkKnight) monster).getExpDrop());
        return "";
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
                monster.attack(player);
                log(attackMsg + " 남은 HP: " + player.getHp());

                // 독 상태이상 부여 체크 (고블린)
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
            int result = JOptionPane.showConfirmDialog(
                this, "쓰러졌습니다! 다시 도전하시겠습니까?", "게임 오버",
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
    }

    // 몬스터 공격 메시지 직접 생성
    private String getMonsterAttackMessage(Monster monster) {
        if (monster instanceof Goblin) {
            return monster.getName() + "의 할퀴기! " + monster.getAttackPower() + " 데미지!";
        } else if (monster instanceof Dragon) {
            return monster.getName() + "의 화염 브레스! " + monster.getAttackPower() + " 데미지!";
        } else if (monster instanceof TreeGuard) {
            return monster.getName() + "의 나무 주먹! " + monster.getAttackPower() + " 데미지!";
        } else if (monster instanceof DarkKnight) {
            return monster.getName() + "의 암흑 검격! " + monster.getAttackPower() + " 데미지!";
        } else if (monster instanceof WildJaewon) {
            return monster.getName() + "의 빠따질! " + monster.getAttackPower() + " 데미지!";
        }
        return monster.getName() + "의 공격! " + monster.getAttackPower() + " 데미지!";
    }

    // 플레이어 공격 메시지 직접 생성
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

    // 플레이어 스킬 메시지 직접 생성
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
            JOptionPane.showMessageDialog(this,
                "게임 클리어!\n\n" + player.toString() + "\n" + player.showEquipped(),
                "축하합니다!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 스테이지 클리어 후 선택지
        String[] options = {"휴식 (HP +30, MP 풀회복)", "수련 (공격력 +5)", "이전 스테이지 재도전"};
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
            } else {
                // 이전 스테이지 재도전 - 포션 지급 없이 바로 재도전
                startStage(currentStage);
            }
        }
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