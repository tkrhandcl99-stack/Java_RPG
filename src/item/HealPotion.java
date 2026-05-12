package item;

import character.GameCharacter;

public class HealPotion extends Item {
	public HealPotion() {
		super("체력 포션", 50);
	}
	
	@Override
	public void use(Object target) {
		GameCharacter character = (GameCharacter) target;
		character.heal(value);
		System.out.println(character.getName() + "의 HP가 " + value + " 회복됐습니다!");
	}
	
	@Override
	public String getDescription() {
		return "HP +" + value;
	}
}