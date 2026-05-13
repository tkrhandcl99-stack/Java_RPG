package inventory;

import item.Item;
import exception.InventoryFullException;
import java.util.ArrayList;

public class Inventory {
	// 인벤토리 모두 공유, 불변값 사용위해 final
    private static final int MAX_SIZE = 5;
    private ArrayList<Item> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public boolean isFull() {
        return items.size() >= MAX_SIZE;
    }
    
    public String addItem(Item item) {
        if (items.size() >= MAX_SIZE) {
            throw new InventoryFullException();
        }
        items.add(item);
        return item.getName() + "을(를) 획득했습니다!";
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

    public String showInventory() {
        if (items.isEmpty()) {
            return "인벤토리가 비어있습니다.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("=== 인벤토리 ===\n");
        for (int i = 0; i < items.size(); i++) {
            sb.append((i + 1) + ". " + items.get(i)).append("\n");
        }
        sb.append("================");
        return sb.toString();
    }
}