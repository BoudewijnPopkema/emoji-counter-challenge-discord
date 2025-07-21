package challenges3;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;
import javax.swing.JTextArea;

public class Process {

	ArrayList<Emoji> emojis = new ArrayList<>();
	private String huidigPersoon = "";
	private ArrayList<Persoon> personen = new ArrayList<>();
	private String ss = "";
	private String separator = " — "; // dit is de string die discord gebruikt om na een persoon de datum weer te geven.
	private static final String SPACE = " ";
	public enum EmojiType {
		BONUS,
		DONE,
		PROMISE
	}
	
	

	// voor gebruik met de GUI
	public Process(BufferedReader brEmojiPromise, BufferedReader brEmojiDone, BufferedReader br_emoji_bonus, ArrayList<BufferedReader> br_Week, JTextArea outputArea) throws IOException {
		maakEmojis(brEmojiDone, EmojiType.DONE);
		maakEmojis(brEmojiPromise, EmojiType.PROMISE);
		maakEmojis(br_emoji_bonus, EmojiType.BONUS);
		//maakPersonen(br_bepaling);
		String results = verwerk(br_Week);
		//printInstellingen();
		System.out.println(results);

	}
	
	// voor gebruik met de reader
	public Process(BufferedReader brEmojiPromise, BufferedReader brEmojiDone, BufferedReader br_emoji_bonus, ArrayList<BufferedReader> br_Week) throws IOException {
		maakEmojis(brEmojiDone, EmojiType.DONE);
		maakEmojis(brEmojiPromise, EmojiType.PROMISE);
		maakEmojis(br_emoji_bonus, EmojiType.BONUS);
		//maakPersonen(br_bepaling);
		String results = verwerk(br_Week);
		//printInstellingen();
	}



	private void printInstellingen() {
		System.out.println("===== INSTELLINGEN ==============");
		for (Emoji emoji: emojis) {
			emoji.print();
		}
		
	}


	private void maakPersonen(BufferedReader br_bepaling) throws IOException {
		while ((ss=br_bepaling.readLine()) !=null) {
			maakPersoon(ss);
		}
	}

	private void maakPersoon(String ss2) {
		//System.out.println(ss2);
		String naam = ss2.substring(0,ss2.indexOf(" — "));
		int aantal = Integer.parseInt(ss2.substring(ss2.indexOf(" — ")+5,ss2.indexOf(",")));
		String weekdata = ss2.substring(ss2.indexOf(",")+1,ss2.length());
		int week = 0;
		int vakantieweek = 0;
		if (weekdata.contains(",")) {
			week = Integer.parseInt(weekdata.substring(0,weekdata.indexOf(",")));
			vakantieweek = Integer.parseInt(weekdata.substring(weekdata.indexOf(",")+1,weekdata.length()));
		} else {
			week = Integer.parseInt(weekdata);
			vakantieweek = 0;
		}
		System.out.println("naam = " + naam + "   aantal "+ aantal + "week " + week);
		Persoon persoon = new Persoon(naam, aantal, week, vakantieweek);
		personen.add(persoon);
	}

	/**private void maakEmojis(BufferedReader brEmoji) throws IOException {
		while ((ss=brEmoji.readLine()) != null) {
			System.out.println("emojiline= "+ss);
			maakEmoji(ss);
		}
	}**/
	private void maakEmojis(BufferedReader brEmojiPromise, EmojiType type) throws IOException {
		while ((ss=brEmojiPromise.readLine()) != null) {
			System.out.println("emojiline= "+ss);
			maakEmoji(ss, type);
		}
	}
	
	private void maakEmoji(String ss2, EmojiType type) {
		String emojiString = ss.substring(0,ss.indexOf(SPACE));
		int points = Integer.parseInt(ss.substring(ss.indexOf(SPACE)+1,ss.length()));
		Emoji emoji = new Emoji(emojiString, emojiString, points, type);
		emojis.add(emoji);
	}

	
	public String verwerk(ArrayList<BufferedReader> br) throws IOException {
		String result = "";
		int weeknumber = 0;
		for (BufferedReader br_week:br) {
			result = "";
			String onelineAgo = "";
			String twolinesAgo = "";
			while ((ss=br_week.readLine()) != null) {
				read(ss + ",", onelineAgo, twolinesAgo);
				onelineAgo = ss;
				twolinesAgo = onelineAgo;
			}

			// personen sorteren
			for (Persoon p : personen) {
				p.nieuweWeek();
			}
			Collections.sort(personen);
			System.out.println(" score-bord: ");
			result = result + "## Scorebord week " + weeknumber;
			int num = 1;
			for (Persoon p : personen) {
				result = result + p.getPoms(num);
				num++;
			}
			weeknumber++;
		}
		return result;
	}


	private void read(String ss, String oneLineAgo, String twoLinesago) {
		if (ss.contains(separator)) { //  —
			huidigPersoon = ss.substring(0, ss.indexOf(" — "));
			if (huidigPersoon.equals(twoLinesago) && oneLineAgo.startsWith("[") && oneLineAgo.endsWith("]")) { // voor mensen met server tags, dat wordt raar geformatteerd
				huidigPersoon = twoLinesago;
			}
		} else {
			addPoints(ss);
		}
	}

	private void addPoints(String ss) {
		for (Emoji emoji: emojis) {
			int aantal = countOccurences(ss, emoji.getChar());
			if (aantal > 0) {
				getPersoon().add(emoji, aantal);
				// Deze kan het detecten van emoji's troubleshooten
				//System.out.println(getPersoon().getNaam()+" heeft " + aantal + " van emoji " + emoji.getChar() +" gehaald");
			}
		}
	}

	private int countOccurences(String source, String find) {
		return (int) Pattern.compile(find) // Pattern
		        .matcher(source) // Matcher
		        .results()       // Stream<MatchResults>
		        .count();
	}


	private Persoon getPersoon() {
		for (Persoon p : personen) {
			if (p.getNaam().equals(huidigPersoon)) {
				return p;
			}
		}
		System.out.println("Error!");
		Persoon p = new Persoon(huidigPersoon, 0, 0, 0);
		personen.add(p);
		return p;
	}

}
