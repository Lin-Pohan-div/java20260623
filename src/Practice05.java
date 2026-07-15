public class Practice05 {
    public static int countVowels(String s) {
        int count = 0;

        String lower = s.toLowerCase();
        
        for (char c : lower.toCharArray() ) {
            if ("aeiou".indexOf(c) > 0) {
                count ++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(countVowels("null"));
        System.out.println(countVowels("Hello"));   
        System.out.println(countVowels("AEIOU"));   
        System.out.println(countVowels("xyz"));     
        System.out.println(countVowels("Java"));    
    }
}
