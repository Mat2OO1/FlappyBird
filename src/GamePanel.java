import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    //constants
    private final int screenWidth = Constants.SCREEN_WIDTH;
    private final int screenHeight = Constants.SCREEN_HEIGHT;
    private final int delay = Constants.DELAY;
    private final int units = Constants.UNITS;
    private final int unitSize = Constants.SQUARE_SIZE;

    //attributes
    private int score = 0;
    private int size = Constants.INITIAL_SIZE;
    private Point position = new Point(screenWidth / 10, screenHeight * 2 / 5);
    private Point obstaclePosition = new Point(position.x + screenWidth / 2, screenHeight / 2);
    private boolean scored = true;
    private boolean clicked = false;
    boolean running = false;
    private Timer timer;
    private Random random = new Random();


    public GamePanel() {
        Dimension windowSize = new Dimension(screenWidth, screenHeight);
        this.setPreferredSize(windowSize);
        this.setBackground(new Color(135, 206, 235));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            drawObstacles(g);
            g.setColor(Color.red);
            g.fillOval(position.x, position.y, unitSize, unitSize);

            g.setColor(new Color(255, 250, 205));
            g.fillRect(0, screenHeight * 7 / 8, screenWidth, screenHeight / 8);

            g.setColor(Color.green);
            g.fillRect(0, screenHeight * 7 / 8 - screenHeight / 8, screenWidth, screenHeight / 8);

        }
    }

    public void move() {
        if (clicked) {
            position.y -= unitSize;
        } else {
            position.y += unitSize / 2;
        }
    }

    private void drawObstacles(Graphics g) {
        if (scored) {
            obstaclePosition = new Point(position.x + screenWidth / 2, position.y + (random.nextBoolean() ? -1 : 1) * screenWidth / 8);
            scored = false;
        } else {
            obstaclePosition = new Point(obstaclePosition.x - unitSize/2, obstaclePosition.y);
        }
        //painting obstacle
        g.setColor(new Color(154, 205, 50));
        g.fillRect(obstaclePosition.x, 0, unitSize * 5, screenHeight);
        //painting a hole
        g.setColor(new Color(135, 206, 235));
        g.fillRect(obstaclePosition.x, obstaclePosition.y, unitSize * 5, unitSize * 5);
    }

    private void checkScore(){
        if((position.x == obstaclePosition.x) &&
                (position.y < obstaclePosition.y + unitSize*5 && position.y > obstaclePosition.y - unitSize*5)){
            scored = true;
            score++;
        }
        System.out.println("Bird coords: " + position.x + " " + position.y);
        System.out.println("obstacle coords "  + obstaclePosition.x + " " + obstaclePosition.y);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkScore();
            clicked = false;
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE){
                clicked = true;
            }
        }
    }
}
