import javax.swing.*;


public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Game Started...............!");
        int bw=360;
        int bh=640;

        JFrame frame =new JFrame("My version of Flappy Bird");
        frame.setSize(bw,bh);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Flappybird flappybird=new Flappybird();
        frame.add(flappybird);
        frame.pack();
        flappybird.requestFocus();
        frame.setVisible(true);
    }
}