package character;

import exception.DeadCharacterException;
import monster.Monster;

public class Warrior extends GameCharacter{
	public Warrior(String name) {
		super(name, 150, 25);
	}
	
	@Override
	public void attack(Object target) {
		Monster enemy = (Monster) target;
		if (!enemy.isAlive()) {
			throw new DeadCharacterException(enemy.getName());
		}
		System.out.println(name + "의 검 공격! " + attackPower + " 데미지!");
		enemy.takeDamage(attackPower);
	}
}