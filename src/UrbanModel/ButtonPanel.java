package UrbanModel;

import java.util.ArrayList;
import processing.core.PApplet;

public class ButtonPanel {
	public class Button {
		private String label;
		private int id;
		private char key;
		private boolean selected = false;

		public Button(String name, int id, char key) {
			this.label = name;
			this.id = id;
			this.key = key;
		}

		public String getLabel() {
			return label;
		}
		
		public void setLabel(String label) {
			this.label = label;
		}
		
		public int getId() {
			return id;
		}
		
		public char getKey() {
			return key;
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}

	
	private final int buttonWidth;
	private final int buttonHeight;
	private final int buttonGap;
	
	private final ArrayList<Button> buttons = new ArrayList<Button>();

	private int x = 10;
	private int y = 10;
	private int w = 0;
	private int h = 0;

	
	public ButtonPanel() {
		this(30, 30, 5);
	}
	
	public ButtonPanel(int buttonWidth, int buttonHeight, int buttonGap) {
		this.buttonWidth = buttonWidth;
		this.buttonHeight = buttonHeight;
		this.buttonGap = buttonGap;
	}

	public void addButton(String label, int id, char key) {
		buttons.add(new Button(label, id, key));
		w = buttons.size() * buttonWidth + (buttons.size() - 1) * buttonGap;
		h = buttonHeight;
	}
	
	public void draw2D(PApplet p) {
		if (buttons.isEmpty())
			return;
		
		p.noStroke();
		int i = 0;
		for (Button button : buttons) {
			if (button.isSelected())
				p.fill(120, 0, 0);
			else
				p.fill(200, 0, 0);
			p.rect(x + i * (buttonWidth + buttonGap), y, buttonWidth, buttonHeight);
			i++;
		}
	}

	public boolean overPanel(int px, int py) {
		return (px >= x && px <= (x + w) && py >= y && py <= (y + h));
	}

	public int selectButton(int px, int py) {
		if (py < y || py > y + h)
			return -1;

		int i = 0;
		for (Button button : buttons) {
			if (px >= x + i * (buttonWidth + buttonGap) && px <= x + i * (buttonWidth + buttonGap) + buttonWidth) {
				button.setSelected(true);
				return button.getId();
			}
			i++;
		}
		return -1;
	}

	public void unselectButtons() {
		for (Button button : buttons)
			button.setSelected(false);
	}

	public int getPressedButton() {
		for (Button button : buttons) {
			if (button.isSelected()) {
				button.setSelected(false);
				return button.getId();
			}
		}
		return -1;
	}
}

