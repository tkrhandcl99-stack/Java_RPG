package monster;

import character.GameCharacter;

public class Dragon extends Monster {

    public Dragon() {
        super("드래곤", 200, 45);
    }

    public Dragon(int attackPower) {
        super("드래곤", 200, attackPower);
    }

    @Override
    public void attack(GameCharacter target) {
        target.takeDamage(attackPower);
    }

    public int getExpDrop() { return 30; }
}