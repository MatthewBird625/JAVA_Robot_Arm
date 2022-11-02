package robot.ascii.impl;

import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

public class Block implements Drawable {

	private boolean move = false;
	private int blockHeight;
	private int blockX;
	private int blockY;
	
	

	public Block(int height, int x, int y) {
		this.blockHeight = height;

		// My code would usually produce the blocks correctly in the 1st and 10th
		// column, but then sometimes would place the
		// same blocks in the 0th(inside the arm) and 9th columns without changing the
		// code in project or blockheights in RobotControl. This ensures that if this occurs
		// that our blocks are moved back over correctly on the x axis,-comment out lines
		// 22-25 if you wish to see this.
		if (x == 0)
			x = x + 1;
		if (x == 9)
			x = x + 1;
		
		//do not comment these out
		this.blockX = x;
		this.blockY = y;
		
		
	}


	@Override
	public void draw(SwingTerminalFrame terminalFrame) {
		int moveY = terminalFrame.getTerminalSize().getRows() - 1 - blockY * Drawable.vScale;

		terminalFrame.setForegroundColor(Drawable.BlockColoursFG[blockHeight - 1]);
		terminalFrame.setBackgroundColor(Drawable.BlockColoursBG[blockHeight - 1]);
		
		
		
		

		for (int blockSize = 0; blockSize < blockHeight; blockSize++) {
			// applying V and H scaling by looping
			for (int rowScaling = 0; rowScaling < Drawable.vScale; rowScaling++) {
				for (int colScaling = 0; colScaling < Drawable.hScale; colScaling++) {
					terminalFrame.setCursorPosition(blockX * Drawable.hScale + colScaling, moveY - rowScaling);
					terminalFrame.putCharacter(Drawable.BarChar);
				}
			}
			moveY -= Drawable.vScale;
		}

	}

	// movement
	public void movementControl() {
		this.move = true;
	}

	public void Stop() {
		this.move = false;
	}

	public void up() {
		if (move)
			blockY++;
	}

	public void down() {
		if (move)
			blockY--;
	}

	public void contract() {
		if (move)
			blockX--;
	}

	public void extend() {
		if (move)
			blockX++;
	}

	public void lower() {
		if (move)
			blockY--;
	}

	public void raise() {
		if (move)
			blockY++;
	}
	
	//these were used during testing/debugging. - 
	
	public int getHeight() {

		return this.blockHeight;
	}

	public int getX() {

		return this.blockX;
	}

	public int getY() {

		return this.blockY;
	}
}
