// Cell.java
// Last Change: 2012/08/03 00:28:40 +0900.

import java.awt.Color;
import javax.swing.*;

class Cell extends JButton {
	private static final Color COLOR_MINE   = Color.RED;        // 爆弾
	private static final Color COLOR_OPENED = Color.WHITE;      // 開かれたセル
	private static final Color COLOR_CLOSED = Color.LIGHT_GRAY; // 閉じているセル
	private static final Color COLOR_FLAG   = Color.YELLOW;     // フラグ
	
	private final int cellX;
	private final int cellY;
	
	// セルの情報
	private int     mineCount   = 0;
	private boolean isMine      = false;
	private boolean isOpened    = false;
	private boolean isFlag      = false;
	
	private Cell(){
		super();
		
		this.cellX = 0;
		this.cellY = 0;
	}
	
	public Cell(int cellX, int cellY){
		super();
		
		this.cellX = cellX;
		this.cellY = cellY;
		
		this.updateGUI();
	}
	
	public void setMineCount(int mineCount){
		this.mineCount = mineCount;
		this.updateGUI();
	}
	
	public int getMineCount(){
		return this.mineCount;
	}
	
	public void setMine(boolean isMine){
		this.isMine = isMine;
		this.updateGUI();
	}
	
	public boolean isMine(){
		return this.isMine;
	}
	
	public void setOpened(boolean isOpened){
		this.isOpened = isOpened;
		this.updateGUI();
	}
	
	public boolean isOpened(){
		return this.isOpened;
	}
	
	public void setFlag(boolean isFlag){
		this.isFlag = isFlag;
		this.updateGUI();
	}
	
	public boolean isFlag(){
		return this.isFlag;
	}
	
	public int getCellX(){
		return this.cellX;
	}
	
	public int getCellY(){
		return this.cellY;
	}
	
	private void updateGUI(){
		
		// 開かれている場合
		if(this.isOpened){
			// 無効にする
			this.setEnabled(false);
			
			// 爆弾な場合
			if(this.isMine){
				this.setBackground(Cell.COLOR_MINE);
				this.setText("*");
			}
			
			// 爆弾で無い場合
			else {
				this.setBackground(Cell.COLOR_OPENED);
				
				// 爆弾の数が 1 以上な場合
				if(this.mineCount > 0){
					this.setText(Integer.toString(this.mineCount));
				}
				
				// 爆弾の数がゼロな場合
				else {
					this.setText(" ");
				}
			}
		}
		
		// 閉じられている場合
		else {
			// 有効にする
			this.setEnabled(true);
			
			// フラグが立っている場合
			if(this.isFlag){
				this.setBackground(Cell.COLOR_FLAG);
				this.setText("F");
			}
			
			// フラグが立っていない場合
			else {
				this.setBackground(Cell.COLOR_CLOSED);
				this.setText(" ");
			}
		}
	}
}

// vim: se noet ts=4 sw=4 sts=0 ft=java :
