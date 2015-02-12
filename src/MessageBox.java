// MessageBox.java
// Last Change: 2012/09/12 19:28.

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

class MessageBox
	extends JDialog
	implements ActionListener
{
	private static final Color COLOR_BACKGROUND = Color.WHITE;
	
	private JLabel text;
	
	public static void show(String message, String title, JFrame owner){
		MessageBox d = new MessageBox(owner, title);
		d.showDialog(message);
	}
	
    //コンストラクタ
	private MessageBox(){
		super((JFrame)null);
	}
	
    public MessageBox(JFrame owner, String title){
		super(owner, title);
		
		this.setGUIParts();
		this.setEscCloseEvent();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // X で閉じる
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
		this.setLayout(new GridLayout(2, 1));
		
		// パネルを生成
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		
		// パネルの背景を設定
		panel1.setBackground(COLOR_BACKGROUND);
		panel2.setBackground(COLOR_BACKGROUND);
		
		// ラベルを生成
		this.text = new JLabel();
		
		// ボタンを生成
		JButton button1 = new JButton("OK");
		button1.addActionListener(this);
		
		// パネルに追加
		panel1.add(text); 
		panel2.add(button1);
		
		// パネルを追加
		this.getContentPane().add(panel1);
		this.getContentPane().add(panel2);
		
		// OK ボタンをデフォルトに設定
		this.getRootPane().setDefaultButton(button1);
    }
    
    // ダイアログを表示する
    void showDialog(String message){
		this.text.setText(message);
		this.pack();
		this.setLocationRelativeTo(this.getOwner()); // 親要素に位置を合わせる
		this.setVisible(true);
    }
    
    //画面を閉じる
    public void actionPerformed(ActionEvent a){
		this.setVisible(false);
    }
}

// vim: se noet ts=4 sw=4 sts=0 ft=java :
