import java.util.*;

public class Practice01 {
    public static int median(int a, int b,int c){
        long sum = (long) a + b + c ;
        int max = Math.max(a, Math.max(b, c));
        int min = Math.min(a, Math.min(b, c));
        return (int)(sum - max - min);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int b = sc.nextInt();
        int c = sc.nextInt();
        System.out.println(median(a, b, c));
        sc.close();
    }

}


