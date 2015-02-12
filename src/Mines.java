// Mines.java
// Last Change: 2012/08/03 02:09:58 +0900.

import java.util.Random;
import java.util.Arrays;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Mines
	extends JFrame
	implements ActionListener
{
	private static final String TITLE       = "Mines";
	private static final int    CELL_SIZE   = 50;
	private static final Color  COLOR_LABEL = Color.WHITE;
	
	private int     cellRows       = 9;
	private int     cellCols       = 9;
	private int     mineCount      = 10;
	private int     flagCount      = 0;
	private boolean isGameStarted  = false;
	private boolean isGameFinished = false;
	
	private JButton buttonNewGame;
	private JButton buttonSetting;
	private JPanel  panelButton;
	private JPanel  panelLabel;
	private JLabel  labelStatus;
	private JLabel  labelTime;
	
	private Cell[][]    cells;
	private boolean[][] visited;
	
	private Timer timer;
	private int   elapsedTime;
	
	public static void main(String[] args){
		Mines mines = new Mines();
		mines.validate();
	}
	
	public Mines(){
		// Look & Feel を設定する
		this.setLookAndFeel();
		
		// タイトルを設定
		this.setTitle(Mines.TITLE);
		
		// デフォルトの閉じる動作を定義
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// GUI を更新
		this.updateGUI();
		
		// タイマを作成
		this.timer = new Timer(1000, this);
		
		// ウィンドウを表示させる
		this.setVisible(true);
	}
	
	// Look & Feel を設定する
	private void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); // Metal
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void updateGUI(){
		// 画面サイズを計算
		int width  = this.cellCols       * CELL_SIZE;
		int height = (this.cellRows + 2) * CELL_SIZE;
		
		// 画面サイズ設定
		this.setSize(width, height);
		
		//  レイアウトを設定
		this.getContentPane().setLayout(new GridBagLayout());
		
		// 既にあるコンポーネントを削除する
		this.getContentPane().removeAll();
		
		// 上部パネルの追加
		this.panelButton = new JPanel();
		this.panelButton.setLayout(new GridLayout(1, 2));
		
		// ボタンを生成
		this.buttonNewGame = new JButton("New Game");
		this.buttonNewGame.addMouseListener(new ButtonEventListener(this));
		
		this.buttonSetting = new JButton("Setting");
		this.buttonSetting.addMouseListener(new ButtonEventListener(this));
		
		// ボタンを追加
		this.panelButton.add(this.buttonNewGame);
		this.panelButton.add(this.buttonSetting);
		
		// パネルを追加
		this.addComponent(this.panelButton, 0, 0, GridBagConstraints.REMAINDER, 1);
		
		// セル数に応じて配列を確保する
		this.cells = new Cell[this.cellCols][this.cellRows];
		
		// セルを新規生成
		for(int i = 0; i < this.cellRows; i++){
			for(int j = 0; j < this.cellCols; j++){
				Cell cell = new Cell(j, i);
				
				this.cells[j][i] = cell;
				
				cell.addMouseListener(new CellEventListener(this));
				this.addComponent(cell, j, i + 1, 1, 1);
			}
		}
		
		// ラベルを追加
		this.panelLabel = new JPanel();
		this.panelLabel.setBackground(Mines.COLOR_LABEL);
		
		this.labelStatus = new JLabel();
		this.labelStatus.setHorizontalAlignment(SwingConstants.CENTER);
		
		this.labelTime = new JLabel();
		this.labelTime.setHorizontalAlignment(SwingConstants.CENTER);
		
		panelLabel.setLayout(new GridLayout(1, 2));
		panelLabel.add(this.labelStatus);
		panelLabel.add(this.labelTime);
		
		this.addComponent(panelLabel, 0, this.cellRows + 1, GridBagConstraints.REMAINDER, 1);
		
		this.updateStatus();
		this.updateElapsedTime();
		this.getContentPane().validate();
	}
	
	private void addComponent(Component c, int x, int y, int w, int h) {
		GridBagConstraints gbc = new GridBagConstraints();
		GridBagLayout      mgr = (GridBagLayout)this.getContentPane().getLayout();
		
		gbc.fill       = GridBagConstraints.BOTH;
		gbc.gridx      = x;
		gbc.gridy      = y;
		gbc.gridwidth  = w;
		gbc.gridheight = h;
		gbc.weightx    = 1;
		gbc.weighty    = 1;
		
		mgr.setConstraints(c, gbc);
		this.getContentPane().add(c);
	}
	
	// ステータスを更新する
	private void updateStatus(){
		this.labelStatus.setText("Flag / Mine : " + this.flagCount + " / " + this.mineCount);
		this.getContentPane().validate();
	}
	
	// ----- ゲームの処理 ----------------------------------------
	
	private void gameNew(){
		layoutClear(); // 盤面をクリア　
		
		this.flagCount = 0;
		this.updateStatus();
		
		this.timerStop();
		this.timerClear();
		this.updateElapsedTime();
		
		this.isGameStarted  = false;
		this.isGameFinished = false;
	}
	
	private void gameStart(int x, int y){
		// 爆弾を配置
		layoutClear();
		layoutMines(x, y);
		
		this.isGameStarted  = true;
		this.isGameFinished = false;
		this.flagCount      = 0;
		
		// タイマーを開始
		this.timerStart();
	}
	
	private void gameOver(){
		
		this.timerStop();
		
		// すべての爆弾マスを開く
		for(int i = 0; i < this.cellRows; ++i){
			for(int j = 0; j < this.cellCols; ++j){
				if(this.cells[j][i].isMine()){
					this.cells[j][i].setOpened(true);
				}
			}
		}
		
		this.isGameFinished = true;
		this.isGameStarted  = false;
		
		MessageBox.show("Game Over!!", "Notice", this);
	}
	
	private void gameClear(){
		
		this.timerStop();
		
		this.isGameFinished = true;
		MessageBox.show("Game Clear!!", "Notice", this);
	}
	
	private boolean isGameClear(){
		int count = 0;
		
		// 爆弾の位置以外が全て開かれている場合
		for(int i = 0; i < this.cellRows; ++i){
			for(int j = 0; j < this.cellCols; ++j){
				if(!this.cells[j][i].isMine() && this.cells[j][i].isOpened()){
					++count;
				}
			}
		}
		
		if(count + this.mineCount == this.cellRows * this.cellCols){
			return true;
		}
		
		return false;
	}
	private void openCell(int x, int y){
		
		// ゲームが終了している
		if(this.isGameFinished){
			return;
		}
		
		// 開放済みのセルの場合
		if(this.cells[x][y].isOpened()){
			return;
		}
		
		// フラグが立っている場合
		if(this.cells[x][y].isFlag()){
			return;
		}
		
		// クリック地点が爆弾の場合の処理
		if(this.cells[x][y].isMine()){
			this.gameOver();
			return;
		}
		
		// フラグの確保
		this.visited = new boolean[this.cellCols][this.cellRows];
		
		// フラグの初期化
		for(int i = 0; i < this.cellCols; ++i){
			Arrays.fill(this.visited[i], false);
		}
		
		// 再帰的にセルを開く
		this.openCell(x, y, true);
		
		// フラグの解放
		this.visited = null;
		
		// ゲームクリアの処理
		if(this.isGameClear()){
			this.gameClear();
		}
		
		// 画面変更を反映
		this.getContentPane().validate();
	}
	
	private void openCell(int x, int y, boolean first){
		if(!this.isValidCoord(x, y)) return;
		if(this.visited[x][y])       return;
		
		this.visited[x][y] = true;
		
		if(!this.cells[x][y].isMine()){
			this.cells[x][y].setOpened(true);
		}
		
		if(this.cells[x][y].getMineCount() == 0){
			for(int i = -1; i <= 1; ++i){
				for(int j = -1; j <= 1; ++j){
					int newX = x + i;
					int newY = y + j;
					
					openCell(newX, newY, false);
				}
			}
		}
	}
	
	// 爆弾を配置する
	private void layoutMines(int clickedX, int clickedY){
		Random rand  = new Random();
		int    count = 0;
		
		// 指定した個数の爆弾を配置する
		while(count < this.mineCount){
			int x = rand.nextInt(this.cellCols);
			int y = rand.nextInt(this.cellRows);
			
			// クリックした座標は除く
			if(clickedX == x && clickedY == y) continue; 
			
			Cell cell = this.cells[x][y];
			
			// 既に爆弾が配置されている場合は除く
			if(cell.isMine()) continue;
			
			// 爆弾を配置
			cell.setMine(true);
			++count;
		}
		
		// 各セルの周囲の爆弾の個数を計算する
		for(int i = 0; i < this.cellRows; ++i){
			for(int j = 0; j < this.cellCols; ++j){
				final int[] d = new int[] { 1, -1, 0 };
				
				for(int k = 0; k < d.length; ++k){
					for(int l = 0; l < d.length; ++l){
						int x = j + d[k];
						int y = i + d[l];
						
						if(!this.isValidCoord(x, y)) continue;
						
						// 座標が存在する & 爆弾のあるセル
						if(cells[x][y].isMine()){
							cells[j][i].setMineCount(cells[j][i].getMineCount() + 1);
						}
					}
				}
			}
		}
	}
	
	// 盤面をクリアする
	private void layoutClear(){
		
		// 盤面をリセットする
		for(int i = 0; i < this.cellRows; ++i){
			for(int j = 0; j < this.cellCols; ++j){
				Cell cell = this.cells[j][i];
				
				cell.setMine(false);
				cell.setFlag(false);
				cell.setOpened(false);
				cell.setMineCount(0);
			}
		}
	}
	
	// フラグの反転処理
	private void toggleFlag(Cell cell){
		assert cell != null;
		
		// セルが開かれて無い場合
		if(!cell.isOpened()){
			// フラグが立っている場合
			if(cell.isFlag()){
				// フラグを消す
				cell.setFlag(false);
				--this.flagCount;
				
				this.updateStatus();
			}
			
			// フラグが立っていない場合
			else {
				// フラグを立てる
				cell.setFlag(true);
				++this.flagCount;
				
				this.updateStatus();
			}
		}
	}
	
	// 正しい座標かどうか
	private boolean isValidCoord(int x, int y){
		return x >= 0 && x < this.cellCols && y >= 0 && y < this.cellRows;
	}
	
	private void updateElapsedTime(){
		int min = this.elapsedTime / 60;
		int sec = this.elapsedTime % 60;
		
		if(min > 0){
			this.labelTime.setText(
					"Time: " + Integer.toString(min) + " min " +
					Integer.toString(sec) + " sec"
					);
		}
		
		else {
			this.labelTime.setText(
					"Time: " + Integer.toString(sec) + " sec"
					);
		}
	}
	
	private void timerStart(){
		this.elapsedTime = 0;
		this.timer.start();
	}
	
	private void timerStop(){
		this.timer.stop();
	}
	
	private void timerClear(){
		this.elapsedTime = 0;
	}
	
	// ----- イベントの処理 -------------------------------------
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == this.timer){
			++this.elapsedTime;
			this.updateElapsedTime(); // 経過時間を更新
		}
	}
	
	// セルがクリックされたときの処理
	public void cellClicked(Cell cell){
		assert cell != null;
		
		int x = cell.getCellX();
		int y = cell.getCellY();
		
		// ゲームが終了状態でない場合
		if(!this.isGameFinished){
			// ゲームが開始されていない場合
			if(!this.isGameStarted){
				this.gameStart(x, y);
			}
			
			// セルを開く
			this.openCell(x, y);
		}
	}
	
	public void cellRightClicked(Cell cell){
		assert cell != null;
		
		//  ゲームが開始されている場合
		if(this.isGameStarted && !this.isGameFinished){
			this.toggleFlag(cell);
		}
	}
	
	public void buttonClicked(JButton button){
		assert button != null;
		
		if(button == this.buttonNewGame){
			this.gameNew();
		}
		
		else if(button == this.buttonSetting){
			SettingDialog dialog = new SettingDialog(this);
			dialog.showDialog(this.cellCols, this.cellRows, this.mineCount);
		}
	}
	
	public void buttonRightClicked(JButton button){}
	
	public void settingOK(int cols, int rows, int mineCount){
		this.cellCols  = cols;
		this.cellRows  = rows;
		this.mineCount = mineCount;
		
		this.updateGUI();
		this.gameNew();
	}
}

// vim: se noet ts=4 sw=4 sts=0 ft=java :
