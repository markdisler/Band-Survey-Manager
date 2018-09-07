import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TableOps {

	public static ArrayList<String> getSetDifference(String[][] table1, String[][] table2, Map<String, ColumnMapping> mappings) {

		// First Name		
		ColumnMapping firstName = mappings.get("first_name");
		int firstNameColumnLeft = columnForLetterRepresentation(firstName.getOrigin());
		int firstNameColumnRight = columnForLetterRepresentation(firstName.getOther());

		// Last Name		
		ColumnMapping lastName = mappings.get("last_name");
		int lastNameColumnLeft = columnForLetterRepresentation(lastName.getOrigin());
		int lastNameColumnRight = columnForLetterRepresentation(lastName.getOther());

		// Nickname Weed Out
		ColumnMapping nickname = mappings.get("nickname");
		int nicknameColumnLeft = -1;
		if (nickname != null) {
			nicknameColumnLeft = columnForLetterRepresentation(nickname.getOrigin());
		}

		boolean ignoreFirstRow = true;

		ArrayList<String> leftTableFullNames = new ArrayList<String>();
		ArrayList<String> rightTableFullNames = new ArrayList<String>();

		// Left Names
		for (int i = (ignoreFirstRow ? 1 : 0); i < table1.length; i++) {
			String first = table1[i][firstNameColumnLeft].trim();
			String last = table1[i][lastNameColumnLeft].trim();
			String nameEntry = first + " " + last;

			if (nicknameColumnLeft != -1) {
				String nicknamePart = table1[i][nicknameColumnLeft].trim();
				if (!nicknamePart.equals("")) {
					nameEntry += "," + nicknamePart + " " + last;
				}
			}

			leftTableFullNames.add(nameEntry);
		}

		// Right Names
		for (int i = (ignoreFirstRow ? 1 : 0); i < table2.length; i++) {
			String first = table2[i][firstNameColumnRight].trim();
			String last = table2[i][lastNameColumnRight].trim();
			rightTableFullNames.add(first + " " + last);
		}

		for (int i = 0; i < rightTableFullNames.size(); i++) {
			for (int j = 0; j < leftTableFullNames.size(); j++) {
				String rightName = rightTableFullNames.get(i).toLowerCase();
				String[] leftNames = leftTableFullNames.get(j).split(",");

				boolean match = false;
				for (String n : leftNames) {
					if (n.toLowerCase().equals(rightName)) {
						leftTableFullNames.remove(j);
						j--;
						match = true;
						break;
					}
				}

				if (match) {
					break;
				}
			}
		}

		// Get the full names to return
		ArrayList<String> missingPeople = new ArrayList<String>();
		for (String e : leftTableFullNames) {
			String[] parts = e.split(",");
			missingPeople.add(parts[0]);
		}

		return missingPeople;
	}

	public static String getEmailAddressForPerson(String[][] rosterTable, String name, Map<String, ColumnMapping> mappings) {
		// First Name		
		ColumnMapping firstName = mappings.get("first_name");
		int firstNameColumnLeft = columnForLetterRepresentation(firstName.getOrigin());

		// Last Name		
		ColumnMapping lastName = mappings.get("last_name");
		int lastNameColumnLeft = columnForLetterRepresentation(lastName.getOrigin());

		// Email		
		ColumnMapping email = mappings.get("email");
		int emailLeft = columnForLetterRepresentation(email.getOrigin());
		
		for (int i = 1; i < rosterTable.length; i++) {
			String firstN = rosterTable[i][firstNameColumnLeft].trim();
			String lastN = rosterTable[i][lastNameColumnLeft].trim();
			String fullN = firstN + " " + lastN;
			
			if (fullN.equals(name)) {
				return rosterTable[i][emailLeft].trim();
			}
		}
		return "";
	}

	//{"First Name", "Last Name", "Nickname", "Full Name", "Instrument", "Email", "Bus Response", "Bus Overflow"};
	public static ArrayList<BusList> generateBusLists(String[][] busListTable, Map<String, ColumnMapping> mappings, String positiveVote, String negativeVote) {

		// Bus Lists
		Map<String, BusList> busLists = new HashMap<String, BusList>();

		// Instrument
		ColumnMapping instrument = mappings.get("instrument");
		ArrayList<String> instruments = responsesInColumn(busListTable, instrument);
		for (int i = 0; i < instruments.size(); i++) {
			busLists.put(instruments.get(i), new BusList(instruments.get(i)));
		}

		// People List

		// First Name		
		ColumnMapping firstName = mappings.get("first_name");
		int firstNameColumnRight = columnForLetterRepresentation(firstName.getOther());

		// Last Name		
		ColumnMapping lastName = mappings.get("last_name");
		int lastNameColumnRight = columnForLetterRepresentation(lastName.getOther());

		// Instrument		
		int instrumentColumnRight = columnForLetterRepresentation(instrument.getOther());

		// Class Year		
		//ColumnMapping classYear = mappings.get("class_year");
		//int classYearColumnRight = columnForLetterRepresentation(classYear.getOther());

		// Bus Response	
		ColumnMapping busResponse = mappings.get("bus_response");
		int busResponseColumnRight = columnForLetterRepresentation(busResponse.getOther());

		// Bus Response	
		ColumnMapping busVolunteer = mappings.get("bus_overflow_volunteer");
		int busVolunteerColumnRight = columnForLetterRepresentation(busVolunteer.getOther());

		boolean ignoreFirstRow = true;

		// Right Data Strings
		for (int i = (ignoreFirstRow ? 1 : 0); i < busListTable.length; i++) {
			String first = busListTable[i][firstNameColumnRight].trim();
			String last = busListTable[i][lastNameColumnRight].trim();
			String inst = busListTable[i][instrumentColumnRight];
			String vote = busListTable[i][busResponseColumnRight];
			String yearS = "0";//busListTable[i][classYearColumnRight];
			int year = Integer.parseInt(yearS);
			boolean vol = busListTable[i][busVolunteerColumnRight].equals("") ? false : true;

			if (vote.equals(positiveVote)) {
				busLists.get(inst).personnel.add(new Person(first + " " + last, year, "", vol));
			} else if (!vote.equals(negativeVote)) {
				busLists.get(inst).personnel.add(new Person(first + " " + last, year, vote, vol));
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

		for (BusList b : busLists.values()) {
			b.printList();
		}

		saveToFile(busLists);

		// Check each bus for overflow sublist
		// - take people who volunteered
		// - rest taken randomly from class of 2022
		// - disperse sublist into other list with more space


		return null;
	}

	private static void saveToFile(Map<String, BusList> busLists) {
		try {
			PrintWriter writer = new PrintWriter("/Users/Mark/Desktop/list.txt");

			int personnelCount = 0;

			for (BusList b : busLists.values()) {
				writer.println(b.getInstrument());
				writer.println("----------------------");
				for (int i = 0; i < b.personnel.size(); i++) {
					writer.print(i + 1 + ") " + b.personnel.get(i).getName());
					personnelCount++;

					if(!b.personnel.get(i).getVote().equals("")) {
						writer.print(" - " + b.personnel.get(i).getVote());
					}
					writer.println("");
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
