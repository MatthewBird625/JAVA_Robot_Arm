package robot.ascii.impl;

import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

public class Bar implements Drawable
{
	private int yPos;
	private int barHeight;
	private int barWidth;
	

	public Bar(int height, int width) {
		this.barWidth = width-1;
		this.barHeight = height;
		
	}
	
	@Override
	public void draw(SwingTerminalFrame terminalFrame)
	{
		
		yPos = terminalFrame.getTerminalSize().getRows() - 1;

		terminalFrame.setForegroundColor(Drawable.BarColourFG);
		terminalFrame.setBackgroundColor(Drawable.BarColourBG);

		for (int barSize = 0; barSize < barHeight; barSize++) {
			// applying V and H scaling by looping
			for (int rowsScaling = 0; rowsScaling < Drawable.vScale; rowsScaling++) {
				for (int colsScaling = 0; colsScaling < Drawable.hScale; colsScaling++) {
					terminalFrame.setCursorPosition(barWidth * Drawable.hScale + colsScaling, yPos - rowsScaling);
					terminalFrame.putCharacter(Drawable.BarChar);
				}
			}
			yPos -= Drawable.vScale;
		}
	}
	
	//these were used during testing/debugging. - 
	
	public int getHeight() {
		
		return this.barHeight;
	}
	
	public int getWidth() {
		
		return this.barWidth;
	}

		
	}

