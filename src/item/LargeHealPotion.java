package item;

import character.GameCharacter;

public class LargeHealPotion extends Item {

    public LargeHealPotion() {
        super("대형 체력 포션", 100);
    }

    @Override
    public String use(Object target) {
        GameCharacter character = (GameCharacter) target;
        character.heal(value);
        return character.getName() + "의 HP가 " + value + " 회복됐습니다!";
    }

    @Override
    public String getDescription() {
        return "HP +" + value;
    }
}