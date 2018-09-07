
public class ColumnMapping {

	private String origin;
	private String other;
	private String designation;
	

	public ColumnMapping(String from, String to, String designation) {
		this.origin = from;
		this.other = to;
		this.designation = designation;
	}
	
	public String getOrigin() {
		return origin;
	}

	public String getOther() {
		return other;
	}

	public String getDesignation() {
		return designation;
	}
	
	public String toString() {
		return this.origin + ", " + this.other + ", " + this.designation;
	}
}
