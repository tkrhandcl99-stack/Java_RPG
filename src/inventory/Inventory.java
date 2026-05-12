package inventory;

import item.Item;
import exception.InventoryFullException;
import java.util.ArrayList;

public class Inventory {
	// final 사용해서 인벤토리 최대칸 5
	private static final int MAX_SIZE = 5;
	private ArrayList<Item> items;
	
	public Inventory() {
		this.items = new ArrayList<>();
	}
	
	public void addItem(Item item) {
		if (items.size() >= MAX_SIZE) {
			throw new InventoryFullException();
		}
		items.add(item);
		System.out.println(item.getName() + "을(를) 획득했습니다!");
	}
	
	public void removeItem(int index) {
		items.remove(index);
	}
	
	public Item getItem(int index) {
		return items.get(index);
	}
	
	public int getSize() {
		return items.size();
	}
	
	public boolean isEmpty() {
		return items.isEmpty();
	}
	
	public void showInventory() {
		if (items.isEmpty()) {
			System.out.println("인벤토리가 비어있습니다.");
			return;
		}
		System.out.println("======= 인벤토리 =======");
		for (int i = 0; i < items.size(); i++) {
			System.out.println((i + 1) + ". " + items.get(i));
		}
		System.out.println("========================");
	}
}