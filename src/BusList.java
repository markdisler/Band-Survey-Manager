import java.util.ArrayList;

public class BusList {
	
	private String instrument;
	public ArrayList<Person> personnel;
	
	public BusList(String instrument) {
		this.instrument = instrument;
		this.personnel = new ArrayList<Person>();
	}
	
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	
	public String getInstrument() {
		return this.instrument;
	}
	
	public void printList() {
		System.out.println(instrument);
		System.out.println("----------------------");
		for (int i = 0; i < personnel.size(); i++) {
			System.out.print(i + 1 + ") " + personnel.get(i).getFullName());
			if(!personnel.get(i).getVote().equals("")) {
				System.out.print(" - " + personnel.get(i).getVote());
			}
			System.out.println("");
		}
		System.out.println("\n\n");
	}
	
	
}
