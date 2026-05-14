package monster;

import character.GameCharacter;
import java.util.Random;

public class Dragon extends Monster {

    private Random random = new Random();
    private boolean usedStrongAttack = false;

    public Dragon() {
        super("드래곤", 350, 45);
    }

    public Dragon(int attackPower) {
        super("드래곤", 350, attackPower);
    }

    @Override
    public void attack(GameCharacter target) {
        int pattern = random.nextInt(100);
        if (pattern < 60) {
            usedStrongAttack = false;
            target.takeDamage(attackPower);
        } else {
            usedStrongAttack = true;
            target.takeDamage(attackPower + 20);
        }
    }

    public boolean isStrongAttack() { return usedStrongAttack; }

    public int getGoldDrop() { return 100; }
    public int getExpDrop() { return 30; }
}