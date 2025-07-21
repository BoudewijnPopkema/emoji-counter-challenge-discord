package challenges3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;


public class reader {
	// Change this string to the amount of weeks
	static int amount_of_weeks = 11;
	
	// Change this string to the folderLocation all the txt files are in.
	static String folderLocation = "D:\\OU\\emoji-challenge-q4\\";

	//static String LocatieBepaling = folderLocation+"Bepaling.txt"; // txt file met hoeveel pannekoeken iedereen doet en vanaf welke week ze mee doen

	static String LocatiePromise = folderLocation+"promise.txt"; // txt file met de emoji's die geteld moeten worden met hun punten en commando
	static String LocatieDone = folderLocation+"done.txt"; // txt file met de emoji's die geteld moeten worden met hun punten en commando
	static String LocatieBonus = folderLocation+"bonus.txt"; // txt file met de bonus emoji's die geteld moeten worden met hun punten
	static String LocatieReward = folderLocation+"reward.txt"; // txt file met de reward emoji's
	
	
	private static BufferedReader getBufReader(String fileLocation) throws FileNotFoundException {
		return new BufferedReader(new FileReader(new File(fileLocation)));
	}
	
	public static void main(String[] args) throws Exception {
		// get file locations, and files
		int week = 0 ;
		ArrayList<BufferedReader> br_week_list = new ArrayList<>();
		
		// Retrieve all data from the chatW<week>.txt's
		// Start with week 0: here people can input their promises for the first week.
		while (week <= amount_of_weeks) {
			String file_name = folderLocation+"chatW"+week+".txt";
			File file = new File(file_name);
			if (file.exists() && file.isFile()) {
				br_week_list.add(new BufferedReader(new FileReader(file)));
			}
			week++;
		}
		
		// Run the full process to generate results
		try {
			new Process(getBufReader(LocatiePromise), getBufReader(LocatieDone), getBufReader(LocatieBonus), getBufReader(LocatieReward), br_week_list);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
	}	
}