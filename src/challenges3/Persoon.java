package challenges3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import challenges3.Process.EmojiType;

public class Persoon implements Comparable<Persoon> {
	private String naam = "";
	private double score = 0;
	private int doel;
	private int bonus;
	private int week;
	private int huidige_week = 0;
	private double percentage = 0;
	private ArrayList<Double> scores = new ArrayList<>();
	private ArrayList<Integer> doelen = new ArrayList<>();
	private ArrayList<Integer> bonussen = new ArrayList<>();
	private boolean pannenkoekenbord = true;
	private int vakantieweek = 0;
	private int promise = 0;
	private ArrayList<Emoji> emojis = new ArrayList<>();

	private void printData() {
		System.out.println(" naam = " + naam);
		System.out.println(" score = " + score);
		System.out.println(" doel = " + doel);
		System.out.println(" bonus = " + bonus);
		System.out.println(" week = " + week);
		System.out.println(" huidige_week = " + huidige_week);
		System.out.println(" percentage = " + percentage);
	}

	public Persoon(String huidigPersoon, int doel, int week, int vakantieweek, ArrayList<Emoji> emojis) {
		naam = huidigPersoon;
		this.doel = doel;
		this.week = week;
		this.vakantieweek = vakantieweek;
		this.emojis = emojis;
		System.out.println(" Persoon aangemaakt: naam= " + huidigPersoon + " doel= " + doel + " week= " + week);
	}

	public Persoon(String huidigPersoon, int doel, int week, int vakantieweek) {
		naam = huidigPersoon;
		this.doel = doel;
		this.week = week;
		this.vakantieweek = vakantieweek;
		System.out.println(" Persoon aangemaakt: naam= " + huidigPersoon + " doel= " + doel + " week= " + week);
	}

	public void add(int aantal) {
		score = score + aantal;
	}

	public String getNaam() {
		return naam;
	}

	public double getScore() {
		if (doel > 0) {
			if (week >= vakantieweek && vakantieweek > 0) {
				return ((doel * (huidige_week - week)) - score) / doel;

			} else {
				return ((doel * (huidige_week - week + 1)) - score) / doel;
			}
		} else {
			return 0;
		}
	}

	public double getPercentage() {
		return percentage;
	}

	@Override
	public int compareTo(Persoon o) {
		System.out.println("persoon " + this.getNaam() + " heeft score: " + this.getTotaleScore() + " en persoon "
				+ o.getNaam() + " heeft " + o.getTotaleScore());
		return o.getTotaleScore() - this.getTotaleScore();

	}

	private int getTotal() {
		return (int) Variance.getSom(scores);
	}

	public String getPoms(int num) {

		if (naam.equals("")) {
			return null;
		}
		int puntentotaal = getTotaleScore();
		int bonustotaal = getBonussenOpgeteld();
		int bonusdezeweek = bonussen.get(bonussen.size() - 1);
		int bonusvoorheen = bonustotaal - bonusdezeweek;
		int basistotaal = getScoresOpgeteld();
		int basisdezeweek = (int) Math.round(scores.get(scores.size() - 1));
		int basisvoorheen = (int) (basistotaal - basisdezeweek);
		String basisemoji = ":fish:";
		int inzet = doelen.get(doelen.size() - 1);
		String bonusemoji = ":one:";
		if (doelen.size() > 0) {
			if (puntentotaal == 0 && inzet == 0) {
				return "";
			}
			// nieuwe format: <bonusemoji> <puntentotaal> <Naam> (<basispunten
			// hiervoor>+basispunten deze week>)=totaal basispunten <emoji> & (0 + 17)=17
			// bonuspunten inzet: 20 :1pomodorog:
			System.out.println("#. " + getPromiseGehaald() + naam + get_spaces(naam, getPromiseGehaald()) + "("
					+ (getScoresOpgeteld()) + "+" + getBonussenOpgeteld() + "=)" + getTotaleScore() + ":fish:"
					+ " inzet volgende week: " + doelen.get(doelen.size() - 1) + ":1pomodorog:");
			return "\n" + num + ". " + getPromiseGehaald() + " **" + puntentotaal + "** " + naam
					+ get_spaces(naam, getPromiseGehaald()) + "(" + basisvoorheen + "+" + basisdezeweek + ") = **"
					+ basistotaal + "** " + basisemoji + " & (" + bonusvoorheen + "+" + bonusdezeweek + ") = **"
					+ bonustotaal + "** " + bonusemoji + "      Inzet: " + inzet;
			// return "\n" + num + ". " + getPromiseGehaald() + checkForChars(naam) +
			// get_spaces(naam, "")+"(" + getScoresOpgeteld() +"+"+ getBonussenOpgeteld()
			// +"=)"+getTotaleScore()+":sunny:" + " inzet volgende week: " +
			// doelen.get(doelen.size()-1)+":1pomodorog:";
		} else {
			if (puntentotaal == 0) {
				return "";
			}
			System.out.println("#. " + naam + "              (" + getScoresOpgeteld() + "+" + getBonussenOpgeteld()
					+ "=)" + getTotaleScore() + ":fish:" + " inzet volgende week: " + 0 + ":1pomodorog:");
			return "\n" + num + ". " + " **" + puntentotaal + "** " + naam + get_spaces(naam, getPromiseGehaald()) + "("
					+ basisvoorheen + "+" + basisdezeweek + ") = " + basistotaal + " " + basisemoji + "  &  ("
					+ bonusvoorheen + "+" + bonusdezeweek + ") = " + bonustotaal + " " + bonusemoji + "   Inzet: "
					+ inzet;

		}
	}

	private String checkForChars(String naam2) {
		// Define special characters that need to be escaped
		String specialChars = "_*";

		// Use a regular expression to replace each special character with an escaped
		// version
		for (char c : specialChars.toCharArray()) {
			naam2 = naam2.replace(Character.toString(c), "\\" + c);
		}

		return naam2;
	}

	private String get_spaces(String naam2, String string) {
		String spaces = "";
		int length = 30 - naam2.length();
		if (string.length() > 0) {
			length = length - 3;
		}
		while (length > 0) {
			spaces = spaces + " ";
			length--;
		}

		return spaces;
	}

	private String getPromiseGehaald() {
		if (doelen.size() > 1 && doelen.get(doelen.size() - 2) > 0
				&& scores.get(scores.size() - 1) >= doelen.get(doelen.size() - 2)) {
			ArrayList<String> promiseSuccessEmojis = new ArrayList<>();
			for (Emoji emoji : emojis) {
				if (emoji.getType() == EmojiType.REWARD) {
					promiseSuccessEmojis.add(emoji.getChar());
				}
			}
			if (!promiseSuccessEmojis.isEmpty()) {
				int index = new Random().nextInt(promiseSuccessEmojis.size());
				return promiseSuccessEmojis.get(index);
			}
		}
		return "";
	}

	public void add(Emoji emoji, int aantal) {
		if (emoji.getType() == EmojiType.BONUS) {
			bonus = bonus + emoji.getPoints() * aantal;
		} else if (emoji.getType() == EmojiType.DONE) {
			score = score + emoji.getPoints() * aantal;
		} else if (emoji.getType() == EmojiType.PROMISE) {
			doel = doel + emoji.getPoints() * aantal;
		} else if (emoji.getType() == EmojiType.REWARD) {
			// Rewards worden niet opgeteld, alleen weergegeven.
		} else {
			System.out.println(" Deze emoji had geen goed type!");
		}

	}

	public void nieuweWeek() {
		doelen.add(doel);
		scores.add(score);
		bonussen.add(bonus);
		doel = 0;
		score = 0;
		bonus = 0;
	}

	public int getScoresOpgeteld() {
		int sum = 0;
		for (Double score : scores) {
			sum += score;
		}
		return Math.round(sum);
	}

	public int getBonussenOpgeteld() {
		int sum = 0;
		for (int bonus : bonussen) {
			sum += bonus;
		}
		return sum;
	}

	public int getTotaleScore() {
		return getScoresOpgeteld() + getBonussenOpgeteld();
	}
}
