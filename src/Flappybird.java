import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class Flappybird extends JPanel implements ActionListener, KeyListener
{
    int bw=360;
    int bh=640;

    //img
    Image backGroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImage;

    //bird
    int birdX=bw/8;
    int birdY=bh/2;
    int birdW=34;
    int birdH=24;




    class Bird
    {
        int x=birdX;
        int y=birdY;
        int width=birdW;
        int height=birdH;
        Image img;

        Bird(Image img)
        {
            this.img=img;
        }
    }

    //pipes
    int pipeX=bw;
    int pipeY=0;
    int pipeWidth=64;
    int pipeHeight=512;

    class Pipe
    {
        int x=pipeX;
        int y=pipeY;
        int width=pipeWidth;
        int height=pipeHeight;
        Image img;
        boolean passed=false;

        Pipe(Image img)
        {
            this.img=img;
        }
    }

    //game logic ka bosda
    Bird bird;
    int velocityX=-4;
    int velocityY=0;
    int gravity=1;

    ArrayList<Pipe> pipes;
    Random random=new Random();

    Timer gameLoop;
    Timer placepipesTimer;
    boolean gameOver=false;
    double score=0;

    Flappybird()
    {
        setPreferredSize(new Dimension(bw,bh));
        //setBackground(Color.BLUE);
        setFocusable(true);
        addKeyListener(this);

        backGroundImage=new ImageIcon("src/flappybirdbg.png").getImage();
        birdImage=new ImageIcon("src/flappybird.png").getImage();
        topPipeImage=new ImageIcon("src/toppipe.png").getImage();
        bottomPipeImage=new ImageIcon("src/bottompipe.png").getImage();

        bird =new Bird(birdImage);
        pipes=new ArrayList<Pipe>();



        //pipe timmer
        placepipesTimer=new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placepipesTimer.start();
        // game timmer
        gameLoop=new Timer(1000/60,this);
        gameLoop.start();
    }

    public void placePipes()
    {
        int randomPipeY=(int)(pipeY- pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace= bh/4;

        Pipe topPipe=new Pipe(topPipeImage);
        topPipe.y=randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe=new Pipe(bottomPipeImage);
        bottomPipe.y= topPipe.y +pipeHeight+openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {

        //background
        g.drawImage(backGroundImage,0,0,bw,bh,null);

        //bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);

        for(int i=0;i<pipes.size();i++)
        {
            Pipe pipe=pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
        }

        //score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver)
        {
            g.drawString("Game Over : "+String.valueOf((int)score),10,35);
            g.drawString("Press SpaceBar Key ",10,65);
            g.drawString("To Start Again ",10,95);
        }
        else
        {
            g.drawString("Score : "+String.valueOf((int)score),10,35);
        }
    }

    public void move()
    {
        //bird
        velocityY+=gravity;
        bird.y+=velocityY;
        bird.y=Math.max(bird.y,0);

        //pipes
        for(int i=0;i<pipes.size();i++)
        {
            Pipe pipe=pipes.get(i);
            pipe.x+=velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width)
            {
                pipe.passed=true;
                score+=0.5;
            }

            if(collision(bird,pipe))
            {
                gameOver=true;
            }
        }

        if(bird.y>bh)
        {
            gameOver=true;
        }
    }

    public boolean collision(Bird a,Pipe b)
    {
        return a.x < b.x + b.width && a.x + a.width>b.x && a.y < b.y + b.height && a.y +a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();
        repaint();
        if(gameOver)
        {
            placepipesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_SPACE)
        {
            velocityY=-9;
            if(gameOver)
            {
                bird.y=birdY;
                velocityY=0;
                pipes.clear();
                score=0;
                gameOver=false;
                gameLoop.start();
                placepipesTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}
