package battle;

import character.GameCharacter;
import character.Warrior;
import character.Mage;
import character.Archer;
import monster.Monster;
import monster.Goblin;
import monster.DarkKnight;
import monster.Dragon;
import monster.TreeGuard;
import monster.WildJaewon;
import inventory.Inventory;
import exception.InvalidTargetException;
import exception.InventoryFullException;
import item.Weapon;
import item.Armor;
import item.MithrilGlove;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class BattleSystem {
	private Scanner scanner;
	
	public BattleSystem() {
		this.scanner = new Scanner(System.in);
	}
	
	public void start(GameCharacter player, ArrayList<Monster> monsters, Inventory inventory) {
		
		while (player.isAlive()) {
			System.out.println("\n" + player);
			player.showStatus();
			printMonsters(monsters);
			
			if (allMonstersDead(monsters)) {
				System.out.println("\n모든 몬스터를 처치했습니다!");
				player.removeStatus("독");
				System.out.println("모든 상태이상이 해제되었습니다!");
				
				boolean hasWildJaewon = false;
				for (Monster m : monsters) {
					if (m instanceof WildJaewon) {
						hasWildJaewon = true;
						break;
					}
				}
				
				if (!hasWildJaewon) {
					dropEquipment(player);
				}
				break;
			}
			
			System.out.println("\n행동을 선택하세요.");
			System.out.println("1. 공격");
			System.out.println("2. 아이템 사용");
			System.out.println("3. 스킬 사용 (MP 소모)");
			System.out.println("4. 상태 확인");
			
			int action = getInput(1, 4);
			
			try {
				if (action == 1) {
					attackPhase(player, monsters);
				} else if (action == 2) {
					itemPhase(player, inventory);
				} else if (action == 3) {
					skillPhase(player, monsters);
				} else {
					System.out.println("\n" + player);
					inventory.showInventory();
					player.showEquipped();
				}
			} catch (InvalidTargetException e) {
				System.out.println(e.getMessage());
			} catch (InventoryFullException e) {
				System.out.println(e.getMessage());
			}
			
			player.recoverMp();
			monsterTurn(player, monsters);
			applyPoison(player);
			
			if (!player.isAlive()) {
				System.out.println("\n" + player.getName() + "이(가) 쓰러졌습니다. 게임 오버!");
			}
		}
	}
	
	private void attackPhase(GameCharacter player, ArrayList<Monster> monsters) {
		System.out.println("\n어떤 몬스터를 공격할까요?");
		printMonsters(monsters);
		
		int target = getInput(1, monsters.size());
		Monster monster = monsters.get(target - 1);
		
		if (!monster.isAlive()) {
			throw new InvalidTargetException("이미 쓰러진 몬스터입니다.");
		}
		
		player.attack(monster);
		System.out.println(monster.getName() + " 남은 HP: " + monster.getHp());
		
		if (!monster.isAlive()) {
			System.out.println(monster.getName() + "을(를) 처치했습니다!");
			if (monster instanceof Goblin) {
				player.gainExp(((Goblin) monster).getExpDrop());
			} else if (monster instanceof Dragon) {
				player.gainExp(((Dragon) monster).getExpDrop());
			} else if (monster instanceof WildJaewon) {
				player.gainExp(((WildJaewon) monster).getExpDrop());
				MithrilGlove glove = new MithrilGlove();
				if (player.getEquipped("장갑") == null) {
					player.equip("장갑", glove);
					glove.use(player);
					System.out.println("장갑 [" + glove.getName() + "] 를 획득하고 자동 장착했습니다!");
				} else {
					System.out.println("이미 장갑을 장착하고 있습니다.");
				}
			} else if (monster instanceof TreeGuard) {
				player.gainExp(((TreeGuard) monster).getExpDrop());
			} else if (monster instanceof DarkKnight) {
				player.gainExp(((DarkKnight) monster).getExpDrop());
			}
		}
	}
	
	private void itemPhase(GameCharacter player, Inventory inventory) {
		if (inventory.isEmpty()) {
			System.out.println("\n사용할 아이템이 없습니다.");
			return;
		}
		
		System.out.println("\n어떤 아이템을 사용할까요?");
		inventory.showInventory();
		
		int target = getInput(1, inventory.getSize());
		inventory.getItem(target - 1).use(player);
		inventory.removeItem(target - 1);
	}
	
	private void skillPhase(GameCharacter player, ArrayList<Monster> monsters) {
		System.out.println("\n어떤 몬스터에게 스킬을 사용할까요?");
		printMonsters(monsters);
		
		int target = getInput(1, monsters.size());
		Monster monster = monsters.get(target - 1);
		
		if (!monster.isAlive()) {
			throw new InvalidTargetException("이미 쓰러진 몬스터입니다.");
		}
		
		if (player instanceof Warrior) {
			((Warrior) player).shieldBash(monster);
		} else if (player instanceof Mage) {
			((Mage) player).magicExplosion(monster);
		} else if (player instanceof Archer) {
			((Archer) player).rapidShot(monster);
		}
		
		System.out.println(monster.getName() + " 남은 HP: " + monster.getHp());
		if (!monster.isAlive()) {
			System.out.println(monster.getName() + "을(를) 처치했습니다!");
			if (monster instanceof Goblin) {
				player.gainExp(((Goblin) monster).getExpDrop());
			} else if (monster instanceof Dragon) {
				player.gainExp(((Dragon) monster).getExpDrop());
			} else if (monster instanceof WildJaewon) {
				player.gainExp(((WildJaewon) monster).getExpDrop());
				MithrilGlove glove = new MithrilGlove();
				if (player.getEquipped("장갑") == null) {
					player.equip("장갑", glove);
					glove.use(player);
					System.out.println("장갑 [" + glove.getName() + "] 를 획득하고 자동 장착했습니다!");
				} else {
					System.out.println("이미 장갑을 장착하고 있습니다.");
				}
			} else if (monster instanceof TreeGuard) {
				player.gainExp(((TreeGuard) monster).getExpDrop());
			} else if (monster instanceof DarkKnight) {
				player.gainExp(((DarkKnight) monster).getExpDrop());
			}
		}
	}
	
	private void monsterTurn(GameCharacter player, ArrayList<Monster> monsters) {
		for (Monster monster : monsters) {
			if (monster.isAlive() && player.isAlive()) {
				monster.attack(player);
				System.out.println(player.getName() + " 남은 HP: " + player.getHp());
			}
		}
	}
	
	private void applyPoison(GameCharacter player) {
		if (player.hasStatus("독")) {
			System.out.println(player.getName() + "이(가) 독 데미지 5를 받았습니다!");
			player.takeDamage(5);
			System.out.println(player.getName() + " 남은 HP: " + player.getHp());
		}
	}
	
	private void dropEquipment(GameCharacter player) {
		Random random = new Random();
		int drop = random.nextInt(100);
		
		if (drop < 40) {
			Weapon weapon = new Weapon();
			if (player.getEquipped("무기") == null) {
				player.equip("무기", weapon);
				weapon.use(player);
				System.out.println("무기 [" + weapon.getName() + "] 를 획득하고 자동 장착했습니다!");
			} else {
				System.out.println("이미 무기를 장착하고 있어 드랍 아이템을 버렸습니다.");
			}
		} else if (drop < 80) {
			Armor armor = new Armor();
			if (player.getEquipped("방어구") == null) {
				player.equip("방어구", armor);
				armor.use(player);
				System.out.println("방어구 [" + armor.getName() + "] 를 획득하고 자동 장착했습니다!");
			} else {
				System.out.println("이미 방어구를 장착하고 있어 드랍 아이템을 버렸습니다.");
			}
		} else if (drop < 83) {
			MithrilGlove glove = new MithrilGlove();
			if (player.getEquipped("장갑") == null) {
				player.equip("장갑", glove);
				glove.use(player);
				System.out.println("장갑 [" + glove.getName() + "] 를 획득하고 자동 장착했습니다!");
			} else {
				System.out.println("이미 장갑을 장착하고 있어 드랍 아이템을 버렸습니다.");
			}
		} else {
			System.out.println("장비를 획득하지 못했습니다.");
		}
	}
	
	private void printMonsters(ArrayList<Monster> monsters) {
		System.out.println("\n==== 몬스터 ====");
		for (int i = 0; i < monsters.size(); i++) {
			Monster m = monsters.get(i);
			String status = m.isAlive() ? m.toString() : m.getName() + " [사망]";
			System.out.println((i + 1) + ". " + status);
		}
		System.out.println("===============");
	}
	
	private boolean allMonstersDead(ArrayList<Monster> monsters) {
		for (Monster monster : monsters) {
			if (monster.isAlive()) return false;
		}
		return true;
	}
	
	private int getInput(int min, int max) {
		while (true) {
			try {
				System.out.print("입력: ");
				int input = scanner.nextInt();
				if (input < min || input > max) {
					throw new InvalidTargetException(min + "~" + max + " 사이의 숫자를 입력하세요.");
				}
				return input;
			} catch (InvalidTargetException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				System.out.println("숫자를 입력하세요.");
				scanner.nextLine();
			}
		}
	}
}