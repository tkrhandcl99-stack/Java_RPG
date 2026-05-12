package main;

import character.GameCharacter;
import character.Warrior;
import character.Mage;
import character.Archer;
import monster.Monster;
import monster.Goblin;
import monster.Dragon;
import inventory.Inventory;
import item.HealPotion;
import item.AttackPotion;
import battle.BattleSystem;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("==============================");
		System.out.println("        콘솔 RPG 게임 시작!        ");
		System.out.println("==============================");
		
		// 캐릭터 선택
		int choice = 0;
		while (choice < 1 || choice > 3) {
			System.out.println("\n캐릭터를 선택하세요.");
			System.out.println("1. 전사 (HP: 150 / 공격력: 25)");
			System.out.println("2. 마법사 (HP: 100 / 공격력: 40)");
			System.out.println("3. 궁수 (HP: 120 / 공격력: 30)");
			System.out.print("입력: ");
			choice = scanner.nextInt();
			scanner.nextLine();
			if (choice < 1 || choice > 3) {
				System.out.println("1~3 사이의 숫자를 입력하세요.");
			}
		}
		
		System.out.print("캐릭터 이름을 입력하세요: ");
		String name = scanner.nextLine();
		
		GameCharacter player;
		if (choice == 1) {
			player = new Warrior(name);
		} else if (choice == 2) {
			player = new Mage(name);
		} else {
			player = new Archer(name);
		}
		System.out.println("\n" + player.getName() + " 선택 완료!");
		
		
		int difficulty = 0;
		while (difficulty < 1 || difficulty > 2) {
			System.out.println("\n난이도를 선택하세요.");
			System.out.println("1. Easy");
			System.out.println("2. Hard");
			System.out.print("입력: ");
			difficulty = scanner.nextInt();
			scanner.nextLine();
			if (difficulty < 1 || difficulty > 2) {
				System.out.println("1~2 사이의 숫자를 입력하세요.");
			}
		}
		
		
		boolean isEasy = difficulty == 1;
		System.out.println(isEasy ? "\nEasy 모드로 시작합니다!" : "\nHard 모드로 시작합니다!");
		
		// 인벤토리 세팅
		Inventory inventory = new Inventory();
		inventory.addItem(new HealPotion());
		inventory.addItem(new HealPotion());
		inventory.addItem(new AttackPotion());
		
		// 스테이지 1 - 고블린
		boolean stage1Cleared = false;
		
		while (!stage1Cleared ) {
			System.out.println("\n====== 스테이지 1 ======");
			ArrayList<Monster> stage1 = new ArrayList<>();
			stage1.add(isEasy ? new Goblin(10) : new Goblin());
			stage1.add(isEasy ? new Goblin(10) : new Goblin());
			
			BattleSystem battle1 = new BattleSystem();
			battle1.start(player, stage1, inventory);
			
			if (player.isAlive()) {
				stage1Cleared = true;
				System.out.println("\n스테이지 1 클리어! 체력 포션을 획득했습니다.");
				inventory.addItem(new HealPotion());
			} else {
				System.out.println("\n다시 도전하시겠습니까? (Y/N)");
				System.out.print("입력: ");
				String retry = scanner.next().toUpperCase();
				
				if (retry.equals("Y")) {
					player.heal(player.getMaxHp() / 2);
					System.out.println("\n체력을 " + player.getMaxHp() / 2 + " 회복하고 재도전합니다!");
				} else {
					System.out.println("\n게임을 종료합니다.");
					scanner.close();
					return;
				}
			}
		}

		// 스테이지 2 - 드래곤
		boolean stage2Cleared = false;
		
		while (!stage2Cleared) {
			System.out.println("\n====== 스테이지 2 - 보스 ======");
			ArrayList<Monster> stage2 = new ArrayList<>();
			stage2.add(isEasy ? new Dragon(20) : new Dragon());
			
			BattleSystem battle2 = new BattleSystem();
			battle2.start(player, stage2, inventory);
			
			if (player.isAlive()) {
				stage2Cleared = true;
			} else {
				System.out.println("\n다시 도전하시겠습니까? (Y/N)");
				System.out.print("입력: ");
				String retry = scanner.next().toUpperCase();
				
				if (retry.equals("Y")) {
					player.heal(player.getMaxHp() / 2);
					System.out.println("\n체력을 " + player.getMaxHp() / 2 + " 회복하고 재도전합니다!");
				} else {
					System.out.println("\n게임을 종료합니다.");
					scanner.close();
					return;
				}
			}
		}
		
		System.out.println("\n==============================");
		System.out.println("         게임 클리어!         ");
		System.out.println("\n==============================");
		
		scanner.close();
	}
}