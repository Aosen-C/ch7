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
		String[] names = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug"};
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
	
	public static void main(String[] args) throws FileNotFoundException {
	Scanner in = new Scanner(new File("tiny.txt"));
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
	
	double percentDrop = (prices[indexBiggestDrop] - prices[indexBiggestDrop - 1])/prices[indexBiggestDrop];
	
	System.out.printf("TINY lowest: %.2f in %s %d\n", prices[indexLow], monthName(months[indexLow]), years[indexLow]);
	System.out.printf("TINY highest: %.2f in %s %d\n", prices[indexHigh], monthName(months[indexHigh]), years[indexHigh]);
	if (indexOfBiggestDrop(prices) != -1) {
		System.out.printf("TINY biggest drop: %.2f in %s %d\n", percentDrop, monthName(months[indexBiggestDrop]), years[indexBiggestDrop]);
	}
	}
}
