package monster;

import character.GameCharacter;

public class DarkKnight extends Monster {

    public DarkKnight() {
        super("다크나이트", 180, 35);
    }

    @Override
    public void attack(GameCharacter target) {
        target.takeDamage(attackPower);
    }

    public int getExpDrop() { return 35; }
}