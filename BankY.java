package tech;
//Creating the database table named as SYSBANK with columns like account_number(AACNO), user_name(NAME),balance(BALANCE),account_type(saving/current(ACCTYPE))
//CREATE TABLE SYSBANK(ACCNO NUMBER,NAME VARCHAR2(20), BALANCE NUMBER, ACCTYPE VARCHAR2(10));
import java.util.*;
import java.sql.*;
public class BankY  {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		try(sc;){
			try {
				//Loading The Driver
				Class.forName("oracle.jdbc.driver.OracleDriver");
				//Create Connection
				//1521-port no. , orcl-service name of oracle product,MYDB- user_name, MYDB-password of oracle-sql
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","MYDB","MYDB"); 
				//Preparing Statement
				Statement stm = con.createStatement();
				//Preparing Statement
				PreparedStatement ps1=con.prepareStatement("SELECT * FROM SYSBANK WHERE ACCNO = ?");
				PreparedStatement ps2=con.prepareStatement("UPDATE SYSBANK SET BALANCE = BALANCE+? WHERE ACCNO = ?");
				con.setAutoCommit(false);
				Savepoint sp=con.setSavepoint();
				while(true) {
					System.out.println("Available Choices : \n1. Create Account \n"
							                           + "2. Deposit\n"
							                           + "3. Withdraw\n"
							                           + "4. Transfer\n"
							                           + "5. Exit");
					System.out.println("Enter your choice : ");
					int choice =Integer.parseInt(sc.nextLine());
					if(choice<=0 || choice>5) {
						System.out.println("Invalid Choice");
					}
					
					switch(choice) {
					//CREATE ACCOUNT
					case 1:{
							System.out.println("Enter the User Name : ");
							String uname = sc.nextLine();
							System.out.println("Enter the Account Type : ");
							String acctyp = sc.nextLine();
							System.out.println("Enter the AccountNumber : ");
							long accnum = Long.parseLong(sc.nextLine());
							System.out.println("Enter the Balance");
							float bal = Float.parseFloat(sc.nextLine());
							ps1.setLong(1, accnum);
							ResultSet rs  = ps1.executeQuery();
							if(rs.next()) {
								System.out.println("This account is already exist");
							}
							else {
								//Executing Queries for creating account 
								//Inserting user details 
								int k = stm.executeUpdate("INSERT INTO SYSBANK VALUES("+accnum+", '"+uname+"',"+bal+",'"+acctyp+"')");
								if(k>0) {
									System.out.println("Account created successfully");
								}
							}
						break;
					}
					
					//DEPOSIT AMOUNT
					case 2:{
						System.out.println("Enter the AccountNumber : ");
						long accnum = Long.parseLong(sc.nextLine());
						ps1.setLong(1, accnum);
						ResultSet rs1 = ps1.executeQuery();
						
						if(rs1.next()) {
							System.out.println("Enter the Amount : ");
							int amount = Integer.parseInt(sc.nextLine());
							if(amount%100==0) {
								ps2.setFloat(1, amount);
								ps2.setLong(2, accnum);
								int i = ps2.executeUpdate();
								if(i>0) {
									con.commit();
									System.out.println("Transaction successfull");
								}
								
								else {
									con.rollback(sp);
									System.out.println("Transaction failed");
								}
							}
							
							else {
								System.out.println("Enter in form of 100 notes");
							}
						}else {
							System.out.println("Invalid AccountNumber");
						}
						break;
					}
					
					//WITHDRAW AMOUNT
					case 3:{
						System.out.println("Enter the AccountNumber : ");
						long accnum = Long.parseLong(sc.nextLine());
						ps1.setLong(1, accnum);
						ResultSet rs1 = ps1.executeQuery();
						
						if(rs1.next()) {
							float bal = rs1.getFloat(3);
							System.out.println("Enter the Amount : ");
							int amount = Integer.parseInt(sc.nextLine());
							if(amount%100==0) {
								if(amount<bal) {
									ps2.setFloat(1, -amount);
									ps2.setLong(2, accnum);
									int i = ps2.executeUpdate();
									if(i>0) {
										con.commit();
										System.out.println("Transaction successfull");
									}
									
									else {
										con.rollback(sp);
										System.out.println("Transaction failed");
									}
								}
								else {
									System.out.println("Insufficient funds");
								}
							}
							
							else {
								System.out.println("Enter in form of 100 notes");
							}
						}else {
							System.out.println("Invalid AccountNumber");
						}
						break;
					}
					
					//TRANSACTION FOR TRANSFER
					case 4:{
						System.out.println("Enter the HomeAccountNumber(From) : ");
						long haccnum = Long.parseLong(sc.nextLine());
						ps1.setLong(1, haccnum);
						ResultSet rs1 = ps1.executeQuery();
						
						if(rs1.next()) {
							float bal = rs1.getFloat(3);
							System.out.println("Enter the Benificier AccountNumber(To) : ");
							long baccnum = Long.parseLong(sc.nextLine());
							ps1.setLong(1, baccnum);
							ResultSet rs2 = ps1.executeQuery();
							
							if(rs2.next()) {
								System.out.println("Enter the Amount : ");
								float amount = Float.parseFloat(sc.nextLine());
								if(amount<bal) {
									ps2.setFloat(1, -amount);
									ps2.setLong(2, haccnum);
									int i = ps2.executeUpdate();
									
									ps2.setFloat(1, +amount);
									ps2.setLong(2, baccnum);
									int j = ps2.executeUpdate();
									
									if(i>0 && j>0) {
										con.commit();
										System.out.println("Transaction Successfull");
									}
									else {
										con.rollback(sp);
										System.out.println("Transaction Failed");
									}
								}
								else {
									System.out.println("Insufficient funds");
								}
							}
							
							else {
								System.out.println("Invalid Benificier AccountNumber(To)");
							}
						}
						else {
							System.out.println("Invalid HomeAccountNumber(From)");
						}
						break;
					}
					
					//EXIT FROM THE TRANSACTION
					case 5:{
						System.out.println("Transaction Exited");
						System.exit(0);
					}
					}
				}
			}
			
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
