import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ToolbarButton extends JButton {

	public ToolbarButton() {
		super();
		commonInit();
	}
	
	public ToolbarButton(String title, ImageIcon image) {
		super(title, image);
		commonInit();
	}
	
	public void commonInit() {
		this.setHorizontalTextPosition(JButton.CENTER);
		this.setVerticalTextPosition(JButton.BOTTOM);
		this.setContentAreaFilled(false);
		this.setOpaque(false);
		this.setBorderPainted(false);
	}
	
}
