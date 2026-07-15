import java.util.Scanner;
public class Practice06 {
    public static String toBinaryString(int n) {

        if (n == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            int remainder = n % 2;          
            sb.append((char) ('0' + remainder)); 
            n /= 2;                         
        }


        return sb.reverse().toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();               // 讀入非負整數
        System.out.println(toBinaryString(n));
        sc.close();
    }
}
