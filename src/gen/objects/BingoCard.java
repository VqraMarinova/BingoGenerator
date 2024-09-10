package gen.objects;
import java.util.Random;

public class BingoCard {
    private static final int SIZE = 5;
    private int[][] numbers;

    public BingoCard() {
        numbers = generateBingoCardNumbers();
    }

    public int[][] generateBingoCardNumbers() {
        int[][] cardNumbers = new int[SIZE][SIZE];
        Random random = new Random();

        for (int col = 0; col < SIZE; col++) {
            int start = col * 15 + 1;
            int end = start + 14;

            for (int row = 0; row < SIZE; row++) {
                int num;
                do {
                    num = random.nextInt(end - start + 1) + start;
                } while (contains(cardNumbers, num));
                cardNumbers[row][col] = num;
            }
        }

        cardNumbers[2][2] = 0; // Free space

        return cardNumbers;
    }

    private boolean contains(int[][] cardNumbers, int num) {
        for (int[] row : cardNumbers) {
            for (int n : row) {
                if (n == num) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[][] getNumbers() {
        return numbers;
    }
}
