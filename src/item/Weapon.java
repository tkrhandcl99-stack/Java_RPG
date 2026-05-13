package item;

import character.GameCharacter;

public class Weapon extends Item {

    public Weapon() {
        super("철 검", 10);
    }

    @Override
    public String use(Object target) {
        GameCharacter character = (GameCharacter) target;
        character.boostAttack(value);
        return character.getName() + "의 공격력이 " + value + " 증가했습니다!";
    }

    @Override
    public String getDescription() {
        return "공격력 +" + value;
    }
}