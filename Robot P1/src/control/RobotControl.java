package control;

import robot.Robot;
//correct folder

//assignment 2:A  Matthew Bird

// s3482450

//Adapted by Caspar and Ross from original Robot code written by Dr Charles Thevathayan
public class RobotControl implements Control {

	private int height = Control.INITIAL_HEIGHT;
	private int width = Control.INITIAL_WIDTH;
	private int depth = Control.INITIAL_DEPTH;

	private int[] barHeights;
	private int[] blockHeights;

	// Extra arrays needed for algorithm sort- first array is for filling blank
	// spaces with zero's
	private int[] zeroBarHeights;

	private int[] SRC_blockHeightsA = new int[Control.MAX_SRC_STACK_HEIGHT];
	private int[] SRC_blockHeightsB = new int[Control.MAX_SRC_STACK_HEIGHT];

	// Used to track direction (left to right=true) (right to left = false) (lTor=
	// Left to Right)
	private boolean lToR = true;

	// must be 1!! 10 will not produce error messages but will alter solution. all
	// other numbers will produce errors.
	private int next = 1;

	// These variables are to store the current source height in private variables
	private int heightA = 0;
	private int heightB = 0;
	private int heightDef = 0;

	private int maximumBar = 0;
	private int clearance = 0;
	private int blockHeight = 0;
	private int increment = 1; // for increment/de-increment. used variable so I can control from here, rather
								// than have to find every increment for manual adjustment.
	private int pass; // this variable is for storing calculations to be later passed into methods.
						// i use this as it made writing the program easier and easier to debug by
						// passing
						// magic numbers during testing/writing, rather than having to delete/move
						// Calculations or lines of code around.

	// Clearance when block is picked
	private int bClearance = 0;
	private int NextBarTarget = 1;

	private Robot robot;

	// called by RobotImpl
	@Override
	public void control(Robot robot, int barHeightsDefault[], int blockHeightsDefault[]) {
		this.robot = robot;

		this.barHeights = new int[] { 1,1,1,1};
		this.blockHeights = new int[] { 1,2,2,1,1,1};

		this.zeroBarHeights = new int[Control.MAX_BARS];

		// initialise the robot
		robot.init(this.barHeights, this.blockHeights, height, width, depth);

		// separates the block arrays and calculates their height.
		organiseBlocks();

		LocateNewHeights();

		// LOOP/ algorithm

		// finds next blocks and determines clearance
		// moves arm over clearance and positions above next block
		// drops depth and grabs block
		// find tallest bar
		// determine clearance
		// extend or contract above next block
		// change height to be above clearance
		// lowers arm
		// drops block
		// change to next block.

		do {

			// first we find the next target
			NextBarTarget = findNextBar(NextBarTarget);

			// determine tallest
			maximumBar = maximumBar(Control.LAST_BAR_COLUMN, zeroBarHeights);

			// determine clearance
			clearance = findClearance(maximumBar, heightA, heightB);

			// determine clearance
			movetoClearanceHeight();

			// move the arm horizontally until it is positioned over the "barTarget"
			changeWidth(next);

			// adjusts depth to position directly above block
			toBlockTopHeight();

			// pick up the block using provided method
			robot.pick();

			// removes the block value from the source height
			deleteBlock();

			// find the tallest bar.
			maximumBar = maximumBar(NextBarTarget, zeroBarHeights);

			changeDepth(Control.INITIAL_DEPTH);

			// determines clearance

			if (next == 1)
				clearance = findLeftClearance(maximumBar, heightA);
			else

				clearance = findClearance(maximumBar, heightA, heightB);

			// finds clearance when considering the current block that has been picked up.
			findClearance();

			// extends or contracts to position above the next block
			changeWidth(NextBarTarget);

			clearance = findLeftClearance(maximumBar, heightA);

			// checks the clearance and changes the height
			checkClearance();

			// lowers the arm into position to drop the blcok.
			pass = height - zeroBarHeights[NextBarTarget - (increment * 2)] - increment - blockHeight;

			changeDepth(pass);

			robot.drop();

			// Adds the current block value to our extra height array
			zeroBarHeights[NextBarTarget - 2] += blockHeight;

			blockHeight = 0;

			// reset depth
			changeDepth(Control.INITIAL_DEPTH);

			// Change to the next block we wish to move.
			next = changeSourceTarget(next);

		} while (heightB > 1 || heightA > 1);

	}

	// METHODS
	

	private void LocateNewHeights() {
		int localControl = 0;
		while (localControl < barHeights.length) {
			zeroBarHeights[localControl] = barHeights[localControl];
			localControl++;
		}
	}

	private void checkClearance() {

		if (clearance <= zeroBarHeights[NextBarTarget - 2] + blockHeight)
			changeHeight(zeroBarHeights[NextBarTarget - 2] + blockHeight + increment);
		else
			changeHeight(clearance);
	}

	// finds the clearance

	private int findClearance(int maximumBar, int sourceHeightA, int sourceHeightB) {
		return increment + depth + Math.max(sourceHeightA, Math.max(sourceHeightB, maximumBar));
	}

	// finds highest bar
	private int maximumBar(int nextB, int newBarHeights[]) {
		int maxReturn = 0;

		int localControl = 0;
		while (localControl <= nextB - (increment * 2)) {
			if (newBarHeights[localControl] > maxReturn)
				maxReturn = newBarHeights[localControl];
			localControl++;
		}
		return maxReturn;
	}

	private int findLeftClearance(int maximumBar, int sourceHeight) {
		return Math.max(sourceHeight, maximumBar) + increment;
	}

	private int findNextBar(int barNext) {
		int next = 0;
		// Going L to R and we are not at rightmost bar
		if (lToR == true && barNext < Control.LAST_BAR_COLUMN) {
			next = ++barNext;
		}
		// Going L to R and we are at rightmost bar
		else if (lToR == true && barNext == Control.LAST_BAR_COLUMN) {
			this.lToR = false;
			next = barNext;
		}
		// Going R to L and we are not at leftmost bar
		else if (lToR == false && barNext > Control.FIRST_BAR_COLUMN)
			next = --barNext;
		// Going R to L and we are at leftmost bar
		else if (lToR == false && barNext == Control.FIRST_BAR_COLUMN) {
			this.lToR = true;
			next = barNext;
		}
		return next;
	}

	private void deleteBlock() {
		int blockPosition = 0;
		int localControl = 0;
		if (next == 1) {
			while (localControl < SRC_blockHeightsA.length) {
				if (SRC_blockHeightsA[localControl] > 0) {
					blockHeight = SRC_blockHeightsA[localControl];
					blockPosition = localControl;
				}
				localControl++;
			}
			heightA -= blockHeight;
			SRC_blockHeightsA[blockPosition] = 0;
		} else {
			while (localControl < SRC_blockHeightsB.length) {
				if (SRC_blockHeightsB[localControl] > 0) {
					blockHeight = SRC_blockHeightsB[localControl];
					blockPosition = localControl;
				}
				localControl++;
			}
			SRC_blockHeightsB[blockPosition] = 0;
			heightB -= blockHeight;

		}
	}

	private void movetoClearanceHeight() {
		int leftClearance = 0;
		int localControl = 0;
		if (next == 1) {
			while (localControl <= Math.max(0, width - (increment * 2))) {
				if (leftClearance <= zeroBarHeights[localControl]) {
					leftClearance = Math.max(heightA, zeroBarHeights[localControl]) + increment;
				}
				localControl++;
			}
			changeHeight(leftClearance);
		} else
			changeHeight(clearance);
	}

	private void toBlockTopHeight() {
		if (next == 1)
			heightDef = heightA;
		else
			heightDef = heightB;

		changeDepth(height - heightDef - increment);
	}

	private int changeSourceTarget(int sourceTarget) {
		if (sourceTarget != 1)
			return 1;
		else
			return 10;
	}

	private void findClearance()

	{
		bClearance = 0;

		if (next == 10) {
			int localControl = zeroBarHeights.length - increment;
			while (localControl >= NextBarTarget - (increment * 2)) {
				if (zeroBarHeights[localControl] + blockHeight + increment > bClearance
						&& zeroBarHeights[localControl] + blockHeight + increment > heightB + blockHeight + increment)

					bClearance = zeroBarHeights[localControl] + blockHeight + increment;

				else if (heightB + blockHeight + increment > bClearance)

					bClearance = heightB + blockHeight + increment;
				localControl--;
			}

		}
		if (clearance + blockHeight <= Control.MAX_HEIGHT) {
			if (bClearance < clearance)
				changeHeight(clearance + blockHeight);
			else
				changeHeight(bClearance);
		}
	}

	private void organiseBlocks() {

		int srcIndex = 0;
		int localControl = 0;
		while (localControl < blockHeights.length) {
			this.SRC_blockHeightsA[srcIndex] = blockHeights[localControl];
			srcIndex++;
			localControl += 2;
		}
		srcIndex = 0;
		localControl = 1;
		while (localControl < blockHeights.length) {
			SRC_blockHeightsB[srcIndex] = blockHeights[localControl];
			srcIndex++;
			localControl += 2;
		}
		localControl = 0;

		while (localControl < SRC_blockHeightsA.length) {
			heightA += SRC_blockHeightsA[localControl];
			localControl++;
		}
		localControl = 0;
		while (localControl < SRC_blockHeightsB.length) {
			heightB += SRC_blockHeightsB[localControl];
			localControl++;
		}
	}
	private void changeWidth(int width) {

		for (int control = this.width; control < width; control++) {
			robot.extend();
			this.width++;
		}

		for (int control = this.width; control > width; control--) {
			robot.contract();
			this.width--;
		}
	}

	private void changeDepth(int depth) {

		for (int control = this.depth; control < depth; control++) {
			robot.lower();
			this.depth++;
		}
		for (int control = this.depth; control > depth; control--) {
			robot.raise();
			this.depth--;
		}

	}

	private void changeHeight(int height) {

		for (int control = this.height; control < height; control++) {
			robot.up();
			this.height++;
		}
		for (int control = this.height; control > height; control--) {
			robot.down();
			this.height--;
		}
	}

}