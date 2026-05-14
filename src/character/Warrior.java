package character;

import monster.Monster;
import java.util.Random;

public class Warrior extends GameCharacter {

	private Random random = new Random();
	
    public Warrior(String name) {
        super(name, 150, 25, 30);
    }

    @Override
    public void attack(Object target) {
        Monster enemy = (Monster) target;
        enemy.takeDamage(attackPower);
    }

    public void shieldBash(Object target) {
        Monster enemy = (Monster) target;
        int skillDamage = attackPower + 15;
        enemy.takeDamage(skillDamage);
    }
    
    public boolean isDefend() {
        return random.nextInt(100) < 10;
    }
}