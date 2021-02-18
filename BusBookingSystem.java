//Program to bus reservation booking system
//Author:Murugan.V	
//Created on:13/2/2021
//last updated on:16/2/2021
//Yet to reviewed by Jaya and Anto
package Realtimeconcepts;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Date;
import java.text.SimpleDateFormat;

public class BusBookingSystem {                //Main class
	public static void main(String[] args) {
			BusTicketOperation user=new BusTicketOperation();
			user.getMobileNumber();
	}
}
	 class BusTicketOperation{                           //Complete bus ticket operation class 
			int totalSeats=40,k=0,limit=6,m=0;
			Scanner scanner=new Scanner(System.in);
		    private Encryption encryption=new Encryption();    
			public void getMobileNumber() {								 //user input for creating a account 
				System.out.println("================================================================");
				System.out.println("Enter your Mobile number to Login/Signup without country code: ");
				System.out.println("================================================================");
				String CellNumber=scanner.nextLine();    //Mobile number user input
				try {
				if(CellNumber.length()!=10) {			 //conditional statement for the proper mobile number	
						throw new ArithmeticException();  
				}
				long check=Long.parseLong(CellNumber);  //Parse the string to long to check for given string is a number or not
				store(CellNumber);
				}
				catch(NumberFormatException e)
				{
					System.err.println("Given input is invalid! Enter a valid number!! \n");
					getMobileNumber();
				}
				catch(ArithmeticException e1) {
					 System.err.println("Please Enter your 10 digit mobile number!! \n");
					 getMobileNumber();
				}
			}
			private void store(String number) { 					//Storing the data into txt file for the next time login
				try {
						File file=new File("Details.txt");	//File class to import the file
						FileWriter filewriter = new FileWriter(file,true); //(file,true) is used to move to the end of file to avoid overwriting
						BufferedWriter bufferedwriter = new BufferedWriter(filewriter);//Bufferedwriter is used to write the data on text file in properly formatted manner
						Scanner read=new Scanner(file);
						int flag=0;
						System.out.println("Loading......");
					    Thread.sleep(2000);
				while(read.hasNext()) {  					//used to looping over the document until the end
						String answer=read.nextLine(); 			
						String splitwords[]=answer.split(" ");
					    String numberFromFile=splitwords[0];
					    
						if(number.equals(numberFromFile)){				//Check for the current number given is already found in database
							flag=1;
							System.out.println("You are already a registered user,Enter your password: ");	
							String password=scanner.next();
							password=encryption.encrypt(password);	//Given password is encrypted and then matched with encrypted password in database
							if(password.equals(splitwords[1])) {
								getUserDetails();			//Getting the data for the old account
								}
							else {
								System.out.println("incorrect password,Try again!");
								store(number);
								} 
						}
				 }	
				 if(flag==0) { 	
					String password=password();
					bufferedwriter.write(number);				//writing the phone number
					bufferedwriter.write(" ");
					bufferedwriter.write(password);					//writing the encrypted password
					bufferedwriter.newLine();					//move the pointer to next line
					bufferedwriter.close();						//closing the document
					System.out.println("You've Successfully created your account");
					getUserDetails();							//Getting the remaining data for the newly created account
				 }
				 }
				 catch (IOException e) {
					System.out.print("File not found");
				 }
				 catch (InterruptedException e) {
					 System.out.println(e);
				 }
			}
			public String password() {				//Method to get the password for the newly registered number
				System.out.println("-----------------------------------------------------------------------");
				System.out.println("* Password must've one Uppercase letter and one Lowercase letter \n"
						+ "* Password must've one number atleast \n"+"* Password must've minimum 5 characters \n"+
						"* Password must not have any spaces");
				System.out.println("-----------------------------------------------------------------------");
				System.out.println("Enter your password: ");
				String match = "(?=.*[0-9])"		//using the (regular expression) to get the proper password
	                    + "(?=.*[a-z])" 			//?= is used to lookahead the string for each conditions given
	                    +"(?=.*[A-Z])"				//"." gets the one instance of character and match it
	                    +".{5,20}";
				String firstPassword=scanner.next();
			    Pattern p = Pattern.compile(match); 
			    Matcher m = p.matcher(firstPassword);
			    if(m.matches()==false) {
			    	System.out.println("Password must've all mandatory conditions given above: ");
			    	password();
			    }	 
				System.out.println("Confirm your password: ");
				String secondPassword=scanner.next();		//Making the user to enter password two times for better authentication
				if(!(firstPassword.equals(secondPassword))) {
					System.out.println("Please enter the same password on both fields:");
					password();
				}
				firstPassword=encryption.encrypt(firstPassword);
				return firstPassword;
			}
			public void getUserDetails() {			//Get the complete user details and requirements
				System.out.println("\nBuses available: \n"
						   +"Mayiladuthurai(MYD) to Coimbatore(CBE)");
				System.out.println("Total available seats:"+totalSeats);
				System.out.println("Enter the number of passengers(Maximum 6 per account is allowed)");
				int passengerCount=scanner.nextInt();		//getting the number of passengers
				if(passengerCount>limit) {
					System.out.println("Maximum passengers limit reached try again!");
					getUserDetails();
				}
				limit=limit-passengerCount;
				userDetails[] user=new userDetails[passengerCount]; //complete data is stored in object array
				for(int i=0;i<passengerCount;i++) {
					user[i]=new userDetails();
					System.out.println("Enter your Name: ");
					scanner.nextLine();
					user[i].setName(scanner.nextLine());
					System.out.println("Enter your Age: ");
					try {
						user[i].setAge(scanner.nextInt());
					}
					catch(Exception e) {
						System.out.println("Enter valid age in numbers");
						System.out.print("Program terminated");
						System.exit(0);
					}
					System.out.println("Enter your Gender(M/F/T): ");
					char gender=scanner.next().charAt(0);
					if(gender=='M'||gender=='F'||gender=='T')
						user[i].setGender(gender);
					else {
						System.out.println("Mismatch character please enter again!!");
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							System.out.println(e);
						}
						getUserDetails();
					}
				}
				String seats=seatBooking(passengerCount);			//Using seat booking class to get the seats which is available
				finalTicket(user,passengerCount,seats);				//Class to print the ticket 
			}
			public String seatBooking(int requiredSeats) {	
				if(totalSeats>requiredSeats) {
					totalSeats=totalSeats-requiredSeats;
				}
				else {
					System.out.println("Seat not available for all passengers please try again!!");
					getUserDetails();
				}
				System.out.println("Enter Y/N for window seat preference \n"
						+ "Note:By giving 'Yes' there's no assurity that window seat is given to all passengers"); //getting the preference of a user
				String seats="";
				char preference=scanner.next().charAt(0);
				int iterator=1,i=0,j=0;
				String window[]=new String[21];                  
				int normal[]=new int[20];
				for(iterator=1;iterator<=40;iterator++) {
					if((iterator%4==1)||(iterator%4==0)) {     
						window[i]=iterator+"W";
						i++;
						}	
					else {
						normal[j]=iterator;						
						j++;
					}
				}
				if(preference=='Y') {
					while(requiredSeats>0) {
					if(window[k+1]!=" ") {						//required condition for the window seat
						seats+=window[k];					
						seats+=" ";
						k++;
						}
					else {
						seats+=normal[m];
						seats+=" ";
						m++;
						}
						requiredSeats--;
					}
				}
				else if(preference=='N') { 						//required condition for the normal seat
					while(requiredSeats>0) {
						if(requiredSeats%2==0) {
							if(window[k+1]!=" ") {
							seats+=window[k];
							seats+=" ";
							k++;
							}
							else {
								seats+=normal[m];
								seats+=" ";
								m++;
							}
						}	
						else {
							seats+=normal[m];
							seats+=" ";
							m++;
						}
						requiredSeats--;
					}
				}
				else {
					System.out.println("Invalid Input");
					System.exit(0);
				}	
				return seats;
			}
			public void finalTicket(userDetails[] user,int requiredSeats,String seats) {  //Printing the ticket finally
				SimpleDateFormat dateformat=new SimpleDateFormat("dd/MM/yyyy hh:mmaa");  //Date format for the date
				Date date=new Date();
				System.out.println("------------------------------------------------------");
				System.out.println("\tYour Ticket has been booked Successfully!!");
				int childcount=0;
				for(int i=0;i<requiredSeats;i++) {
					System.out.println("\tPassenger name: "+user[i].getName());				 //Printing the passenger name
					System.out.println("\tPassenger Age: "+user[i].getAge());					 //Printing the passenger Age
					if(user[i].getAge()<10) {						
						childcount++;
					}
					System.out.println("\tGender: "+user[i].getGender());						 //Printing the passenger Gender
				}
				int childfare=0;
				childfare=childcount*250;
				int adultfare=(requiredSeats-childcount)*400;
				int totalfare=childfare+adultfare;
				System.out.println("\tPickup time:10:30PM Drop time:5:30AM");
				System.out.println("\tSeat number:"+seats);								 //Printing the seat number
				System.out.println("\tChild fare:"+childcount+"*250=₹"+childfare
									+"\n\tAdult fare:"+(requiredSeats-childcount)+"*400=₹"+adultfare  //Printing the fare
									+"\n\tTotal fare:₹"+totalfare);
				System.out.println("\t"+dateformat.format(date));							 //Printing the current date and time
				System.out.println("------------------------------------------------------");
				System.out.println("*****Have a safe journey ahead!!*****");
				try {
					Thread.sleep(2000);													 //Using the thread to differentiate the end of booking
				} 
				catch (InterruptedException e) {
					System.out.print(e);
				}
				System.out.println("\nDo you want to Book again Y/N");				      //Asking the user to do a another booking
				char again=scanner.next().charAt(0);
				if(again=='Y')
					getUserDetails();
				else if(again=='N')
					System.out.println("Thank you!");
			}
}	
	 class userDetails{							//User details are get via the separate class and stored in a object array
		 	private int age;
		 	private String name;
		 	private char gender;
		 	void setName(String name) {			//setting the value
		 		this.name=name;
		 	}
		 	void setAge(int age) {
		 		this.age=age;
		 	}
		 	void setGender(char gender) {
		 		this.gender=gender;
		 	}
		 	String getName() {					//returning the value
		 		return name;
		 	}
		 	int getAge() {
		 		return age;
		 	}
		 	char getGender() {
		 		return gender;
		 	}
}
	 class Encryption{							 //Encrypting the password 
		    private char key1=56;
		    private char key2=(char)(-25);
		    public String encrypt(String pass) {
				 	String encryptedText="";
				 for(int i=0;i<pass.length();i++) { 
					 	char character=pass.charAt(i);
						if(i%2==0)
							encryptedText+=(char)(character+key1);
						else
							encryptedText+=(char)(character+key2);	
				 }
				 return encryptedText;						//return the encrypted password
			 }
}
