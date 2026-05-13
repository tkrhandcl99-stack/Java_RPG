package character;

import monster.Monster;

public class Warrior extends GameCharacter {

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
}