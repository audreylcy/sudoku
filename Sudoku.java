import java.awt.*; // Uses AWT's Layout Managers
import java.awt.event.*; // Uses AWT's Event Handlers
import java.net.URL;
import javax.swing.*; // Uses Swing's Container/Components
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JCheckBoxMenuItem;
import java.util.Random;
import java.awt.Window;
import javax.swing.JOptionPane;

/* The Sudoku game. To solve the number puzzle, each row, each column, and each
 * of the nine 3�3 sub-grids shall contain all of the digits from 1 to 9
 */

public class Sudoku extends JFrame {
  // Name-constants for the game properties
	public static final int GRID_SIZE = 9; // Size of the board
	public static final int SUBGRID_SIZE = 3; // Size of the sub-grid
  // Name-constants for UI control (sizes, colors and fonts)
	public static final int CELL_SIZE = 60; // Cell width/height in pixels
	public static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
	public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
  // Board width/height in pixels
	public static final Color OPEN_CELL_BGCOLOR = Color.pink;
	public static final Color OPEN_CELL_TEXT_YES = new Color(0, 255, 0); // RGB
	public static final Color OPEN_CELL_TEXT_NO = Color.RED;
	public static final Color CLOSED_CELL_BGCOLOR = Color.cyan; // RGB
	public static final Color CLOSED_CELL_TEXT = Color.BLACK;
	public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);
	
	private static int maskQTY = 5;
  // The game board composes of 9x9 JTextFields,
  // each containing String "1" to "9", or empty String
	private JTextField[][] tfCells = new JTextField[GRID_SIZE][GRID_SIZE];

  // Puzzle to be solved and the mask (which can be used to control the
  // difficulty level).
  // Hardcoded here. Extra credit for automatic puzzle generation
  // with various difficulty levels.
	private int[][] puzzle = { 
			{ 5, 3, 4, 6, 7, 8, 9, 1, 2 }, 
			{ 6, 7, 2, 1, 9, 5, 3, 4, 8 },
			{ 1, 9, 8, 3, 4, 2, 5, 6, 7 }, 
			{ 8, 5, 9, 7, 6, 1, 4, 2, 3 }, 
			{ 4, 2, 6, 8, 5, 3, 7, 9, 1 },
			{ 7, 1, 3, 9, 2, 4, 8, 5, 6 }, 
			{ 9, 6, 1, 5, 3, 7, 2, 8, 4 }, 
			{ 2, 8, 7, 4, 1, 9, 6, 3, 5 },
			{ 3, 4, 5, 2, 8, 6, 1, 7, 9 } 
	};
  // For testing, open only 2 cells.
	private boolean[][] masks = {  
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false } 
	};
	
	public boolean[][] shuffleMasks(int maskQTY) {
		boolean[][] tempMasks = masks;
		
		for (int i = maskQTY; i >= 1; i--) {
			int r1, r2;
			r1 = (int) (Math.random()* 9);
			r2 = (int) (Math.random()* 9);
			tempMasks [r1][r2] = true;
		}
		
		masks = tempMasks;
		return masks;
	}
	
	
	public int[][] shuffle() {
		int[][] tempPuzzle = puzzle;
		for (int i = 0; i <= 8; i += SUBGRID_SIZE) {
			tempPuzzle = rowShuffle(i, tempPuzzle); // shuffle the 3 rows
			tempPuzzle = transpose(tempPuzzle); // transpose to shuffle column
			tempPuzzle = rowShuffle(i, tempPuzzle); // shuffle the 3 columns
			tempPuzzle = transpose(tempPuzzle); // transpose to shuffle row
		 }
		return tempPuzzle; // total of 6 shuffles, 3 rows and 3 cols
	}

			   /* This helps to swap a random row once, and the row has to be within 1-3, 4-6
			    * and 7-9
			    * 
			    * @param low        is the first row number of the set
			    * @param tempPuzzle the puzzle that is required to be shuffled
			    * @return the shuffled puzzle
			    */
	private int[][] rowShuffle(int low, int[][] tempPuzzle) {
		 Random r = new Random();
		 int change = r.nextInt(SUBGRID_SIZE);
		 int[] temp = tempPuzzle[low];
		 tempPuzzle[low] = tempPuzzle[low + change];
		 tempPuzzle[low + change] = temp;
		 return tempPuzzle;
	}

			   /* Helps to transpose a n by n array
			    * 
			    * @param tempPuzzle n by n array, a puzzle that is required to be transpose
			    * @return transposed n by n array
			    */
	private int[][] transpose(int[][] tempPuzzle) {
		int[][] transpose = new int[tempPuzzle.length][tempPuzzle.length];
		for (int i = 0; i < tempPuzzle.length; i++) {
			for (int j = 0; j < tempPuzzle.length; j++) {
				transpose[i][j] = tempPuzzle[j][i];
			}
		}
		return transpose;
	}
  /* Constructor to setup the game and the UI Components
   */
  

  public Sudoku() {
	  	shuffle();
	  	shuffleMasks(maskQTY);
	  	JMenuBar menuBar;
		JMenu menu, menu2;
		JMenuItem menuItem, menu3;
		
		menuBar = new JMenuBar();
		
		menu = new JMenu("File");
		menuBar.add(menu);
		
		menuItem = new JMenuItem("New Game", KeyEvent.VK_C);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Sudoku();
				
			}
		});
		
		menuItem = new JMenuItem("Reset Game", KeyEvent.VK_R);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		          for (int i = 0; i < GRID_SIZE; i++) {
		              	for (int j = 0; j < GRID_SIZE; j++) {
		              		if(masks[i][j] == true) {
		              			tfCells[i][j].setText("");
		              			tfCells[i][j].setEditable(true);
		              			tfCells[i][j].setBackground(OPEN_CELL_BGCOLOR);
		              		} 
		              	}
		          }
			}
		});
		
		menuItem = new JMenuItem("Exit", KeyEvent.VK_E);
		menu.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		menu2 = new JMenu("Levels");
		menuBar.add(menu2);
		
		menuItem = new JMenuItem("Easy");
		menu2.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				dispose();
				maskQTY = 5;
				new Sudoku();
			}
		});
		
		menuItem = new JMenuItem("Intermediate");
		menu2.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				maskQTY = 15;
				new Sudoku();
			}
		});
		
		menuItem = new JMenuItem("Advance");
		menu2.add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				maskQTY = 40;
				new Sudoku();
			}
		});
		
		menu3 = new JMenuItem("Help");
		menuBar.add(menu3);
		menu3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					      // JOptionPane does not have to run under a Swing Application (extends JFrame).
					      // It can run directly under main().
					      JOptionPane.showMessageDialog(null, "Display a message (returns void)!",
					            "Message Dialog", JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		setJMenuBar(menuBar);

		Container cp = getContentPane();
		cp.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE)); // 9x9 GridLayout

    // Allocate a common listener as the ActionEvent listener for all the
    // JTextFields
    // [TODO 3]
		InputListener listener = new InputListener();

    // Construct 9x9 JTextFields and add to the content-pane
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				tfCells[row][col] = new JTextField(); // Allocate element of array
				cp.add(tfCells[row][col]); // ContentPane adds JTextField
				if (masks[row][col]) {
					tfCells[row][col].setText(""); // set to empty string
					tfCells[row][col].setEditable(true);
					tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);
					tfCells[row][col].addActionListener(listener); // For all editable rows and cols
				} else {
					tfCells[row][col].setText(puzzle[row][col] + "");
					tfCells[row][col].setEditable(false);
					tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
					tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
				}
        // Beautify all the cells
				tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
				tfCells[row][col].setFont(FONT_NUMBERS);
			}
		}

    // Set the size of the content-pane and pack all the components
    // under this container.
		cp.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		pack();
    
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle window closing
		setTitle("Sudoku");
		setVisible(true);
 	}

  /** The entry main() entry method */

  	public static void main(String[] args) {
    // [TODO 1] (Now)
    // Check Swing program template on how to run the constructor
    // The entry main() method
    // Invoke the constructor to setup the GUI, by allocating an anonymous instance
  		new Sudoku();
  	}

  // Define the Listener Inner Class
  // [TODO 2]
  // Inner class to be used as ActionEvent listener for ALL JTextFields
  	private class InputListener implements ActionListener {

    @Override
    	public void actionPerformed(ActionEvent e) {
      // All the 9*9 JTextFileds invoke this handler. We need to determine
      // which JTextField (which row and column) is the source for this invocation.
    		int rowSelected = -1;
    		int colSelected = -1;

      // Get the source object that fired the event
    		JTextField source = (JTextField) e.getSource();
      // Scan JTextFileds for all rows and columns, and match with t
    		boolean found = false;
    		for (int row = 0; row < GRID_SIZE && !found; ++row) {
    			for (int col = 0; col < GRID_SIZE && !found; ++col) {
    				if (tfCells[row][col] == source) {
    					rowSelected = row;
    					colSelected = col;
    					found = true; // break the inner/outer loops
    				}
    			}
    		}
    		int solution = Integer.parseInt(tfCells[rowSelected][colSelected].getText());

    		if (puzzle[rowSelected][colSelected] == solution) {
    			tfCells[rowSelected][colSelected].setBackground(Color.GREEN);
    			tfCells[rowSelected][colSelected].setEditable(false);
    			masks[rowSelected][colSelected] = false;
    			boolean solved = true;
    			for (int i = 0; i < GRID_SIZE; i++) {
    				for (int j = 0; j < GRID_SIZE; j++) {
    					if (masks[i][j] == true) {
    						solved = false;
    					}
    				}
    			}
    			if (solved == true) {
    				JOptionPane.showMessageDialog(null, "Congratulations! Try something else? ");
    				}
    			}

    			else {
    				tfCells[rowSelected][colSelected].setBackground(Color.RED);
    				JOptionPane.showMessageDialog(null, "Conflict! Try again?");
    				tfCells[rowSelected][colSelected].setEditable(true);
    				}
    			}
  		}
	}