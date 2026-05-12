package monster;

import character.GameCharacter;
import exception.DeadCharacterException;
import java.util.Random;

public class Goblin extends Monster {
	
	private Random random = new Random();
	
	public Goblin() {
		super("고블린", 60, 15);
	}
	
	public Goblin(int attackPower) {
		super("고블린", 60, attackPower);
	}
	
	@Override
	public void attack(GameCharacter target) {
		if (!target.isAlive()) {
			throw new DeadCharacterException(target.getName());
		}
		System.out.println(name + "의 할퀴기! " + attackPower + " 데미지!");
		target.takeDamage(attackPower);
		
		// 15% 확률로 독 부여
		if (random.nextInt(100) < 15) {
			target.addStatus("독");
		}
	}
}