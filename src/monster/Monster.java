package monster;

import character.GameCharacter;

public abstract class Monster {
	protected String name;
	protected int hp;
	protected int maxHp;
	protected int attackPower;
	protected boolean alive;
	
	public Monster(String name, int hp, int attackPower) {
		this.name = name;
		this.hp = hp;
		this.maxHp = hp;
		this.attackPower = attackPower;
		this.alive = true;
	}
	
	public void takeDamage(int damage) {
		this.hp -= damage;
		if (this.hp <= 0) {
			this.hp = 0;
			this.alive = false;
		}
	}
	
	public abstract void attack(GameCharacter target);
	
	public boolean isAlive() { return alive; }
	public String getName() { return name; }
	public int getHp() { return hp; }
	public int getMaxHp() { return maxHp; }
	public int getAttackPower() { return attackPower; }
	
	@Override
	public String toString() {
		return name + " [HP: " + hp + "/" + maxHp + "]";
	}
}