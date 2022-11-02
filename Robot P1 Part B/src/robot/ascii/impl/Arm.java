package robot.ascii.impl;

import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import robot.RobotMovement;

public class Arm implements RobotMovement, Drawable
{
	private int armWidth;
	private int armWidthX;
	private int armDepth;
	private int armDepthY;
	private int armHeight;
	private int armHeightY;
	

	
	public Arm(int height, int width, int depth)
	{
		this.armDepth = depth;
		this.armHeight = height;
		this.armWidth = width;

	}


	@Override
	public void draw(SwingTerminalFrame terminalFrame)
	{
		terminalFrame.setForegroundColor(Drawable.ArmColourFG);
	    terminalFrame.setBackgroundColor(Drawable.ArmColourBG);



	    // Draw arm width 
	    armWidthX = Drawable.hScale;
	   	for (int barSizeControl = 0; barSizeControl < armWidth; barSizeControl++)
	    {
	      drawBar(terminalFrame, armWidthX, terminalFrame.getTerminalSize().getRows() - 1 - (armHeight - 1) * Drawable.vScale);
	      armWidthX += Drawable.hScale;
	    }
	   	
	    // Draw arm height 
	    armHeightY = terminalFrame.getTerminalSize().getRows() - 1;
	   	for (int barSize = 0; barSize < armHeight; barSize++)
	    {
	      drawBar(terminalFrame, 0, armHeightY);
	      armHeightY -= Drawable.vScale;
	    }

	    // Draw arm depth 
	    armDepthY = terminalFrame.getTerminalSize().getRows() - 1 - (armHeight - 2) * Drawable.vScale;
	   	for (int barSizeControl = 0; barSizeControl < armDepth; barSizeControl++)
	    {
	      drawBar(terminalFrame, armWidth * Drawable.hScale, armDepthY);
	      armDepthY += Drawable.vScale;
	    }

	}
	private void drawBar(SwingTerminalFrame terminalFrame, int x, int y)
	{
 		for (int rowScaling = 0; rowScaling < Drawable.vScale; rowScaling++)
    {
      for (int colScaling = 0; colScaling < Drawable.hScale; colScaling++)
      {
        terminalFrame.setCursorPosition(x + colScaling, y - rowScaling);
        
        terminalFrame.putCharacter(Drawable.BarChar);
        
      }
    }
	}


	@Override
	public void pick()
	{

	}

	@Override
	public void drop()
	{

	}

	@Override
	public void up()
	{
		armHeight++;
	}

	@Override
	public void down()
	{
		armHeight--;
	}

	@Override
	public void contract()
	{
		armWidth--;
	}

	@Override
	public void extend()
	{
		armWidth++;
	}

	@Override
	public void lower()
	{
		armDepth++;
	}

	@Override
	public void raise()
	{
		armDepth--;
	}
}
