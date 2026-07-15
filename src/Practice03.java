public class Practice03 {
    public static String classify(char c) {
        if (Character.isDigit(c))
            return "數字";
        if (Character.isLetter(c))
            return "字母";
        if (Character.isWhitespace(c))
            return "空白";
        return "其他";
    }

    public static void main(String[] args) {
        System.out.println(classify('A')); // 字母
        System.out.println(classify('7')); // 數字
        System.out.println(classify(' ')); // 空白
        System.out.println(classify('@')); // 其他
    }
}
