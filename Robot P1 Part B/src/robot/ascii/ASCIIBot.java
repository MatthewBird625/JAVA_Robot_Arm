package robot.ascii;

import control.RobotControl;
import robot.Robot;
import robot.ascii.impl.Arm;
import robot.ascii.impl.Bar;
import robot.ascii.impl.Block;
import robot.ascii.impl.Drawable;
import robot.impl.RobotImpl;
import robot.impl.RobotInitException;

// ASCIIBot template code Programming 1 s2, 2019
// matthew bird s3482450
// designed by Caspar, additional code by Ross
public class ASCIIBot extends AbstractASCIIBot implements Robot

{
	private int heightLeft = 0;
	private int heightRight = 0;
	private Block[] blocks;
	private Arm arm;
	private int blocksA;
	private int blocksB;
	private int[] barHarray;
	private int[] blockHArray;
	private int tarBlock;
	private Bar[] bars;

	
	public static void main(String[] args)
	{
		new RobotControl().control(new ASCIIBot(), null, null);
	}

	// MUST CALL DEFAULT SUPERCLASS CONSTRUCTOR!
	public ASCIIBot()
	{
		super();
	}

	@Override
	public void init(int[] barHeightsArray, int[] blockHeightArray, int height, int width, int depth)
	{
		
		try
		{
			RobotImpl.validateInitParams(this, barHeightsArray, blockHeightArray, height, width, depth);
		}
		catch (RobotInitException e)
		{
			System.err.println(e.getMessage());
			System.exit(0);
		}

		// write init code here to place bars, blocks and initial arm position
		// suggest writing a separate method e.g. initImpl()

		// clears previous frame/screen
		terminalFrame.clearScreen();
		// demo drawing method (for example purposes only)
		
		//demoDraw();
		initImpl(barHeightsArray, blockHeightArray, height, width, depth);
		
		
		
		
		
		// call this to delay/animate and write out contents after draw 
		// (i.e. after each robot move)
		delayAnimation();
	}

	// simple demo draw method
	// write init code here to place bars, blocks and initial arm position
			// suggest writing a separate method e.g. initImpl()

	private void initImpl(int[] barHeights, int[] blockHeights, int armHeight, int armWidth, int armDepth) {
		this.barHarray = barHeights;
		this.blockHArray = blockHeights;
		this.tarBlock = blockHeights.length -1;

		// Initiate arrays
		bars = new Bar[this.barHarray.length];
		blocks = new Block[this.blockHArray.length];

		int block = terminalFrame.getTerminalSize().getColumns() / Drawable.hScale - 1;
		

		// Initiate bar objects
		for (int i = 0; i < this.barHarray.length; i++) {
			bars[i] = new Bar(this.barHarray[i], i +3);
			//System.out.println(bars[i].getHeight() + bars[i].getWidth());
		}

		// Initiate block array objects and sort
		if (blockHeights.length % 2 == 0) {
			blocksA= block +8;
			blocksB = block -1;	
		}
		else { 
			blocksA= block -1;
			blocksB = block +8;	
			
		}
		
		for (int i = 0; i < this.blockHArray.length; i++) {
			if (i % 2 == 0) {

				blocks[i] = new Block(this.blockHArray[i], blocksA, heightLeft);
				heightLeft += this.blockHArray[i];
			}
			else {
				blocks[i] = new Block(this.blockHArray[i], blocksB, heightRight);
				heightRight += this.blockHArray[i];
			
			}
		}
		
		

		drawBlocks();

		arm = new Arm(armHeight, armWidth, armDepth);
		arm.draw(terminalFrame);
	}

	private void drawBlocks() {
		// Draw bars
		for (int blockIndex = 0; blockIndex < barHarray.length; blockIndex++) {
			bars[blockIndex].draw(terminalFrame);
		}

		// Draw blocks
		for (int blockIndex = 0; blockIndex < blockHArray.length; blockIndex++) {
			blocks[blockIndex].draw(terminalFrame);
			
		}
	}

// implement methods to draw robot environment using your implementation of Arm.draw(), Bar.draw() etc.
	@Override
	public void pick() {
		terminalFrame.clearScreen();
		if (tarBlock >= 0)
			blocks[tarBlock].movementControl();
		arm.draw(terminalFrame);
		drawBlocks();
		delayAnimation();
		System.out.println("pick()");
	
	}

	@Override
	public void drop() {
		terminalFrame.clearScreen();
		arm.drop();
		blocks[tarBlock].Stop();
		if (tarBlock > 0)
			tarBlock--;
		arm.draw(terminalFrame);
		drawBlocks();
		delayAnimation();
		System.out.println("drop()");
		
	}

	@Override
	public void up() {
		terminalFrame.clearScreen();
		arm.up();
		blocks[tarBlock].up();
		arm.draw(terminalFrame);
		
		drawBlocks();
		delayAnimation();
		System.out.println("up()");
		
	}

	@Override
	public void down() {
		terminalFrame.clearScreen();
		arm.down();
		blocks[tarBlock].down();
		arm.draw(terminalFrame);
		drawBlocks();
		delayAnimation();
		System.out.println("down()");
		
	}
	@Override
	public void lower() {
		terminalFrame.clearScreen();
		arm.lower();
		blocks[tarBlock].lower();
		arm.draw(terminalFrame);
		drawBlocks();
		delayAnimation();
		System.out.println("lower()");
		
	}

	@Override
	public void raise() {
		terminalFrame.clearScreen();
		arm.raise();
		blocks[tarBlock].raise();
		arm.draw(terminalFrame);
		drawBlocks();
		delayAnimation();
		System.out.println("raise()");
		
	}

	@Override
	public void contract() {
		terminalFrame.clearScreen();
		arm.contract();
		blocks[tarBlock].contract();
		arm.draw(terminalFrame);
		drawBlocks();
		delayAnimation();
		System.out.println("contract()");
	
	}

	@Override
	public void extend() {
		terminalFrame.clearScreen();
		arm.extend();
		blocks[tarBlock].extend();
		arm.draw(terminalFrame);
		drawBlocks();
		delayAnimation();
		System.out.println("extend()");
	}


}

