public class Practice07 {
    /** 介面定義 */
    interface Animal {
        // 抽象方法：必須由實作類別提供叫聲
        String sound();

        // 預設方法：直接使用子類別的 sound()
        default String describe() {
            return "這隻動物說：" + sound();
        }
    }

    /** Dog 實作 Animal */
    static class Dog implements Animal {
        @Override
        public String sound() {
            return "汪汪";
        }
    }

    /** Cat 實作 Animal */
    static class Cat implements Animal {
        @Override
        public String sound() {
            return "喵喵";
        }
    }

    public static void main(String[] args) {
        // 建立 Animal 陣列，同時放入 Dog 與 Cat
        Animal[] animals = new Animal[] { new Dog(), new Cat() };

        // 走訪陣列並呼叫 describe()
        for (Animal a : animals) {
            System.out.println(a.describe());
        }
    }
}
