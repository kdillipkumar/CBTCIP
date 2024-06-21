package tech;

import java.util.*;

public class GuessTheNumber {
	
	public static void main(String [] args) {
		
		Random ran = new Random();
		int randomnum  = ran.nextInt(100)+1;
				
		guessNumber(randomnum);
	}
	
	static void guessNumber(int randomnum) {
		
		int attempts = 0;
		
		while(true) {
			
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			System.out.println("Guess the Number between 1-100");
			int ipnum = sc.nextInt();
			attempts ++;
			
			if(attempts==10) {
				System.out.println("Exceeding the number of attempts");
				System.out.println("Your Score : "+0);
				System.out.println("You got "+0+" points");
				System.exit(0);
			}
			
			if(randomnum == ipnum) {
				System.out.println("Wow! You guess the correct number");
				System.out.println("No. of attempts : "+attempts );
				
				if(attempts<=2) 
					System.out.println("You got "+9+" points");
				else if(attempts>=3 && attempts<=5) 
					System.out.println("You got "+7+" points");
				else if(attempts>=6 && attempts<=7) 
					System.out.println("You got "+5+" points");
				else
					System.out.println("You got "+2+" points");
				
				break;
			}
			else if(randomnum>ipnum) {
				System.out.println("Nope! The random number is larger");
			}
			else {
				System.out.println("Nope! The random number is smaller");
			}
		}
		
	}

}
