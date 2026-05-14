package character;

import monster.Monster;
import java.util.Random;

public class Archer extends GameCharacter {

	private Random random = new Random();
	
    public Archer(String name) {
        super(name, 120, 30, 40);
    }

    @Override
    public void attack(Object target) {
        Monster enemy = (Monster) target;
        enemy.takeDamage(attackPower);
    }

    public void rapidShot(Object target) {
        Monster enemy = (Monster) target;
        int skillDamage = attackPower + 15;
        enemy.takeDamage(skillDamage);
    }
    
    public boolean isDoubleAttack() {
        return random.nextInt(100) < 5;
    }
}