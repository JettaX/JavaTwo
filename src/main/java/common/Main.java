package common;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Filling array sequence");
        createAndFillArraySequence(10_000_000);

        System.out.println("Filling array parallel");
        createAndFillArrayParallel(10_000_000);

        //Experimental results:
        System.out.println("Filling array parallel with stream api");
        creatAndFillArrayParallelWithStreamApi(10_000_000);

        System.out.println("Filling array parallel with arrays parallel");
        createAndFillArrayParallelWithArraysParallel(10_000_000);

    }

    private static void createAndFillArraySequence(int size) {
        int[] arr = new int[size];
        Arrays.fill(arr, 1);

        long start = System.currentTimeMillis();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start));
    }

    private static void createAndFillArrayParallel(int size) {
        int[] arr = new int[size];
        int[] arr2 = new int[size / 2];
        int[] arr3 = new int[size / 2];
        Arrays.fill(arr, 1);

        long start = System.currentTimeMillis();

        Thread thread1 = new Thread(() -> {
            System.arraycopy(arr, 0, arr2, 0, arr.length / 2);
            for (int i = 0; i < arr2.length; i++) {
                arr2[i] = (int) (arr2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });

        Thread thread2 = new Thread(() -> {
            System.arraycopy(arr, arr.length / 2, arr3, 0, arr.length / 2);
            for (int i = 0; i < arr3.length; i++) {
                arr3[i] = (int) (arr3[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.arraycopy(arr2, 0, arr, 0, arr2.length);
        System.arraycopy(arr3, 0, arr, arr2.length, arr3.length);

        long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start));
    }

    private static void creatAndFillArrayParallelWithStreamApi(int size) {
        int[] arr = new int[size];
        Arrays.fill(arr, 1);

        long start = System.currentTimeMillis();

        arr = Arrays.stream(arr)
                .parallel()
                .map(x -> (int) (x * Math.sin(0.2f + x / 5) * Math.cos(0.2f + x / 5) * Math.cos(0.4f + x / 2)))
                .toArray();

        long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start));
    }

    private static void createAndFillArrayParallelWithArraysParallel(int size) {
        int[] arr = new int[size];
        Arrays.fill(arr, 1);

        long start = System.currentTimeMillis();

        Arrays.parallelSetAll(arr, i -> (int) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2)));

        long end = System.currentTimeMillis();

        System.out.println("Time: " + (end - start));
    }
}
