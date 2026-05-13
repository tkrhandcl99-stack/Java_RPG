package character;

import monster.Monster;

public class Mage extends GameCharacter {

    public Mage(String name) {
        super(name, 100, 40, 60);
    }

    @Override
    public void attack(Object target) {
        Monster enemy = (Monster) target;
        enemy.takeDamage(attackPower);
    }

    public void magicExplosion(Object target) {
        Monster enemy = (Monster) target;
        int skillDamage = attackPower + 15;
        enemy.takeDamage(skillDamage);
    }
}