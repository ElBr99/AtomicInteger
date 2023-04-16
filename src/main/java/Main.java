import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static AtomicInteger threeLengthWord = new AtomicInteger(0);
    static AtomicInteger fourLengthWord = new AtomicInteger(0);
    static AtomicInteger fiveLengthWord = new AtomicInteger(0);



    public static void main(String[] args) throws InterruptedException {
        List<Thread> list = new ArrayList<>();
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = TextGenerator.generateText("abc", 3 + random.nextInt(3));
        }


        list.add(new Thread(() -> {
            StringBuilder revers = new StringBuilder();
            for (String str : texts) {
                revers.delete(0, revers.length()).append(str).reverse();
                if (str.equals(revers.toString())) {
                    increment(str.length());
                }
            }
        }));

        list.add(new Thread(() -> {
            for (String s : texts) {
                if (s.chars().filter(x -> x == s.charAt(0)).count() == s.length()) {
                    increment(s.length());
                }
            }
        }));

        list.add(new Thread(() -> {
            StringBuilder sortedS = new StringBuilder();
            for (String s : texts) {
                sortedS.delete(0, sortedS.length());
                Arrays.stream(s.split("")).sorted().forEach(sortedS::append);
                if (s.equals(sortedS.toString())) {
                    increment(s.length());
                }
            }
        }));

        list.forEach(Thread::start);
        list.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("Красивых слов с длиной 3: " + threeLengthWord.get() + " шт\n" +
                "Красивых слов с длиной 4: " + fourLengthWord.get() + " шт\n" +
                "Красивых слов с длиной 5: " + fiveLengthWord.get() + " шт");
        System.out.println(threeLengthWord.get() + fourLengthWord.get() + fiveLengthWord.get());
    }

    static void increment(int length) {
        switch (length) {
            case (3):
                threeLengthWord.incrementAndGet();
            case (4):
                fourLengthWord.incrementAndGet();
            case (5):
                fiveLengthWord.incrementAndGet();
        }
    }
}
