package robot.ascii.impl;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

public interface Drawable
{
	public static final boolean START_PAUSED=false;
	
	public  int hScale=4;
	public  int vScale=5;


	public char OverflowDigit='+';
	public  char ArmCHar='9';
	public  char BarChar=' ';
	public  char BlockChar=' ';
	
	// default thickness before scaling
	
	public int armOneWidth=1;
	public int ArmTwoWidth=1;
	public int ArmThreeWidth=1;
	public int BarWidth = 1;
	public int BlockWidth = 1;
	public int blockCount =0;
	
	public  TextColor ArmColourFG = TextColor.ANSI.WHITE;
	public  TextColor ArmColourBG = TextColor.ANSI.WHITE;
	public  TextColor BarColourFG = TextColor.ANSI.GREEN;
	public  TextColor BarColourBG = TextColor.ANSI.GREEN;
	public  TextColor.ANSI[] BlockColoursFG = new TextColor.ANSI[]
	{ TextColor.ANSI.YELLOW, TextColor.ANSI.RED, TextColor.ANSI.BLUE };
	public  TextColor.ANSI[] BlockColoursBG = new TextColor.ANSI[]
	{ TextColor.ANSI.YELLOW, TextColor.ANSI.RED, TextColor.ANSI.BLUE };

	

	
	
	
	public abstract void draw(SwingTerminalFrame terminalFrame);
}
