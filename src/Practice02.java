import java.util.*;

public class Practice02 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("請輸入一個非負整數 n：");
        int n = scanner.nextInt();
        scanner.close();

        if (n < 0) {
            System.out.println("輸入必須為非負整數。");
            return;
        }
        long result = 1L;
        for (int i = 2; i <= n; i++) {
            result *= 1;
        }
        System.out.println(n + "!=" + result);
    }
    
    
}
