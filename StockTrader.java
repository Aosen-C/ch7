import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class StockTrader {
	public static void loadData(Scanner in, int[] years, int[] months, double[] prices) {
		for (int i = 0; i < years.length; i++) {
			years[i] = in.nextInt();
			months[i] = in.nextInt();
			prices[i] = in.nextDouble();
		}
	}
	
	public static String monthName(int month) {
		String[] names = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
		String months = names[month - 1];
		return months;
	}
	
	public static void printData(int[] years, int[] months, double[] prices) {
		for(int i = 0; i < years.length; i++) {
			System.out.printf("%s %d: %.2f\n", monthName(months[i]), years[i], prices[i]);
		}
	}
	
	public static int indexOfLowest(double[] prices) {
		double lowest = prices[0];
		int index = 0;
		for (int i = 0; i < prices.length; i++) {
			if (prices[i] < lowest) {
				index = i;
				lowest = prices[i];
			}
		}
		return index;
	}
	
	public static int indexOfHighest(double[] prices) {
		double highest = 0;
		int index = 0;
		for (int i = 0; i < prices.length; i++) {
			if (prices[i] > highest) {
				index = i;
				highest = prices[i];
			}
		}
		return index;
	}
	
	public static int indexOfBiggestDrop(double[] prices) {
		double percentDifference = 0;
		int index = -1;
		for (int i = 1; i < prices.length; i++) {
			if ((prices[i] - prices[i-1])/prices[i-1] < percentDifference) {
				percentDifference = (prices[i] - prices[i-1])/prices[i-1];
				index = i;
			}
		}
		return index;
	}
	
	public static final double START_CASH = 10000.0;
	public static final double FEE = 10.0;
	
	public static String buyAndHold(double[] history, boolean holding, double boughtAt) {
		return "BUY";
	}
	
	public static String decide(double[] history, boolean holding, double boughtAt) {
		return buyAndHold(history, holding, boughtAt);
	}
	
	public static double simulate(int[] years, int[] months, double[] prices) {
		double cash = START_CASH;
		int shares = 0;
		double boughtAt = 0.0;
		
		for (int i = 0; i < prices.length; i++) {
			double[] history = Arrays.copyOf(prices, i + 1);
			String action = decide(history, shares > 0, boughtAt);
			
			if (action.equals("BUY") && shares == 0) {
				int canBuy = (int) ((cash - FEE) / prices[i]);
				if (canBuy > 0) {
					shares = canBuy;
					cash = cash - shares * prices[i] - FEE;
					boughtAt = prices[i];
					System.out.printf("%s %d: BUY %d shares at $%.2f%n", monthName(months[i]), years[i], shares, prices[i]);
				}
			} else if (action.equals("SELL") && shares > 0) {
				cash = cash + shares * prices[i] - FEE;
				System.out.printf("%s %d: SELL %d shares at $%.2f%n", monthName(months[i]), years[i], shares, prices[i]);
				shares = 0;
				boughtAt = 0.0;
			}
		}
		return cash + shares * prices[prices.length - 1];
	}
	
	public static void main(String[] args) throws FileNotFoundException {
	Scanner systemScanner = new Scanner(System.in);
	System.out.print("Enter the file name: ");
	String filename = systemScanner.nextLine();
	
	Scanner in = new Scanner(new File(filename));
	String symbol = in.next();
	int n = in.nextInt();
	
	int[] years = new int[n];
	int[] months = new int[n];
	double[] prices = new double[n];
	
	loadData(in, years, months, prices);
	
	printData(years, months, prices);
	
	int indexLow = indexOfLowest(prices);
	int indexHigh = indexOfHighest(prices);
	int indexBiggestDrop = indexOfBiggestDrop(prices);
	
	
	System.out.printf("%s lowest: %.2f in %s %d\n", symbol, prices[indexLow], monthName(months[indexLow]), years[indexLow]);
	System.out.printf("%s highest: %.2f in %s %d\n", symbol, prices[indexHigh], monthName(months[indexHigh]), years[indexHigh]);
	if (indexOfBiggestDrop(prices) != -1) {
		double percentDrop = Math.abs((prices[indexBiggestDrop] - prices[indexBiggestDrop - 1])/prices[indexBiggestDrop-1]) * 100;
		System.out.printf("%s biggest drop: %.2f%% in %s %d\n", symbol, percentDrop, monthName(months[indexBiggestDrop]), years[indexBiggestDrop]);
	
	System.out.println();	
	System.out.printf("Your final cash: $%.2f", simulate(years, months, prices));
	}
	}
}
