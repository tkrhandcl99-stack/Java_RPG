package character;

import exception.DeadCharacterException;
import monster.Monster;

public class Archer extends GameCharacter{
	public Archer(String name) {
		super(name, 120, 30);
	}
	
	@Override
	public void attack(Object target) {
		Monster enemy = (Monster) target;
		if (!enemy.isAlive()) {
			throw new DeadCharacterException(enemy.getName());
		}
		System.out.println(name + "의 화살 공격! " + attackPower + " 데미지!");
		enemy.takeDamage(attackPower);
	}
}