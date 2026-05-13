package item;

import character.GameCharacter;

public class Armor extends Item {

    public Armor() {
        super("철 갑옷", 30);
    }

    @Override
    public String use(Object target) {
        GameCharacter character = (GameCharacter) target;
        character.boostMaxHp(value);
        character.heal(value);
        return character.getName() + "의 최대 HP가 " + value + " 증가했습니다!";
    }

    @Override
    public String getDescription() {
        return "최대 HP +" + value;
    }
}