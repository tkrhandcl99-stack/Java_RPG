package item;

public interface Usable {
    String use(Object target);  // void → String ui 구조화 위해
    String getDescription();
}