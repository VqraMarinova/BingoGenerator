package gen.objects;

import java.util.Random;

public class LotteryTicket {

	private static final Random RANDOM = new Random();
	private BingoCard bingoCard;
	private int[][] playerNumbers;
	private double winningAmount;

	public LotteryTicket(double[] possibleWinSums) {
		bingoCard = new BingoCard();
		playerNumbers = bingoCard.generateBingoCardNumbers();
		winningAmount = chooseRandomWinning(possibleWinSums);
	}

	private double chooseRandomWinning(double[] possibleWinSums) {
		if (possibleWinSums == null || possibleWinSums.length == 0) {
			return 0;
		} else {
			int index = RANDOM.nextInt(possibleWinSums.length);
			return possibleWinSums[index];
		}
	}

	public double getWinningAmount() {
		return winningAmount;
	}

	public BingoCard getBingoCard() {
		return bingoCard;
	}

	public int[][] getPlayerNumbers() {
		return playerNumbers;
	}
}
