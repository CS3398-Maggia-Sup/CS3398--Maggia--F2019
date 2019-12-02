package Options;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Engine.Board;

public class Options {

	boolean timer;
	int[] time = new int[Board.teamNum];
	int[] timeInc = new int[Board.teamNum];
	boolean highlight;
	boolean online;
	boolean cpu;
	ObjectOutputStream output;
	ObjectInputStream input;
	int turn;
	String mode;
	String[][] board = new String[8][8];
	String promote;

	public Options() {

		defaultSet();
	}
	public Options(String preset) {

		if(preset.equals("bullet"))
			bulletSet();
		else if(preset.equals("wall"))
			wallSet();
		else
			defaultSet();
	}

	public boolean getTimer() { return timer; }
	public void setTimer(boolean t) { timer = t; }

	public int[] getTime() { return time; }
	public void setTime(int[] t) { time = t; }

	public int[] getTimeInc() { return timeInc; }
	public void setTimeInc(int[] i) { timeInc = i; }

	public boolean getHighlight() { return highlight; }
	public void setHighlight(boolean h) { highlight = h; }

	public boolean getOnline() { return online; }
	public void setOnline(boolean o) { online = o; }

	public String getMode() { return mode; }
	public void setMode(String m) { mode = m; }

	public String[][] getBoard() { return board; }
	public void setBoard(String[][] b) { board = b; }

	public String getPromote() { return promote; }
	public void setPromote(String p) { promote = p; }

	public ObjectOutputStream getOutput() { return output; }
	public void setOutput(ObjectOutputStream o) { output = o; }

	public ObjectInputStream getInput() { return input; }
	public void setInput(ObjectInputStream i) { input = i; }

	public int getTurn() { return turn; }
	public void setTurn(int t) { turn = t; }

	public boolean getCPU() { return cpu; }
	public void setCPU(boolean c) { cpu = c; }

	public void defaultSet() {

		timer = false;
		highlight = true;
		online = false;
		cpu = false;
		mode = "STANDARD";
		board = BoardTypes.standardChessBoard();
		promote = "kbrq";
	}

	public void bulletSet() {

		defaultSet();
		setMode("BULLET");

		timer = true;

		for(int i = 0; i < Board.teamNum; i++) {
			time[i] = 120;
			timeInc[i] = 1;
		}
	}

	public void wallSet() {

		defaultSet();
		setMode("WALL");

		String wallChessBoard[][] = {
				{"br","bk","bb","bq","bK","bb","bk","br"},
				{"bp","bp","bp","bp","bp","bp","bp","bp"},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"wW","  ","wW","  ","wW","  ","wW","  "},
				{"  ","bW","  ","bW","  ","bW","  ","bW"},
				{"  ","  ","  ","  ","  ","  ","  ","  "},
				{"wp","wp","wp","wp","wp","wp","wp","wp"},
				{"wr","wk","wb","wq","wK","wb","wk","wr"}};

		board = wallChessBoard;
	}
}