import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JPanel;

public class Toolbar extends JPanel {

	public JPanel leftSection, centerSection, rightSection;
	
	public Toolbar() {
		super(new BorderLayout());
		
		this.leftSection = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.centerSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
		this.rightSection = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		this.add(this.leftSection, BorderLayout.WEST);
		this.add(this.centerSection, BorderLayout.CENTER);
		this.add(this.rightSection, BorderLayout.EAST);
	}
	
	public void setBackground(Color bg) {
		super.setBackground(bg);
		
		if (this.leftSection != null)
			this.leftSection.setBackground(bg);
		
		if (this.centerSection != null)
			this.centerSection.setBackground(bg);
		
		if (this.rightSection != null)
			this.rightSection.setBackground(bg);
	}
	
}
