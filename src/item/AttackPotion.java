package item;

import character.GameCharacter;

public class AttackPotion extends Item {
	public AttackPotion() {
		super("공격력 포션", 20);
	}
	
	@Override
	public void use(Object target) {
		GameCharacter character = (GameCharacter) target;
		character.boostAttack(value);
		System.out.println(character.getName() + "의 공격력이 " + value + " 증가했습니다!");
	}
	
	@Override
	public String getDescription() {
		return "공격력 +" + value;
	}
}