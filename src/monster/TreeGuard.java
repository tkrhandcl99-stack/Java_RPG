package monster;

import character.GameCharacter;

public class TreeGuard extends Monster {

    public TreeGuard() {
        super("트리가드", 120, 30);
    }

    @Override
    public void attack(GameCharacter target) {
        target.takeDamage(attackPower);
    }
    
    public int getGoldDrop() { return 20; }
    public int getExpDrop() { return 25; }
}