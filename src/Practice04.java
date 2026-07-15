import org.junit.runners.Parameterized.Parameter;

public class Practice04 {

    public static int diagonalSum(int[][] m) {
        int n = m.length;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += m[i][i];
            if (i != n - 1 - i) {
                sum += m[i][n - 1 - i];
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        System.out.println(diagonalSum(matrix));
    }

}
