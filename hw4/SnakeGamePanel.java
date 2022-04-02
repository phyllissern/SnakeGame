import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

public class SnakeGamePanel extends JPanel{
    
    //private fields
    private static final int WIDTH = 320; //width of the screen of the game
    private static final int HEIGHT = 320; //height of the screen of the game
    private static final int UNIT_SIZE = 20; //size of each unit

    //more private fields, to initialize
    private ArrayList<Sequence> snake; //to store the coordinates of the snake
    private int applesEaten; //number of apples eaten by the snake
    private int delayDecreaser; //keeps track of how many times there is a decrease in delay time
    private int appleX; //x coordinate of the apple
    private int appleY; //y coordinate of the apple
    private int DELAY = 250; //delay time is set to 250
    private Timer timer; //timer to perform animation
    private Random random; //random to generate random numbers
    private char direction; //direction of the arrow key
    private char prevdirection; //remembers the previous arrow key

    /**
     * Initializes the snake game panel
     */
    public SnakeGamePanel() {
        random = new Random(); //creates new random object
        timer = new Timer(DELAY, new MovingListener()); //creates timer with delay time
        setPreferredSize(new Dimension(WIDTH, HEIGHT)); //sets up the size of the screen
        setBackground(Color.blue);//set the background color to be blue
        setFocusable(true); //sets focusable to true
        addKeyListener(new DirectionListener()); //adds a key listener
        newGame(); //calls newGame
    }

    /**
     * Creates a new game by resetting delay time, restarting timer, and generating new snake and apple
     */
    private void newGame() {
        applesEaten = 0; //setting number of apples eaten to 0
        delayDecreaser = 0; //sets the amount of times delay has been decreased to 0
        snake = new ArrayList<>(); //creates ArrayList to store sequence of snake
        int x = random.nextInt((int)(WIDTH/UNIT_SIZE))*UNIT_SIZE; //generate a random x coordinate
        int y = random.nextInt((int)(HEIGHT/UNIT_SIZE))*UNIT_SIZE; //generate a random y coordinate
        Sequence a = new Sequence(x,y); //creates new Sequence
        snake.add(a); //adds the sequence of the coordinates to the snake ArrayList
        nextApple(); //calls nextApple function
        direction = 'N'; //sets direction to no direction key pressed
        DELAY = 250; //sets delay time to 250
        timer.start(); //starts the timer
    }
    
    /**
     * Obtains the direction of the snake
     */
    private class DirectionListener implements KeyListener{
        /**
         * Implementing keyPressed method to get the direction of the snake
         */
        public void keyPressed(KeyEvent event) {
            //get the key pressed
            switch(event.getKeyCode()) {
                //if the right arrow is pressed
                case KeyEvent.VK_RIGHT:
                    //makes sure that the snake can't go in reverse, left key is ignored
                    if(direction != 'L') {
                        direction = 'R'; //set the direction to right
                    }
                    break;
                //if the left arrow is pressed
                case KeyEvent.VK_LEFT:
                    //makes sure that the snake can't go in reverse, right key is ignored
                    if(direction != 'R') {
                        direction = 'L'; //set the direction to left
                    }
                    break;
                //if the up arrow is pressed
                case KeyEvent.VK_UP:
                    //makes sure that the snake can't go in reverse, down key is ignored
                    if(direction != 'D') {
                        direction = 'U'; //set the direction to up
                    }
                    break;
                //if the down arrow is pressed
                case KeyEvent.VK_DOWN:
                    //makes sure that the snake can't go in reverse, up key is ignored
                    if(direction != 'U') {
                        direction = 'D'; //set the direction to down
                    }
                    break;
                //if the space arrow is pressed
                case KeyEvent.VK_SPACE:
                    prevdirection = direction; //stores the previous direction
                    direction = 'S'; //set the direction to space
                    break;
            }
        }
        //no need to implement keyReleased method, so left it empty
        public void keyReleased(KeyEvent event) {}
        //no need to implement keyTyped method, so left it empty
        public void keyTyped(KeyEvent event) {}
    }
    
    /**
     * Implements the ActionListener interface
     */
    private class MovingListener implements ActionListener{
        /**
         * Updates the snake's position, depending on key obtained from DirectionListener
         */
        public void actionPerformed(ActionEvent e) {
            
            //if spacebar is pressed
            if(direction == 'S') {
                //display dialog box
                JOptionPane.showMessageDialog(null, "Pause, continue?", "Information", JOptionPane.INFORMATION_MESSAGE);
                direction = prevdirection; //set the direction to previous direction
            }
            
            //for loop to move the snake's position around the game
            for(int i = snake.size()-1; i > 0; i--) {
                snake.get(i).x = snake.get(i-1).x; //moves the x snake coordinate
                snake.get(i).y = snake.get(i-1).y; //moves the y snake coordinate
            }
            //if direction is up
            if(direction == 'U') {
                snake.get(0).y -= UNIT_SIZE; //moves snake up by 20 pixels
            }
            //if direction is down
            else if(direction == 'D') {
                snake.get(0).y += UNIT_SIZE; //moves snake down by 20 pixels
            }
            //if direction is left
            else if(direction == 'L') {
                snake.get(0).x -= UNIT_SIZE; //moves snake left by 20 pixels
            }
            //if direction is right
            else if(direction == 'R') {
                snake.get(0).x += UNIT_SIZE; //moves snake right by 20 pixels
            }
            
            isGameOver(); //checks if there is a collision

            //checks if snake has "eaten" the apple
            if(snake.get(0).x == appleX && snake.get(0).y == appleY) {
                Sequence b = new Sequence(appleX, appleY); //create new sequence with the apple coordinates
                snake.add(0,b); //add the coordinates to the snake
                applesEaten++; //increase number of apples eaten by one
                nextApple(); //calls nextApple() method
            }

            repaint(); //repaints the panel
            
        }
    }
    
    /**
     * Paints the panel with the apple and snake
     * @param g the graphics on the panel
     */
    protected void paintComponent(Graphics g) {

        super.paintComponent(g); //inherits methods from parent class
        g.setColor(Color.green); //set the apple color to green
        g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //fills the apple in with green
       
        //for loop to go through coordinates of the snake
        for(int i = 0; i < snake.size(); i++) {
            g.setColor(Color.red); //set snake color to red
            g.fillRect(snake.get(i).x,snake.get(i).y, UNIT_SIZE, UNIT_SIZE); //fills coordinates of snake with red
        }
        
    }

    /**
     * Generates a new apple 
     */
    private void nextApple() {
        boolean onSnake = true; //check if the apple coordinate are on the snake body
        //while loop to check if generated coordinates are on snake body
        while(onSnake) {
            onSnake = false; //set boolean to false
            appleX = random.nextInt((int)(WIDTH/UNIT_SIZE))*UNIT_SIZE; //generate random x coordinate of apple
            appleY = random.nextInt((int)(HEIGHT/UNIT_SIZE))*UNIT_SIZE; //generate random y coordinate of apple
            //for loop to go through each Sequence in snake
            for(Sequence a : snake) {
                //if the x and y coordinates from snake match with apple
                if(a.x == appleX && a.y == appleY) {
                    onSnake = true; //set boolean to true so while loop runs again
                }
            }
        }
        //check how many apples were eaten to check when to decrease delay time
        if(applesEaten != 0) {
            //check for every 4 apples eaten and if the highest level of game has been reached
            if(applesEaten % 4 == 0 && delayDecreaser < 10) {
                DELAY -= 20; //decrease delay time by 20
                delayDecreaser += 1; //increase by 1
            }
        }
    }
    
    /**
     * Checks whether the game is over
     * @return result, if the game is over or not
     */
    private boolean isGameOver() {

        boolean result = false; //set result to false

        //for loop to go through each sequence of snake body
        for(int i = snake.size()-1; i > 0; i--) {
            //if head of snake collides with its body
            if ((snake.get(0).x == snake.get(i).x) && (snake.get(0).y == snake.get(i).y)) {
                result = true; //set result to true
            }
        }
        //check if head touches left border
        if(snake.get(0).x < 0) {
            result = true; //set result to true
        }
        //check if head touches right border
        if(snake.get(0).x > WIDTH) {
            result = true; //set result to true
        }
        //check if head touches top border
        if(snake.get(0).y < 0) {
            result = true; //set result to true
        }
        //check if head touches bottom border
        if(snake.get(0).y > HEIGHT) {
            result = true; //set result to true
        }
       
        //if result is true
        if(result) {
            timer.stop(); //stop the timer
            //display dialog box that says game is over, and if player wants to play a new game
            JOptionPane.showMessageDialog(null, "Game Over, New Game?", "Information", JOptionPane.INFORMATION_MESSAGE);
            timer.restart(); //restarts the timer
            newGame(); //calls for a new game
        }
        
        return result; //returns the result

    }

    /**
     * Stores the 2D coordinates of snake
     */
    private class Sequence {

        //public fields
        public int x;
        public int y;

        /**
         * Initializes Sequence class
         * @param x the x coordinate
         * @param y the y coordinate
         */
        public Sequence(int x, int y) {
            this.x = x; //initializes x 
            this.y = y; //initializes y
        }
    }
    

}
