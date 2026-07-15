import java.util.ArrayList;
import java.util.List;

public class Practice09 {

    public List<Integer> parseToIntegers(String[] input) {
        List<Integer> result = new ArrayList<>();
        if (input == null) {
            return result;
        }
        for (String s : input) {
            try {
                result.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException e) {
                result.add(0);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Practice09 p = new Practice09();
        String[] data = {"1", "2", "x", "4"};
        System.out.println(p.parseToIntegers(data));
    }
}
