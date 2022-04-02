package exercise_1;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String[] array = new String[]{
                "patisserie", "grave", "goal", "platform", "rank", "soft", "acquaintance",
                "home", "evil", "scavenger", "crockery", "thumb", "main", "master", "grave",
                "platform", "thumb", "evil", "grave", "scavenger", "home", "soft"
        };

        printUniqueWords(array);
        System.out.println("_________________________________________________");
        printNumberOfRepetitions(array);
        System.out.println("_________________________________________________");

    }

    private static void printUniqueWords(String[] words) {
        Arrays.stream(words)
                .distinct()
                .forEach(System.out::println);
        /*System.out.println(new HashSet<>(Arrays.asList(words)));*/
    }

    private static void printNumberOfRepetitions(String[] words) {
        Arrays.stream(words)
                .map(word -> word + " " + Arrays.stream(words)
                        .filter(word::equals)
                        .count() + " times")
                .distinct()
                .forEach(System.out::println);
    }
}
