import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Practice03Test {

    @Test
    void 數字() {
        assertEquals("數字", Practice03.classify('7'));
        assertEquals("數字", Practice03.classify('0'));
    }

    @Test
    void 字母() {
        assertEquals("字母", Practice03.classify('A'));
        assertEquals("字母", Practice03.classify('z'));
    }

    @Test
    void 中文字也算字母() {
        // Character.isLetter 對中日韓文字亦回傳 true
        assertEquals("字母", Practice03.classify('中'));
    }

    @Test
    void 空白() {
        assertEquals("空白", Practice03.classify(' '));
        assertEquals("空白", Practice03.classify('\t'));
        assertEquals("空白", Practice03.classify('\n'));
    }

    @Test
    void 其他() {
        assertEquals("其他", Practice03.classify('@'));
        assertEquals("其他", Practice03.classify('#'));
    }
}
