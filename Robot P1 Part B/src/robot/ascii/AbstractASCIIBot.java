package robot.ascii;

import javax.swing.JFrame;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import control.Control;
import robot.Robot;
import robot.ascii.impl.Drawable;

// ASCIIBot designed by Caspar, additional code by Ross
// DO NOT CHANGE THIS CLASS .. IT PROVIDES BASIC SUPPORT TO INITIALISE AND
// PROVIDES KEYBOARD FUNCTIONALITY FOR MANUAL MOVEMENT AND SPEED CONTROL
public abstract class AbstractASCIIBot implements Robot
{
	// these two instance variables are given in startup code
	protected SwingTerminalFrame terminalFrame;
	private int speed = (Drawable.START_PAUSED ? 0 : 5);

	// MUST CALL FROM SUBCLASS .. DO NOT CHANGE THIS CONSTRUCTOR!
	public AbstractASCIIBot()
	{
		// create the terminal and set size
		DefaultTerminalFactory factory = new DefaultTerminalFactory();
		factory.setInitialTerminalSize(new TerminalSize((Control.MAX_WIDTH + 1),
				Control.MAX_HEIGHT));
		terminalFrame = factory.createSwingTerminal();
		// required by Lanterna framework to initialise
		terminalFrame.enterPrivateMode();
		terminalFrame.setCursorVisible(false);
		terminalFrame.setLocation(200, 400);
		terminalFrame.setVisible(true);
		// set close operation so program with exit if "X" clicked
		terminalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void speedUp(int amount)
	{
		if (speed == -1)
			return;

		speed += amount;
		speed = speed <= 10 ? speed : 10;
	}

	@Override
	public void slowDown(int amount)
	{
		if (speed == -1)
			return;

		speed -= amount;
		speed = speed >= 0 ? speed : 0;
	}

	// you can call this method between each complete screen draw
	protected void delayAnimation()
	{
		final int[] delays = { 6, 144, 89, 55, 34, 21, 13, 8, 5, 3, 2 };
		int delayCounter = (speed == -1) ? 16 : delays[speed];

		terminalFrame.flush();

		do
		{
			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
			}
		}
		while (--delayCounter > 0 || speed == 0);
	}
}
