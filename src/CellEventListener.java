// CellEventListener.java
// Last Change: 2012/07/25 20:04:59 +0900.

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CellEventListener extends MouseAdapter {
	private Mines mines;
	
	public CellEventListener(Mines mines){
		this.mines = mines;
	}
	
    public void mousePressed(MouseEvent e){
		Cell cell        = (Cell)e.getSource();
		int  mouseButton = e.getButton();
		
		// 左クリック
		if(mouseButton == MouseEvent.BUTTON1){
			this.mines.cellClicked(cell);
		}
		
		// 右クリック
		else if(mouseButton == MouseEvent.BUTTON3){
			this.mines.cellRightClicked(cell);
		}
	}
}

// vim: se noet ts=4 sw=4 sts=0 ft=java :
