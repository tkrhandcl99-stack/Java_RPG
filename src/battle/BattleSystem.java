package battle;

import character.GameCharacter;
import monster.Monster;
import inventory.Inventory;
import exception.InvalidTargetException;
import exception.InventoryFullException;
import exception.DeadCharacterException;
import java.util.ArrayList;
import java.util.Scanner;

public class BattleSystem {
	private Scanner scanner;
	
	public BattleSystem() {
		this.scanner = new Scanner(System.in);
	}
	
	public void start(GameCharacter player, ArrayList<Monster> monsters, Inventory inventory) {
		System.out.println("\n========= 전투 시작! =========");
		System.out.println(player);
		
		while (player.isAlive()) {
			printMonsters(monsters);
			
			if (allMonstersDead(monsters)) {
				System.out.println("\n모든 몬스터를 처치했습니다!");
				
				player.removeStatus("독");
				System.out.println("모든 상태이상이 해제되었습니다!");
				break;
			}
			
			System.out.println("\n행동을 선택하세요.");
			System.out.println("1. 공격");
			System.out.println("2. 아이템 사용");
			System.out.println("3. 상태 확인");
			
			int action = getInput(1, 3);
			
			try {
				if (action == 1) {
					attackPhase(player, monsters);
				} else if (action == 2) {
					itemPhase(player, inventory);
				} else {
					System.out.println("\n" + player);
					inventory.showInventory();
				}
			} catch (DeadCharacterException e) {
				System.out.println(e.getMessage());
			} catch (InvalidTargetException e) {
				System.out.println(e.getMessage());
			} catch (InventoryFullException e) {
				System.out.println(e.getMessage());
			}
			
			// 살아있는 몬스터가 반격
			monsterTurn(player, monsters);
			
			// 독 데미지 적용
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
	
	private void monsterTurn(GameCharacter player, ArrayList<Monster> monsters) {
		for (Monster monster : monsters) {
			if (monster.isAlive() && player.isAlive()) {
				monster.attack(player);
				System.out.println(player.getName() + " 남은 HP: " + player.getHp());
				
			}
		}
	}
	
	// 독 데미지 처리
	private void applyPoison(GameCharacter player) {
		if (player.hasStatus("독")) {
			System.out.println(player.getName() + "이(가) 독 데미지 5를 받았습니다!");
			player.takeDamage(5);
			System.out.println(player.getName() + " 남은 HP: " + player.getHp());
		}
	}
	
	private void printMonsters(ArrayList<Monster> monsters) {
		System.out.println("\n======= 몬스터 =======");
		for (int i = 0; i < monsters.size(); i++) {
			Monster m = monsters.get(i);
			String status = m.isAlive() ? m.toString() : m.getName() + " [사망]";
			System.out.println((i + 1) + ". " + status);
		}
		System.out.println("=====================");
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