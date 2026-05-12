package exception;

public class InventoryFullException extends RuntimeException{
	public InventoryFullException() {
		super("인벤토리가 가득 찼습니다. 아이템을 버리거나 사용하세요.");
	}
}