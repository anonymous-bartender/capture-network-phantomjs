package utils;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

public class CustomSwingGroup {
	
	private List<JComponent> group;
	
	public CustomSwingGroup() {
		group = new ArrayList<JComponent>();
		
	}
	
	public void add(Object object) {
		group.add((JComponent) object);
	}
	
	public void setEnable(boolean enable) {
		for(JComponent e: group) {
			e.setEnabled(enable);
		}
		
	}
	
	public void setVisible(boolean visible) {
		for(JComponent e: group) {
			e.setVisible(visible);
		}
		
	}
	

	public void toggleEnable() {
		boolean b;
		for(JComponent e: group) {
			b = e.isEnabled();
			e.setEnabled(!b);
		}
		
	}

}
