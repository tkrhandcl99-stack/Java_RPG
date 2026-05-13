package character;

import monster.Monster;

public class Archer extends GameCharacter {

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
}