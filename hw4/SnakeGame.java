import javax.swing.JFrame;

public class SnakeGame {
    public static void main(String args[]) {
        
        JFrame frame = new JFrame("Snake Game"); //create JFrame with title "Snake Game"
        SnakeGamePanel panel = new SnakeGamePanel(); //create SnakeGamePanel
        frame.add(panel); //add panel to JFrame

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //program exits when window is closed
        frame.pack(); //fit the JFrame to size panel
        frame.setResizable(false); //set frame so that it cannot be resized
        frame.setVisible(true); //set frame as visible
        
    }
}
