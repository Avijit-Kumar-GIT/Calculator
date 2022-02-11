import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import components.naturalnumber.NaturalNumber;

/**
 * View class.
 *
 * @author Avijit Kumar
 */
public final class NNCalcView1 extends JFrame implements NNCalcView {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Controller object registered with this view to observe user-interaction
     * events.
     */
    private NNCalcController controller;

    /**
     * State of user interaction: last event "seen".
     */
    private static enum State {
        /**
         * Last event was clear, enter, another operator, or digit entry, resp.
         */
        SAW_CLEAR, SAW_ENTER, SAW_OTHER_OP, SAW_DIGIT
    }

    /**
     * State variable to keep track of which event happened last; needed to
     * prepare for digit to be added to bottom operand.
     */
    private State currentState;

    /**
     * Text areas.
     */
    private final JTextArea tTop, tBottom;

    /**
     * Operator and related buttons.
     */
    private final JButton bClear, bSwap, bEnter, bAdd, bSubtract, bMultiply,
            bDivide, bPower, bRoot;

    /**
     * Digit entry buttons.
     */
    private final JButton[] bDigits;

    /**
     * Useful constants.
     */
    private static final int TEXT_AREA_HEIGHT = 5, TEXT_AREA_WIDTH = 20,
            DIGIT_BUTTONS = 10, MAIN_BUTTON_PANEL_GRID_ROWS = 4,
            MAIN_BUTTON_PANEL_GRID_COLUMNS = 4, SIDE_BUTTON_PANEL_GRID_ROWS = 3,
            SIDE_BUTTON_PANEL_GRID_COLUMNS = 1, CALC_GRID_ROWS = 3,
            CALC_GRID_COLUMNS = 1;

    /**
     * Default constructor.
     */
    public NNCalcView1() {
        // Create the JFrame being extended

        /*
         * Call the JFrame (superclass) constructor with a String parameter to
         * name the window in its title bar
         */
        super("Natural Number Calculator");

        // Set up the GUI widgets --------------------------------------------

        /*
         * Set up initial state of GUI to behave like last event was "Clear";
         * currentState is not a GUI widget per se, but is needed to process
         * digit button events appropriately
         */
        this.currentState = State.SAW_CLEAR;

        /*
         * Create widgets
         */
        this.tTop = new JTextArea("", TEXT_AREA_HEIGHT, TEXT_AREA_WIDTH);
        this.tBottom = new JTextArea("", TEXT_AREA_HEIGHT, TEXT_AREA_WIDTH);
        this.bDigits = new JButton[DIGIT_BUTTONS];
        
        String[] numberButtons = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        for (int i = 0; i < DIGIT_BUTTONS; i++) {
            this.bDigits[i] = new JButton(numberButtons[i]);
        }
        
        this.bAdd = new JButton("+");
        this.bClear = new JButton("Clear");
        this.bDivide = new JButton("/");
        this.bEnter = new JButton("Enter");
        this.bMultiply = new JButton("*");
        this.bPower = new JButton("Power");
        this.bRoot = new JButton("Root");
        this.bSubtract = new JButton("-");
        this.bSwap = new JButton("Swap");
        
        // Set up the GUI widgets --------------------------------------------

        /*
         * Text areas should wrap lines, and should be read-only; they cannot be
         * edited because allowing keyboard entry would require checking whether
         * entries are digits, which we don't want to have to do
         */
        this.tTop.setEditable(false);
        this.tTop.setLineWrap(true);
        this.tTop.setWrapStyleWord(true);
        this.tBottom.setEditable(false);
        this.tBottom.setLineWrap(true);
        this.tBottom.setWrapStyleWord(true);
        
        /*
         * Initially, the following buttons should be disabled: divide (divisor
         * must not be 0) and root (root must be at least 2) -- hint: see the
         * JButton method setEnabled
         */
        this.bDivide.setEnabled(false);
        this.bRoot.setEnabled(false);
        
        /*
         * Create scroll panes for the text areas in case number is long enough
         * to require scrolling
         */
        JScrollPane inScrollPane = new JScrollPane(this.tTop);
        JScrollPane outScrollPane = new JScrollPane(this.tBottom);
        
        /*
         * Create main button panel
         */
        JPanel mainButtonPanel = new JPanel(new GridLayout(MAIN_BUTTON_PANEL_GRID_ROWS, MAIN_BUTTON_PANEL_GRID_COLUMNS));
        
        /*
         * Add the buttons to the main button panel, from left to right
         * and top to bottom
         */

        // Create the starting and ending points for each row
        final int rowFive = 7;
        final int rowFour = 6;
        final int rowThree = 4;
        final int rowTwo = 3;
        final int rowOne = 1;
        
        // This is for the third row of buttons with a width of 3 (7-9)
        for (int i = rowFive; i < DIGIT_BUTTONS; i++) {
        	mainButtonPanel.add(this.bDigits[i]);
        }
        
        // Add the Add operation in the third row
        mainButtonPanel.add(this.bAdd);
        
        // This is for the second row of buttons with a width of 3 (4-6)
        for (int i = rowThree; i <= rowFour; i++) {
        	mainButtonPanel.add(this.bDigits[i]);
        }
        
        // Add the Subtract operation in the second row
        mainButtonPanel.add(this.bSubtract);
        
        // This is for the first row of buttons with a width of 3 (1-3)
        for (int i = rowOne; i <= rowTwo; i++) {
        	mainButtonPanel.add(this.bDigits[i]);
        }
        
        // Add the Multiply operation in the first row
        mainButtonPanel.add(this.bMultiply);
        
        // Add the value of zero to the bottom row
        mainButtonPanel.add(this.bDigits[0]);
        
        // Add all the operations buttons
        mainButtonPanel.add(this.bPower);
        mainButtonPanel.add(this.bRoot);
        mainButtonPanel.add(this.bDivide);
        
        /*
         * Create side button panel
         */
        JPanel sideButtonPanel = new JPanel(new GridLayout(
                SIDE_BUTTON_PANEL_GRID_ROWS, SIDE_BUTTON_PANEL_GRID_COLUMNS));

        /*
         * Add the buttons to the side button panel, from left to right and top
         * to bottom
         */
        sideButtonPanel.add(this.bClear);
        sideButtonPanel.add(this.bSwap);
        sideButtonPanel.add(this.bEnter);
        
        /*
         * Create combined button panel organized using flow layout, which is
         * simple and does the right thing: sizes of nested panels are natural,
         * not necessarily equal as with grid layout
         */
        JPanel combButtonPanel = new JPanel(new FlowLayout());
        
        /*
         * Add the other two button panels to the combined button panel
         */
        combButtonPanel.add(mainButtonPanel);
        combButtonPanel.add(sideButtonPanel);
        
        /*
         * Organize main window
         */
        this.setLayout(new GridLayout(CALC_GRID_ROWS, CALC_GRID_COLUMNS));
        
        /*
         * Add scroll panes and button panel to main window, from left to right
         * and top to bottom
         */
        this.add(inScrollPane);
        this.add(outScrollPane);
        this.add(combButtonPanel);
        
        // Set up the observers ----------------------------------------------

        /*
         * Register this object as the observer for all GUI events
         */
        
        // Turns all other non-numerical values into pressable buttons
        this.bAdd.addActionListener(this);
        this.bDivide.addActionListener(this);
        this.bEnter.addActionListener(this);
        this.bMultiply.addActionListener(this);
        this.bPower.addActionListener(this);
        this.bRoot.addActionListener(this);
        this.bSubtract.addActionListener(this);
        this.bSwap.addActionListener(this);
        this.bClear.addActionListener(this);
        
        // Loops this to turn all the numbers into pressable buttons
        for (int i = 0; i < DIGIT_BUTTONS; i++) {
            this.bDigits[i].addActionListener(this);
        }
        
        // Set up the main application window --------------------------------

        /*
         * Make sure the main window is appropriately sized, exits this program
         * on close, and becomes visible to the user
         */
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void registerObserver(NNCalcController controller) {
    	
        // Make the controller an observer to the user's inputs
        this.controller = controller;
    }

    @Override
    public void updateTopDisplay(NaturalNumber n) {
    	
    	// Change the number in the top to the value n given by the user
        this.tTop.setText(n.toString());
    }

    @Override
    public void updateBottomDisplay(NaturalNumber n) {
    	
        // Change the number in the bottom to the value n given by the user
        this.tBottom.setText(n.toString());
    }
    
    /*
     * The variable allowed is comes from NNCalcController1.java and is used 
     * to determine if an operation button is enabled or disabled
     */

    @Override
    public void updateSubtractAllowed(boolean allowed) {
    	
    	// Allow subtract to be enabled or disabled if variable allowed is true or false        
        if (allowed) {
            this.bSubtract.setEnabled(true);
        } else {
            this.bSubtract.setEnabled(false);
        }
    }

    @Override
    public void updateDivideAllowed(boolean allowed) {
    	
        // Allow divide to be enabled or disabled if variable allowed is true or false
        if (allowed) {
            this.bDivide.setEnabled(true);
        } else {
            this.bDivide.setEnabled(false);
        }
    }

    @Override
    public void updatePowerAllowed(boolean allowed) {
    	
    	// Allow power to be enabled or disabled if variable allowed is true or false
        if (allowed) {
            this.bPower.setEnabled(true);
        } else {
            this.bPower.setEnabled(false);
        }
    }

    @Override
    public void updateRootAllowed(boolean allowed) {
    	
    	// Allow root to be enabled or disabled if variable allowed is true or false
        if (allowed) {
            this.bRoot.setEnabled(true);
        } else {
            this.bRoot.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        /*
         * Set cursor to indicate computation on-going; this matters only if
         * processing the event might take a noticeable amount of time as seen
         * by the user
         */
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        /*
         * Determine which event has occurred that we are being notified of by
         * this callback; in this case, the source of the event (i.e, the widget
         * calling actionPerformed) is all we need because only buttons are
         * involved here, so the event must be a button press; in each case,
         * tell the controller to do whatever is needed to update the model and
         * to refresh the view
         */
        Object source = event.getSource();
        if (source == this.bClear) {
            this.controller.processClearEvent();
            this.currentState = State.SAW_CLEAR;
        } else if (source == this.bSwap) {
            this.controller.processSwapEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bEnter) {
            this.controller.processEnterEvent();
            this.currentState = State.SAW_ENTER;
        } else if (source == this.bAdd) {
            this.controller.processAddEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bSubtract) {
            this.controller.processSubtractEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bMultiply) {
            this.controller.processMultiplyEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bDivide) {
            this.controller.processDivideEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bPower) {
            this.controller.processPowerEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.bRoot) {
            this.controller.processRootEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else {
            for (int i = 0; i < DIGIT_BUTTONS; i++) {
                if (source == this.bDigits[i]) {
                    switch (this.currentState) {
                        case SAW_ENTER:
                            this.controller.processClearEvent();
                            break;
                        case SAW_OTHER_OP:
                            this.controller.processEnterEvent();
                            this.controller.processClearEvent();
                            break;
                        default:
                            break;
                    }
                    this.controller.processAddNewDigitEvent(i);
                    this.currentState = State.SAW_DIGIT;
                    break;
                }
            }
        }
        /*
         * Set the cursor back to normal (because we changed it at the beginning
         * of the method body)
         */
        this.setCursor(Cursor.getDefaultCursor());
    }

}