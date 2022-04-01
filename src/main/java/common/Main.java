package common;

import exception.MyArrayDataException;
import exception.MyArraySizeException;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println(transformToIntAndSum(new String[][]{
                {"1", "1", "1", "1"},
                {"1", "1", "1", "1"},
                {"1", "1", "1", "1"},
                {"1", "1", "1", "1"}
        }));
    }

    private static int transformToIntAndSum(String[][] mass4X4) {
        if (mass4X4.length != 4 || Arrays.stream(mass4X4).anyMatch(line -> line.length != 4))
            throw new MyArraySizeException();
        int sum = 0;

        for (int i = 0; i < mass4X4.length; i++) {
            for (int j = 0; j < mass4X4[i].length; j++) {
                try {
                    sum += Integer.parseInt(mass4X4[i][j]);
                } catch (NumberFormatException e) {
                    throw new MyArrayDataException(mass4X4[i][j], i + " " + j);
                }
            }
        }
        return sum;
    }
}
