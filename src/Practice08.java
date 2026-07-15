import java.util.ArrayDeque;
import java.util.Deque;

public class Practice08 {
    /** 判斷只含 ()、[]、{} 的字串是否所有括號都正確配對且正確巢狀 */
    public static boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char c : s.toCharArray()) {
            // 1️⃣ 左括號直接壓入堆疊
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
                continue;
            }

            // 2️⃣ 右括號必須和堆疊頂部的左括號相配
            if (stack.isEmpty()) {
                // 沒有可對應的左括號 → 失配
                return false;
            }
            char top = stack.pop();
            if ((c == ')' && top != '(')
                    || (c == ']' && top != '[')
                    || (c == '}' && top != '{')) {
                return false;
            }
        }

        // 3️⃣ 最後堆疊必須為空，表示所有左括號都有對應的右括號
        return stack.isEmpty();
    }

    // 簡單的測試程式，方便直接執行觀察結果
    public static void main(String[] args) {
        String[] tests = { "{[]}", "([)]", "()", "()[]{}", "(]", "([{}])", "" };

        for (String t : tests) {
            System.out.printf("%s → %b%n", t, isValid(t));
        }
    }
}
