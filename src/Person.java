
public class Person {
	
	private String name;
	private int classYear;
	private String vote;
	private boolean volunteer;
	
	public Person(String name, int year, String vote, boolean willingToMove) {
		this.name = name;
		this.classYear = year;
		this.vote = vote;
		this.setVolunteer(willingToMove);
	}

	public String getName() {
		return name;
	}

	public int getClassYear() {
		return classYear;
	}

	public String getVote() {
		return vote;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setClassYear(int classYear) {
		this.classYear = classYear;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}

	public boolean isVolunteer() {
		return volunteer;
	}

	public void setVolunteer(boolean volunteer) {
		this.volunteer = volunteer;
	}
	
}
