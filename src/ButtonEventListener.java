// ButtonEventListener.java
// Last Change: 2012/07/31 13:32:52 +0900.

import java.awt.event.*;
import javax.swing.*;

public class ButtonEventListener extends MouseAdapter {
	private Mines mines;
	
	public ButtonEventListener(Mines mines){
		this.mines = mines;
	}
	
    public void mousePressed(MouseEvent e){
		JButton button      = (JButton)e.getSource();
		int     mouseButton = e.getButton();
		
		// 左クリック
		if(mouseButton == MouseEvent.BUTTON1){
			this.mines.buttonClicked(button);
		}
		
		// 右クリック
		else if(mouseButton == MouseEvent.BUTTON3){
			this.mines.buttonRightClicked(button);
		}
	}
}

// vim: se noet ts=4 sw=4 sts=0 ft=java :
