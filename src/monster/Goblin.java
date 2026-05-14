package monster;

import character.GameCharacter;
import java.util.Random;

public class Goblin extends Monster {

    private Random random = new Random();

    public Goblin() {
        super("고블린", 60, 15);
    }

    public Goblin(int attackPower) {
        super("고블린", 60, attackPower);
    }

    // 15% 확률로 독 부여
    @Override
    public void attack(GameCharacter target) {
        target.takeDamage(attackPower);
    }
    public int getGoldDrop() { return 10; }
    public int getExpDrop() { return 15; }
}