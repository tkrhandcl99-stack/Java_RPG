package exception;

public class DeadCharacterException extends RuntimeException {
	public DeadCharacterException(String name) {
		super (name + "은(는) 이미 쓰러져 있습니다.");
	}	
}