import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class TableOps {

	private static boolean ignoreFirstRow = true;
	private static int maxPeople = 54;

	public static ArrayList<Person> getPeopleList(String[][] table1, Map<String, ColumnMapping> mappings) {
		// First Name		
		ColumnMapping firstName = mappings.get("first_name");
		int firstNameColumnLeft = columnForLetterRepresentation(firstName.getOrigin());

		// Last Name		
		ColumnMapping lastName = mappings.get("last_name");
		int lastNameColumnLeft = columnForLetterRepresentation(lastName.getOrigin());

		// Nickname
		ColumnMapping nickname = mappings.get("nickname");
		int nicknameColumnLeft = -1;
		if (nickname != null) {
			nicknameColumnLeft = columnForLetterRepresentation(nickname.getOrigin());
		}

		// Email		
		ColumnMapping email = mappings.get("email");
		int emailColumnLeft = columnForLetterRepresentation(email.getOrigin());

		// Class Year
		ColumnMapping classYear = mappings.get("class_year");
		int classYearColumnLeft = columnForLetterRepresentation(classYear.getOrigin());

		// Instrument
		ColumnMapping instrument = mappings.get("instrument");
		int instrumentColumnLeft = columnForLetterRepresentation(instrument.getOrigin());



		ArrayList<Person> people = new ArrayList<Person>();

		for (int i = (ignoreFirstRow ? 1 : 0); i < table1.length; i++) {
			String first = table1[i][firstNameColumnLeft].trim();
			String last = table1[i][lastNameColumnLeft].trim();
			String nn = (nicknameColumnLeft != -1) ? table1[i][nicknameColumnLeft].trim() : "";
			String emailAd = table1[i][emailColumnLeft].trim();
			String year = table1[i][classYearColumnLeft].trim();
			String instr = table1[i][instrumentColumnLeft].trim();

			Person p = new Person(first, last, nn, instr, emailAd, year);
			people.add(p);
		}

		return people;

	}

	public static void fillSurveyData(ArrayList<Person> people, String[][] table2, Map<String, ColumnMapping> mappings) {

		// First Name		
		ColumnMapping firstName = mappings.get("first_name");
		int firstNameColumnRight = columnForLetterRepresentation(firstName.getOther());

		// Last Name		
		ColumnMapping lastName = mappings.get("last_name");
		int lastNameColumnRight = columnForLetterRepresentation(lastName.getOther());

		// Instrument
		ColumnMapping instrument = mappings.get("instrument");
		int instrumentColumnRight = columnForLetterRepresentation(instrument.getOther());

		// Time Stamp
		ColumnMapping timestamp = mappings.get("timestamp");
		int timestampColumnRight = columnForLetterRepresentation(timestamp.getOther());

		// Bus Response	
		ColumnMapping busResponse = mappings.get("bus_response");
		int busResponseColumnRight = columnForLetterRepresentation(busResponse.getOther());

		// Bus Response	
		ColumnMapping busVolunteer = mappings.get("bus_overflow_volunteer");
		int busVolunteerColumnRight = columnForLetterRepresentation(busVolunteer.getOther());

		for (int i = 0; i < people.size(); i++) {
			Person p = people.get(i);

			for (int j = (ignoreFirstRow ? 1 : 0); j < table2.length; j++) {

				String first = table2[j][firstNameColumnRight].trim();
				String last = table2[j][lastNameColumnRight].trim();
				String busResp = table2[j][busResponseColumnRight].trim();
				String inst = table2[j][instrumentColumnRight].trim();
				String ts = table2[j][timestampColumnRight].trim();
				boolean busVol = table2[j][busVolunteerColumnRight].equals("") ? false : true;

				String rightName = first + " " + last;
				String[] leftNames = {
						p.getFirstName() + " " + p.getLastName(),
						p.getNickname() + " " + p.getLastName()
				};

				boolean match = false;
				for (String n : leftNames) {
					if (n.toLowerCase().equals(rightName.toLowerCase())) {
						p.setInstrument(inst);
						p.setVote(busResp);
						p.setVolunteer(busVol);
						p.setSurveyFilledOut(true);
						p.setTimestamp(ts);
						match = true;
						break;
					}
				}

				if (match) {
					break;
				}
			}
		}
	}

	public static ArrayList<Person> getSurveyStragglers(ArrayList<Person> people) {
		ArrayList<Person> missingPeople = new ArrayList<Person>();
		for (Person p : people) {
			if (!p.isSurveyFilledOut()) {
				missingPeople.add(p);
			}
		}
		return missingPeople;
	}

	public static void generateBusLists(ArrayList<Person> people, String[][] busListTable, Map<String, ColumnMapping> mappings, String positiveVote, String negativeVote) {

		// Bus Lists
		Map<String, BusList> busLists = new HashMap<String, BusList>();

		// Instrument
		ColumnMapping instrument = mappings.get("instrument");
		ArrayList<String> instruments = responsesInColumn(busListTable, instrument);
		for (int i = 0; i < instruments.size(); i++) {
			busLists.put(instruments.get(i), new BusList(instruments.get(i)));
		}

		// Sort people into instruments
		for (int i = 0; i < people.size(); i++) {
			Person p = people.get(i);
			if (p.isSurveyFilledOut()) {
				if (p.getVote().equals(positiveVote)) {
					p.setVote("");
				}
				busLists.get(p.getInstrument()).personnel.add(p);
			}
		}		


		// Combine lists that sabrina wanted combined

		// Combine Altos and Tenors into "Alto/Tenor"		
		combine("Alto Sax", "Tenor Sax", "Alto/Tenor", busLists);

		// Combine Mello and Euph into "Mello/Euph"
		combine("Mello", "Euph", "Mello/Euph", busLists);

		// Combine Trombone and Tuba into "Trombone/Tuba"
		combine("Trombone", "Tuba", "Trombone/Tuba", busLists);

		// Combine Guard and Twirlers and Band Aids
		combine("Color Guard", "Twirler", "Color Guard/Twirler", busLists);
		combine("Color Guard/Twirler", "Band Aid", "Color Guard/Twirler/Band Aid", busLists);

		// Combined Clarinets and DMs
		combine("Clarinet", "DMs", "Clarinets/DMs", busLists);
		combine("Clarinets/DMs", "Pro Staff", "Clarinets/DMs/Pro Staff", busLists);


		// Export
		ArrayList<BusList> lists = new ArrayList<BusList>();
		for (BusList b : busLists.values()) {
			b.printList();
			lists.add(b);
		}

		Collections.sort(lists, new Comparator<BusList>() {
			@Override
			public int compare(BusList bl1, BusList bl2) {
				Integer s1 = bl1.personnel.size();
				Integer s2 = bl2.personnel.size();
				return s1.compareTo(s2);
			}
		});
		
		ArrayList<BusList> overflowSubLists = new ArrayList<BusList>();

		// Get overflow sublists
		for (BusList list : busLists.values()) {
			if (list.personnel.size() > TableOps.maxPeople) {
				ArrayList<Person> oflow = TableOps.getRandomOverflowPersonnel(list.personnel);
				BusList sublist = new BusList(list.getInstrument());
				sublist.personnel.addAll(oflow);
				overflowSubLists.add(sublist);
			}
		}

		// Sort Overflow Lists
		Collections.sort(overflowSubLists, new Comparator<BusList>() {
			@Override
			public int compare(BusList bl1, BusList bl2) {
				Integer s1 = bl1.personnel.size();
				Integer s2 = bl2.personnel.size();
				return -(s1.compareTo(s2));
			}
		});
		
		// Distribute sublists to places with size
		int idx = 0;
		while (overflowSubLists.size() > 0) {
			
			BusList sub = overflowSubLists.get(0);
			BusList actual = lists.get(idx);
			
			if (actual.personnel.size() + sub.personnel.size() <= TableOps.maxPeople) {  // see if merged list fits
				actual.personnel.addAll(sub.personnel);
				overflowSubLists.remove(0);
			}
			
			idx += 1;
		}


		saveToFile(busLists);
		SpreadsheetManager.createExcelFile(lists);

	}

	private static ArrayList<Person> getRandomOverflowPersonnel(ArrayList<Person> personnel) {

		int numNeeded = personnel.size() - TableOps.maxPeople;

		ArrayList<Person> overflow = new ArrayList<Person>();
		for (int i = 0; i < personnel.size(); i++) {
			Person p = personnel.get(i);
			if (p.isVolunteer()) {
				overflow.add(p);
				numNeeded -= 1;
				personnel.remove(i);
				i--;
			}

			if (numNeeded == 0) {
				break;
			}
		}

		if (numNeeded > 0) {
			ArrayList<Person> clonePersonnel = new ArrayList<Person>(personnel);
			Collections.sort(clonePersonnel, new Comparator<Person>() {

				@Override
				public int compare(Person p1, Person p2) {
					String c1 = p1.getClassYear();
					String c2 = p2.getClassYear();
					int cComp = c1.compareTo(c2);

					if (cComp != 0) {
						return cComp;
					}

					String t1 = p1.getTimestamp();
					String t2 = p2.getTimestamp();
					return t1.compareTo(t2);
				}
			});

			System.out.println("\n\n");
			for (Person p: clonePersonnel) {
				System.out.println(p.getFullName() + "   " + p.getClassYear() + "   " + p.getTimestamp());
			}

			List<Person> of = clonePersonnel.subList(clonePersonnel.size() - numNeeded, clonePersonnel.size());
			overflow.addAll(of);
			personnel.removeAll(of);
		}

		return overflow;
	}

	private static String reformCase(String name) {
		String[] parts = name.split(" ");
		String newS = "";
		for (int i = 0; i < parts.length; i++) {
			String p = parts[i];
			newS += p.substring(0, 1).toUpperCase() + p.substring(1, p.length());
			if (i < parts.length - 1) {
				newS += " ";
			}
		}
		return newS;
	}

	private static void saveToFile(Map<String, BusList> busLists) {
		try {
			PrintWriter writer = new PrintWriter("/Users/Mark/Desktop/list.txt");

			int personnelCount = 0;

			for (BusList b : busLists.values()) {
				writer.println(b.getInstrument());
				writer.println("----------------------");
				for (int i = 0; i < b.personnel.size(); i++) {
					if (!b.personnel.get(i).isVolunteer()) {
						writer.print(b.personnel.get(i).getFullName());
						personnelCount++;
						if(!b.personnel.get(i).getVote().equals("")) {
							writer.print(" - " + b.personnel.get(i).getVote());
						}	
						writer.println("");
					}
				}

				for (int i = 0; i < b.personnel.size(); i++) {
					if (b.personnel.get(i).isVolunteer()) {
						writer.print(b.personnel.get(i).getFullName());
						writer.print("*");

						personnelCount++;
						if(!b.personnel.get(i).getVote().equals("")) {
							writer.print(" - " + b.personnel.get(i).getVote());
						}	
						writer.println("");
					}
				}

				writer.println("\n\n");
			}


			writer.println("Total Personnel: " + personnelCount);

			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void combine(String instrument1, String instrument2, String combinedName, Map<String, BusList> busLists) {
		BusList one = busLists.get(instrument1);
		BusList two = busLists.get(instrument2);
		BusList oneTwo = new BusList(combinedName);
		oneTwo.personnel.addAll(one.personnel);
		oneTwo.personnel.addAll(two.personnel);
		busLists.put(combinedName, oneTwo);
		busLists.remove(instrument1);
		busLists.remove(instrument2);
	}

	public static ArrayList<String> responsesInColumn(String[][] table, ColumnMapping map) {
		if (map == null) {
			return new ArrayList<String>();
		}

		int col = columnForLetterRepresentation(map.getOther());
		HashSet<String> set = new HashSet<String>();
		for (int i = 1; i < table.length; i++) {
			set.add(table[i][col]);
		}
		return new ArrayList<String>(set);
	}

	private static int columnForLetterRepresentation(String r) {

		if (r.length() > 0) {
			char start = 'A';
			char c = r.charAt(0);
			int index = c - start;
			return index;
		}
		return -1;
	}

}
