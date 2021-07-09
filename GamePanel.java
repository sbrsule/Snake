import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    
    //dimensions of window
    static final int screenWidth = 600; 
    static final int screenHeight = 600;

    static final int unitSize = 25; //Size of unit blocks, 25 is normal
    static final int gameUnits = (screenWidth * screenHeight)/unitSize; //Calculates the number of units in the window
    static final int delay = unitSize*3; //Timing delay to refresh frame

    //Location
    final int x[] = new int[gameUnits];
    final int y[] = new int[gameUnits];

    int bodyParts = 6; //Number of segments of snake
    int applesEaten; //Number of apples eaten
    int appleX;
    int appleY;
    char direction ='R'; //Direction of snake, 'R' for right, 'L' for left, 'U' for up, and 'D' for down
    boolean running = true;
    Timer timer; //Declares a timer
    Random random; //Declares random

    JButton startOver = new JButton("Start Over");
    boolean restarted = false;
    boolean paused = false;
    

    GamePanel() { //Creates panel
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame() { //Starts the game
        newApple();
        if (!restarted) {
            timer = new Timer(delay, this);
        }
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        
        if(running) {
            for(int i = 0; i <screenWidth/unitSize; i++) {
                g.drawLine(i*unitSize, 0, i*unitSize, screenHeight);
            }
    
            for(int i = 0; i <screenHeight/unitSize; i++) {
                g.drawLine(0, i*unitSize, screenWidth, i*unitSize);
            }
    
            g.setColor(Color.red);
            g.fillRect(appleX, appleY, unitSize, unitSize);
    
            for(int i = 0; i < bodyParts; i++) {
                if(i==0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
    
                else {
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
            }
            if(paused) {
                g.setFont(new Font("Times New Roman", Font.BOLD, 500));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("PAUSED" , (screenWidth - metrics.stringWidth("GAME OVER"))/2 , g.getFont().getSize());
            }
        g.setColor(Color.white);
        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, 5 , g.getFont().getSize());

        }

        else {
            gameOver(g);
        }
        
    }
    public void newApple() {
        appleX = random.nextInt((int)(screenWidth/unitSize))*unitSize;
        appleY = random.nextInt((int)(screenHeight/unitSize))*unitSize;

    }
    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - unitSize;
                break;
            case 'D':
                y[0] = y[0] + unitSize;
                break;
            case 'L':
                x[0] = x[0] - unitSize;
                break;
            case 'R':
                x[0] = x[0] + unitSize;
        }
    }
    public void checkApple() {
        if((x[0] == appleX) && y[0] == appleY) {
            bodyParts++;
            applesEaten += 10;
            newApple();
        }
    }
    public void checkCollisions() {
        //Checks for collision with body
        for(int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i])&& (y[0] == y[i])) {
                running = false;
            }
        }

        //Checks for collision with left wall
        if(x[0] < 0) {
            running = false;
        }

        //Checks for collision with right wall
        if(x[0] >= screenWidth) {
            running = false;
        }

        //Checks for collision with top wall
        if(y[0] < 0) {
            running = false;
        }

        //Checks for collision with bottom wall
        if(y[0] >= screenHeight) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }


    }
    public void gameOver(Graphics g) {
        timer.stop();
        restarted = true;

        g.setColor(Color.red);
        g.setFont(new Font("Times New Roman", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (screenWidth - metrics.stringWidth("GAME OVER"))/2 , (screenHeight/2) - 100);
        g.setColor(Color.white);
        g.setFont(new Font("Times New Roman", Font.BOLD, 30));
        metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (screenWidth - metrics.stringWidth("Score: " + applesEaten))/2, (screenHeight/2)-50);

        this.add(startOver);
        startOver.setBackground(Color.black);
        startOver.setForeground(Color.white);
        startOver.setFocusable(false);
        startOver.setBounds((screenWidth/2) - 50, (screenHeight/2), 100, 30);
        startOver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearBoard();
                running = true;
                bodyParts = 6;
                applesEaten = 0;
                resetPosition();
                startGame();
            }
        });
    }

    public void resetPosition() {
        for (int i = 0; i < bodyParts; i++) {
            x[i] = (bodyParts - i)*unitSize;
            y[i] = 0;
        }

        direction = 'R';
    }

    public void clearBoard() {
        this.removeAll();
        revalidate();
        repaint();
    }

    public void pause() {
        timer.stop();
        paused = true;
    }

    public void resume() {
        timer.start();
        paused = false;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
        
    }

    public boolean checkWouldCollide(char direction) {
        switch(direction) {
            case 'U':
                if(y[1] == y[0] + unitSize) {
                    return true;
                }
                break;
            case 'D':
                if(y[1] == y[0] - unitSize) {
                    return true;
                }
                break;
            case 'L':
                if(x[1] == x[0] - unitSize) {
                    return true;
                }
                break;
            case 'R':
                if(x[1] == x[0] + unitSize) {
                    return true;
                }
                break;
        }
        return false;
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(!checkWouldCollide('L')) {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(!checkWouldCollide('R')) {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(!checkWouldCollide('U')) {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(!checkWouldCollide('D')) {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    if(!paused) {
                        pause();
                        new menuPanel();
                    }
                    else {
                        resume();
                    }
                    break;

            }
        }
    }
}
