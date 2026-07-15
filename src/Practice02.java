import java.util.*;

public class Practice02 {
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n 必須為非負整數");
        }
        long result = 1L;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("請輸入一個非負整數 n：");
        int n = scanner.nextInt();
        scanner.close();

        if (n < 0) {
            System.out.println("輸入必須為非負整數。");
            return;
        }
        System.out.println(n + "!=" + factorial(n));
    }
}
