package character;

import java.util.HashSet;

public abstract class GameCharacter implements Attackable {
	protected String name;
	protected int hp;
	protected int maxHp;
	protected int attackPower;
	protected boolean alive;
	protected HashSet<String> statusEffects;
	
	public GameCharacter(String name, int hp, int attackPower) {
		this.name = name;
		this.hp = hp;
		this.maxHp = hp;
		this.attackPower = attackPower;
		this.alive = true;
		this.statusEffects = new HashSet<>();
	}
	
	// 상태이상 추가
	public void addStatus(String status) {
		if (statusEffects.add(status)) {
			System.out.println(name + "에게 " + status + " 상태이상이 걸렸습니다!");
		} else {
			System.out.println(name + "은(는) 이미 " + status + " 상태입니다.");
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
	
	// 상태이상 출력
	public void showStatus() {
		if (statusEffects.isEmpty()) {
			System.out.println("상태이상 없음");
			return;
		}
		System.out.println("상태이상: " + statusEffects);
	}
	
	// hp가 0 아래면 0으로 맞추고 죽었다고 설정
	public void takeDamage(int damage) {
		this.hp -= damage;
		if (this.hp <= 0) {
			this.hp = 0;
			this.alive = false;
		}
	}
	// hp가 maxhp보다 크면 현재 체력을 최대 체력으로 바꿈
	public void heal(int amount) {
		this.hp += amount;
		if (this.hp > maxHp) {
			this.hp = maxHp;
		}
	}
	
	public void boostAttack(int amount) {
		this.attackPower += amount;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public String getName() { return name; }
	public int getHp() { return hp; }
	public int getMaxHp() { return maxHp; }
	
	@Override
	public int getAttackPower() { return attackPower; }
	
	@Override
	public String toString() {
		return name + " [HP: " + hp + "/" + maxHp + "] [공격력: " + attackPower + "]";
	}
}