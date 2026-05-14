package main;

import java.util.HashMap;
import java.util.Map;

public class MonsterBook {

    // 몬스터 이름 - 처치 횟수
    private HashMap<String, Integer> book = new HashMap<>();

    // 몬스터 처치 시 기록
    public void record(String monsterName) {
        if (book.containsKey(monsterName)) {
            book.put(monsterName, book.get(monsterName) + 1);
        } else {
            book.put(monsterName, 1);
        }
    }

    // 도감 출력 - 메시지 반환
    public String show() {
        if (book.isEmpty()) {
            return "아직 처치한 몬스터가 없습니다.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("===== 몬스터 도감 =====\n");
        for (Map.Entry<String, Integer> entry : book.entrySet()) {
            sb.append(entry.getKey() + " - 처치 횟수: " + entry.getValue() + "\n");
        }
        sb.append("====================");
        return sb.toString();
    }

    public boolean isEmpty() { return book.isEmpty(); }
}