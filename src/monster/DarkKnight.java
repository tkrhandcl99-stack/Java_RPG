package monster;

import character.GameCharacter;
import java.util.Random;

public class DarkKnight extends Monster {

    private Random random = new Random();
    private boolean usedStrongAttack = false;

    public DarkKnight() {
        super("다크나이트", 180, 35);
    }

    @Override
    public void attack(GameCharacter target) {
        int pattern = random.nextInt(100);
        if (pattern < 60) {
            usedStrongAttack = false;
            target.takeDamage(attackPower);
        } else {
            usedStrongAttack = true;
            target.takeDamage(attackPower + 15);
        }
    }

    public boolean isStrongAttack() { return usedStrongAttack; }

    public int getGoldDrop() { return 30; }
    public int getExpDrop() { return 35; }
}