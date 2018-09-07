import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class ResourceLoader {
	
	public static ResourceLoader r1 = new ResourceLoader();
	
	public static Image getImage(String name) {
		
		if (!name.contains(".")) {
			name = name + ".png";
		}
		return Toolkit.getDefaultToolkit().getImage(r1.getClass().getResource("images/" + name));
	}
	
	public static ImageIcon getIcon(String name) {
		return new ImageIcon(getImage(name));
	}

}
