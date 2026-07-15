import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class Practice02Test {

    @Test
    void 零的階乘為一() {
        assertEquals(1L, Practice02.factorial(0));
    }

    @Test
    void 一的階乘為一() {
        assertEquals(1L, Practice02.factorial(1));
    }

    @Test
    void 一般值() {
        assertEquals(120L, Practice02.factorial(5));
        assertEquals(3628800L, Practice02.factorial(10));
    }

    @Test
    void 最大不溢位值() {
        // 20! = 2432902008176640000 為 long 可容納的最大階乘
        assertEquals(2432902008176640000L, Practice02.factorial(20));
    }

    @Test
    void 負數丟出例外() {
        assertThrows(IllegalArgumentException.class, () -> Practice02.factorial(-1));
    }
}
