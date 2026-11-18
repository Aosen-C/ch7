// Part 2 Questions
// The person would have lost money because the stock decreased in price
// Both timings make sense. The Great Depression occured from 1929 to 1939 so it is expected that sp500_1925_1945 would have its lowest price in 1932. The 2008 financial crisis occured in 2008, making it reasonable for sbux_2007_2016 to have its lowest price in 2008.

// Part E Questions
// Dip Buyer did the best on the most files. No strategy was the best on every file.
// Dip buyer performed poorly on falling.txt because when dip buyer bought into a "dip" the prices never went up to make any profit.
// Moving average beat buy and hold on falling.txt. That is because buy and hold immediately buys a share, but the prices keep on falling so it receives a significant loss. Moving average detects that the stock's trend is always decreasing because the price is always below the 12 month average. Thus, it does not buy the stock trending downward.
// My strategy did better than Buy and Hold on 5 stocks. If I changed my rules to do better on one stock, it would decrease my profit on others.

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
	
	public static String dipBuyer(double[] history, boolean holding, double boughtAt) {
		if (!holding && history.length >= 3) {
			if (history[history.length - 2] < history[history.length - 3] && history[history.length-1] < history[history.length-2]) {
				return "BUY";
			}
		} else if (holding && history.length >= 3){
			if (holding && (history[history.length - 1]-boughtAt)/boughtAt > 0.1) {
				return "SELL";
			}
		} 
		return "HOLD";
	} 
	
	public static String movingAverage(double[] history, boolean holding, double boughtAt) {
		if (history.length < 12) {
			return "HOLD";
		}
		double sum = 0;
		int indexOf12MonthsBefore = history.length - 12;
		for (int i = indexOf12MonthsBefore; i < history.length; i++) {
			sum += history[i];
		}
		double average = sum/12;
		
		if (!holding && history[history.length - 1] > average) {
			return "BUY";
		} else if (holding && history[history.length - 1] < average) {
			return "SELL";
		}
		return "HOLD";
	}
	
	// My Strategy Explanation
	// For my strategy, I combined the dip buyer and moving average strategies. 
	// It buys stocks when the current price is above the 12-month moving average but has decreased compared to the previous month
	// It sells when the stock price increases by more than 10% compared to its purchase price.
	// If none of these conditions are met, it holds.
	
	// I believe this strategy will work because it identifies stocks with an overall upward trend and takes advantage of a temporary price decrease to buy the stock at a lower price.
	
	public static String myStrategy(double[] history, boolean holding, double boughtAt) {
		if (history.length < 12) {
			return "HOLD";
		}
		double sum = 0;
		int indexOf12MonthsBefore = history.length - 12;
		for (int i = indexOf12MonthsBefore; i < history.length; i++) {
			sum += history[i];
		}
		double average = sum/12;
		
		if (!holding && history[history.length - 1] > average) {
			if (history[history.length-1] < history[history.length-2]) {
				return "BUY";
			}
		} else if (holding && (history[history.length - 1]-boughtAt)/boughtAt > 0.1){
			return "SELL";
		}
		return "HOLD";
	}
	
	public static String decide(double[] history, boolean holding, double boughtAt) {
		return myStrategy(history, holding, boughtAt);
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
	}
	
	System.out.println();	
	System.out.printf("Your final cash: $%.2f", simulate(years, months, prices));
	}
}
