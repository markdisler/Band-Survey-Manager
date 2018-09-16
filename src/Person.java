
public class Person {
		
	private String firstName;
	private String lastName;
	private String nickname;
	
	private String instrument;
	private String emailAddress;
	private String classYear;
	
	private String vote;
	private boolean volunteer;
	
	private String timestamp;
	
	private boolean surveyFilledOut;
		
	public Person(String first, String last, String nickname, String instrument, String email, String classYear) {
		this.firstName = first;
		this.lastName = last;
		this.nickname = nickname;
		this.instrument = instrument;
		this.emailAddress = email;
		this.classYear = classYear;
		this.vote = "";
		this.volunteer = false;
		this.surveyFilledOut = false;
	}
	

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getNickname() {
		return nickname;
	}

	public String getInstrument() {
		return instrument;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFullName() {
		if (this.nickname.equals("")) {
			return this.firstName + " " + this.lastName;
		}
		return this.nickname + " " + this.lastName;
	}

	public String getClassYear() {
		return classYear;
	}

	public String getVote() {
		return vote;
	}

	public void setClassYear(String classYear) {
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

	public boolean isSurveyFilledOut() {
		return surveyFilledOut;
	}

	public void setSurveyFilledOut(boolean didFillOutSurvey) {
		this.surveyFilledOut = didFillOutSurvey;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getTimestamp() {
		return this.timestamp;
	}
	
	
	
	public String toString() {
		return this.firstName + " " + this.lastName;
	}
	
}
