package monster;

import character.GameCharacter;

public class WildJaewon extends Monster {

    public WildJaewon() {
        super("야생의 이재원", 60, 70);
    }

    @Override
    public void attack(GameCharacter target) {
        target.takeDamage(attackPower);
    }

    public int getGoldDrop() { return 50; }
    public int getExpDrop() { return 40; }
}