import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Practice01Test {

    @Test
    void 一般順序() {
        assertEquals(2, Practice01.median(3, 1, 2));
    }

    @Test
    void 已排序() {
        assertEquals(2, Practice01.median(1, 2, 3));
    }

    @Test
    void 有重複值() {
        assertEquals(5, Practice01.median(5, 5, 1));
        assertEquals(5, Practice01.median(5, 5, 9));
    }

    @Test
    void 三數相同() {
        assertEquals(7, Practice01.median(7, 7, 7));
    }

    @Test
    void 含負數() {
        assertEquals(-1, Practice01.median(-5, -1, 3));
    }

    @Test
    void 極端值不溢位() {
        assertEquals(0, Practice01.median(Integer.MAX_VALUE, Integer.MIN_VALUE, 0));
    }
}
