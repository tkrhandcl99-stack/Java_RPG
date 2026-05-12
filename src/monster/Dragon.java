package monster;

import character.GameCharacter;
import exception.DeadCharacterException;

public class Dragon extends Monster {
	
	public Dragon() {
		super("드래곤", 200, 30);
	}
	
	public Dragon(int attackPower) {
		super("드래곤", 200, attackPower);
	}
	
	@Override
	public void attack(GameCharacter target) {
		if (!target.isAlive()) {
			throw new DeadCharacterException(target.getName());
		}
		System.out.println(name + "의 화염 브레스! " + attackPower + " 데미지!");
		target.takeDamage(attackPower);
	}
}