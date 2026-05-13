package character;

import java.util.HashSet;
import java.util.HashMap;
import item.Item;

public abstract class GameCharacter implements Attackable {
	protected String name;
	protected int hp;
	protected int maxHp;
	protected int mp;
	protected int maxMp;
	protected int attackPower;
	protected int level;
	protected int exp;
	protected int expToLevelUp;
	protected boolean alive;
	protected HashSet<String> statusEffects;
	protected HashMap<String, Item> equippedItems;
	
	public GameCharacter(String name, int hp, int attackPower, int mp) {
		this.name = name;
		this.hp = hp;
		this.maxHp = hp;
		this.attackPower = attackPower;
		this.alive = true;
		this.mp = mp;
		this.maxMp = mp;
		this.level = 1;
		this.exp = 0;
		this.expToLevelUp = 30;
		this.statusEffects = new HashSet<>();
		this.equippedItems = new HashMap<>();
	}
	
	// 상태이상 추가 - 메시지 반환
	public String addStatus(String status) {
		if (statusEffects.add(status)) {
			return name + "에게 " + status + " 상태이상이 걸렸습니다!";
		} else {
			return name + "은(는) 이미 " + status + " 상태입니다.";
		}
	}
	
	// 상태이상 확인
	public boolean hasStatus(String status) {
		return statusEffects.contains(status);
	}
	
	// 상태이상 제거
	public void removeStatus(String status) {
		statusEffects.remove(status);
	}
	
	// 상태이상 출력 - 메시지 반환
	public String showStatus() {
		if (statusEffects.isEmpty()) {
			return "상태이상 없음";
		}
		return "상태이상: " + statusEffects;
	}
	
	// 장비 장착
	public void equip(String slot, Item item) {
		equippedItems.put(slot, item);
	}
	
	// 장착 장비 확인
	public Item getEquipped(String slot) {
		return equippedItems.get(slot);
	}
	
	// 장착 장비 출력 - 메시지 반환
	public String showEquipped() {
		if (equippedItems.isEmpty()) {
			return "장착된 장비가 없습니다.";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("==== 장착 장비 ====\n");
		for (String slot : equippedItems.keySet()) {
			sb.append(slot + ": " + equippedItems.get(slot).getName()).append("\n");
		}
		sb.append("==================");
		return sb.toString();
	}
	
	// 최대 HP 증가
	public void boostMaxHp(int amount) {
		this.maxHp += amount;
	}
	
	// MP 소모 - 부족하면 false 반환
	public boolean useMp(int amount) {
		if (this.mp < amount) {
			return false;
		}
		this.mp -= amount;
		return true;
	}
	
	// 매 턴 MP 5 회복
	public void recoverMp() {
		if (this.mp < this.maxMp) {
			this.mp += 5;
			if (this.mp > this.maxMp) this.mp = this.maxMp;
		}
	}
	
	// MP 풀회복
	public void fullRecoverMp() {
		this.mp = this.maxMp;
	}
	
	// 경험치 획득 및 레벨업 - 메시지 반환
	public String gainExp(int amount) {
		this.exp += amount;
		StringBuilder sb = new StringBuilder();
		sb.append(name + "이(가) 경험치 " + amount + "을(를) 획득했습니다!\n");
		sb.append("현재 경험치: " + exp + "/" + expToLevelUp);
		if (this.exp >= this.expToLevelUp) {
			sb.append("\n").append(levelUp());
		}
		return sb.toString();
	}
	
	// 레벨업 - 스탯 증가 및 메시지 반환
	private String levelUp() {
		this.level++;
		this.exp = 0;
		this.expToLevelUp += 5;
		this.hp += 20;
		this.maxHp += 20;
		this.attackPower += 5;
		this.mp += 10;
		this.maxMp += 10;
		return "==============================\n" +
			   "레벨 업! 현재 레벨: " + level + "\n" +
			   "HP +20 / 공격력 +5 / MP +10\n" +
			   "다음 레벨업까지 필요 경험치: " + expToLevelUp + "\n" +
			   "==============================";
	}
	
	// hp가 0 이하면 0으로 맞추고 사망 처리
	public void takeDamage(int damage) {
		this.hp -= damage;
		if (this.hp <= 0) {
			this.hp = 0;
			this.alive = false;
		}
	}
	
	// hp가 maxHp보다 크면 최대 체력으로 제한, 회복 시 alive 복구
	public void heal(int amount) {
		this.hp += amount;
		if (this.hp > maxHp) {
			this.hp = maxHp;
		}
		if (this.hp > 0) {
			this.alive = true;
		}
	}
	
	// 공격력 증가
	public void boostAttack(int amount) {
		this.attackPower += amount;
	}
	
	public boolean isAlive() { return alive; }
	public String getName() { return name; }
	public int getHp() { return hp; }
	public int getMaxHp() { return maxHp; }
	public int getMp() { return mp; }
	public int getMaxMp() { return maxMp; }
	public int getLevel() { return level; }
	public int getExp() { return exp; }
	public int getExpToLevelUp() { return expToLevelUp; }
	
	@Override
	public int getAttackPower() { return attackPower; }
	
	@Override
	public String toString() {
		return name + " Lv." + level +
			   " [HP: " + hp + "/" + maxHp + "]" +
			   " [MP: " + mp + "/" + maxMp + "]" +
			   " [공격력: " + attackPower + "]" +
			   " [EXP: " + exp + "/" + expToLevelUp + "]";
	}
}