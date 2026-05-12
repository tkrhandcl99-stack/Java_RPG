package character;

import exception.DeadCharacterException;
import monster.Monster;

public class Mage extends GameCharacter{
	public Mage(String name) {
		super(name, 100, 40);
	}
	
	@Override
	public void attack(Object target) {
		Monster enemy = (Monster) target;
		if (!enemy.isAlive()) {
			throw new DeadCharacterException(enemy.getName());
		}
		System.out.println(name + "의 파이어볼! " + attackPower + " 데미지!");
		enemy.takeDamage(attackPower);
	}
}