/**
 * CS 331
 * Professor: Tannaz Rezaei
 *
 * Project #2 - Task # 2
 * 
 * Description: program the dynamic approach to the 0/1 knapsack problem.
 * 1. ask user for knapsack capacity
 * 2. ask user for n objects weights and profits or read an input.txt that contains n objects
 * 3. list objects that maximmize profit and show maximimum profit as output considering capacity.
 * 
 * @author - Eric Schenck
 * last modified: January 20, 2018
 *   
 */

import java.util.Scanner;
import java.io.*;


public class DynamicKnapsack {

	int capacity;											// used to store given capacity	
	int userChoice = 0;											// used to store user choice for entry or file input
	int numItems;														// number of items to be looked at. user entered or at top of input.txt
	double weight;
	double value;												// used to temporarily store before creating Item
	boolean runAgain = true;											// boolean to store user choice to run program again
	String itemFile = "C://Users//Eric//Desktop//input.txt";
	String itemsBagged = "";
	
	Item[] onTheTable;											// creating an array of items to store whats "on the table"
	double[][] values;											// used to store values and keep track of total profit 
	
	
	private void dynamicKnapsack(){
		
		values = new double[numItems + 1][capacity + 1];		// creating a 2D matrix to store all values of item combinations for dynamic use
		
		for(int i = 0; i <= numItems ; ++i){
			for(int j = 0; j <= capacity; ++j){
				
				if(j == 0 || i == 0){
					values[i][j] = 0;							// first row or column is always zero, and needed for dynamic approach
				
				}else if(onTheTable[i-1].weight <= j){			// if the weight is small enought to fit into knapsack
					
					
					// if (the value of current item + value considering 1 less item and less space in knapsack) is...
					//... greater than the value of same size knapsacks previous items solution 
					if(onTheTable[i-1].value + values[i-1][j - (int)onTheTable[i-1].weight] > values[i-1][j]){
						
						// set value equal to larger of the two in current if statement
						// acts like a Max( x, y) returning x if x>y and y if y>x
						
						values[i][j] =  onTheTable[i-1].value + values[i-1][j - (int)onTheTable[i-1].weight];
						
					}else{
						
						values[i][j] = values[i-1][j];						// bring values down if it is max out of two 
					}
					
					
				}else{
					
					values[i][j] = values[i-1][j];						// bring values down since current item weight is too big
					
				}
				
			}
		}
		
		
		// following code is used to reverse look through 2D array and save all taken items to string. 
		
		int i = numItems;												// used to get items in knapsack
		int j = capacity;												
		
		while(j>0 && i>0){												// while there is space in the bag and items to check
			
			if( j - onTheTable[i-1].weight >= 0 						// not looking at items that outweigh capacity
					&& onTheTable[i-1].value + values[i-1][j- (int)onTheTable[i-1].weight] == values[i][j]){
						// if current value was created by item value + value of sack with one less item 
						//and less that items capacity.
			
				itemsBagged += onTheTable[i-1].toString();				// saving item to a printable string
			
				j -= onTheTable[i-1].weight;							// adjusting remaining bag capacity
				
				--i;													// decrimenting i , looking at previous items one by one
			
			}else{
				--i;													// must continue going through items 
			}
		
		}
		
		System.out.print(itemsBagged);
		System.out.println("Total Value in KnapSack: " + values[numItems][capacity]);	// printout to console 
		
		
		
		
		
		
	}
	
	private String[] handleInitialSpaces(String[] splitUserSel){

		try{
			splitUserSel[0].charAt(0);			// handling issue of initial entered space
		}catch(Exception e){
		
			for(int i = 0; i < splitUserSel.length - 1 ; ++i){
				splitUserSel[i] = splitUserSel[i+1];
				splitUserSel[i+1] = null;
			}
		}
		return splitUserSel;
	}
	
	
	
	private DynamicKnapsack() throws FileNotFoundException{
		
		Scanner keyboard = new Scanner(System.in);
		
		
		
						
		System.out.println("----------------------------------");	// good portion is user input
														// didnt want to deal with errors so i just exit if wrong input
			
	
		System.out.println("Enter the Capacity of the Knapsack");	// user prompt
		try{
			capacity = keyboard.nextInt();
				
		}catch(Exception e){
				//keyboard.close();
			System.out.println("Only enter an integer value next time...");
			System.exit(1); 										// exit program if issue occurs
		}															
			
		System.out.println("Manual Entry of Items - Enter 1\n"
					+ "Load input.txt File - Enter 2");
		try{
			userChoice = keyboard.nextInt();
				//System.out.println(userChoice);
		}catch(Exception e){
				//keyboard.close();
			System.out.println("Only enter an integer value next time...");
			System.exit(1); 										// exit program if issue occurs
		}	
								
		if(userChoice == 1){// manual entry of items
				
			System.out.println("Enter n number of items");
				
			try{
				numItems = keyboard.nextInt();
					
			}catch(Exception e){
					
				System.out.println("Only enter an integer value next time...");
				System.exit(1); 										// exit program if issue occurs
			}
				
			onTheTable = new Item[numItems];									// array will be size n 
				
			System.out.println("Enter a weight and value separated by space. No Mistakes please");
				
				
			for(int i = 0; i < numItems ; ++i){
					
				System.out.println("-----------");
				System.out.println("Item no. " + (i+1));				// Item no. to go with user entry
					
				Scanner stringKey = new Scanner(System.in);				// used for user input
					
				String tempStr = stringKey.nextLine();					// getting user input as a string
					
				String[] tempStrArray = tempStr.split("\\s+");			// making sense of blank space between input
					
				tempStrArray = handleInitialSpaces(tempStrArray);		// handling initial space issue
					
				try{
					weight = Double.parseDouble(tempStrArray[0]);			// parsing input into weight and value
					value = Double.parseDouble(tempStrArray[1]);
				}catch(Exception e){
					System.out.println("Next time enter two numerical values");
					System.exit(1);											// exit program
				}
				
				onTheTable[i] = new Item((i+1) , weight, value);		// adding given item to the table
			
				keyboard.close();
				stringKey.close();
			}
				
		}else if(userChoice == 2){// input.txt 
				
			File file = new File(itemFile);									// Open file
			Scanner inputFile = new Scanner(file);
				
			String tempN = inputFile.nextLine();
			numItems = Integer.parseInt(tempN);
				
			onTheTable = new Item[numItems];									// initializing array
				
			int i = 0;													// used to keep track of current index
				
			while(inputFile.hasNextLine()){
																			
				String tempStr = inputFile.nextLine();					// getting user input as a string
				String[] tempStrArray = tempStr.split("\\s+");			// making sense of blank space between input
					
				weight = Double.parseDouble(tempStrArray[0]);			// getting weight
				value = Double.parseDouble(tempStrArray[1]);
					
				onTheTable[i] = new Item(++i, weight, value);			// storing item info and increase value of i by 1
			}
				
				/*  used for testing
				for(int j = 0; j< n ; ++j){
					System.out.println(onTheTable[j].toString());
				}
				*/
				
				
			inputFile.close();
		}else{
			System.out.println("Only enter 1 or 2 next time...");
			System.exit(1);
		}
			
			
		dynamicKnapsack();
				
		
	}
	
	
	
	
	
	
	public static void main(String[] args) throws FileNotFoundException{

		new DynamicKnapsack();
	
	}
	
	
	
	private class Item{											// item constructor
		
		protected int itemNumber;							// too keep track of what item is what for later printout
		//protected double ratio;									// value/weight = ratio
		protected double weight;								// values being saved
		protected double value;
		
		public Item(int itemNumber, double weight, double value){				// constructor
			this.weight = weight;
			this.value = value;
			this.itemNumber = itemNumber;
		}
		
		@Override
		public String toString(){
			
			return ("Item Number: " + itemNumber + "; weight = " + weight + "; value = " + value + "\n");
		}
		
	}
}
