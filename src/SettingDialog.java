// SettingDialog.java
// Last Change: 2012/08/03 01:54:50 +0900.

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

class SettingDialog
	extends JDialog
	implements ActionListener
{
	private Mines mines;
	
	private JTextField textCols;
	private JTextField textRows;
	private JTextField textMineCount;
	
	private JButton buttonOK;
	private JButton buttonCancel;
	
    //コンストラクタ
	private SettingDialog(){
		super((JFrame)null);
	}
	
    public SettingDialog(Mines mines){
		super(mines, "Setting");
		
		this.mines = mines;
		
		this.setGUIParts();
		this.setEscCloseEvent();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setModal(true);
    }
	
	// Escape キーで閉じる
	private void setEscCloseEvent(){
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		
		this.getRootPane().registerKeyboardAction(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setVisible(false);
			}
		}, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
    private void setGUIParts(){
		GridLayout layout = new GridLayout(4,2);
		
		this.setLayout(layout);
		
		JLabel labelCols      = new JLabel("Cols");
		JLabel labelRows      = new JLabel("Rows");
		JLabel labelMineCount = new JLabel("Mine Count");
		
		this.textCols      = new JTextField(10);
		this.textRows      = new JTextField(10);
		this.textMineCount = new JTextField(10);
		
		this.buttonCancel = new JButton("Cancel");
		this.buttonOK     = new JButton("OK");
		
		buttonCancel.addActionListener(this);
		buttonOK.addActionListener(this);
		
		this.getContentPane().add(labelCols);
		this.getContentPane().add(this.textCols);
		
		this.getContentPane().add(labelRows);
		this.getContentPane().add(this.textRows);
		
		this.getContentPane().add(labelMineCount);
		this.getContentPane().add(this.textMineCount);
		
		this.getContentPane().add(this.buttonCancel);
		this.getContentPane().add(this.buttonOK);
		
		// Enter キーで閉じる
		this.getRootPane().setDefaultButton(this.buttonOK);
    }
    
    void showDialog(int cols, int rows, int mineCount){
		
		this.textCols.setText(Integer.toString(cols));
		this.textRows.setText(Integer.toString(rows));
		this.textMineCount.setText(Integer.toString(mineCount));
		
		this.pack();                                 // 大きさを調整
		this.setLocationRelativeTo(this.getOwner()); // 位置を調整
		this.setVisible(true);                       // 画面を開く
    }
    
    //画面を閉じる
    public void actionPerformed(ActionEvent a){
		JButton button = (JButton)a.getSource();
		
		if(button == this.buttonOK){
			int cols      = Integer.valueOf(this.textCols.getText());
			int rows      = Integer.valueOf(this.textRows.getText());
			int mineCount = Integer.valueOf(this.textMineCount.getText());
			
			if(this.isValid(cols, rows, mineCount)){
				this.mines.settingOK(cols, rows, mineCount);
				this.setVisible(false);
			}
			
			else {
				MessageBox.show(
						"Mine Count is invalid.",
						"Error",
						(JFrame)this.getOwner()
						);
			}
		}
		
		if(button == this.buttonCancel){
			this.setVisible(false);
		}
    }
	
	// 妥当な値か調べる
	public boolean isValid(int cols, int rows, int mineCount){
		if(cols * rows <= mineCount){
			return false;
		}
		
		if(cols <= 0 || rows <= 0 || mineCount <= 0){
			return false;
		}
		
		return true;
	}
}
