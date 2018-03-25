package models;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Field extends JPanel implements MouseMotionListener
{
    private static Random rng = new Random();

    Bar bar;
    Ball ball;
    ArrayList<ArrayList<Brick>> bricks;
    private JPanel pnlResults;
    int points;
    public Timer timer;
    Boolean isLost;
    Boolean isWon;
    private Color bricksColor;
    
    public Field(int width, int height, JPanel pnlResults)
    {
        super();
        double sqrt2 = Math.sqrt(2);
        double initDx = rng.nextDouble() * (2 * sqrt2) - sqrt2;
        double initDy = Math.sqrt(2 - Math.pow(initDx, 2));
        this.pnlResults = pnlResults;
        this.points = 0;
        this.setSize(width, height);
        this.bar = new Bar(width / 2 - 40, 420, 80, 10);
        this.ball = new Ball(this, width / 2 - 5, height / 2 - 5, 10, 10, initDx, initDy);

        int colorNum = randInt(1, 10);
        switch (colorNum)
        {
            case 1:
            {
                this.bricksColor = Color.BLUE;
                break;
            }
            case 2:
            {
                this.bricksColor = Color.GREEN;
                break;
            }
            case 3:
            {
                this.bricksColor = Color.RED;
                break;
            }
            case 4:
            {
                this.bricksColor = Color.YELLOW;
                break;
            }
            case 5:
            {
                this.bricksColor = Color.ORANGE;
                break;
            }
            case 6:
            {
                this.bricksColor = Color.ORANGE;
                break;
            }
            case 7:
            {
                this.bricksColor = Color.decode("#9400D3"); // VIOLET
                break;
            }
            case 8:
            {
                this.bricksColor = Color.MAGENTA;
                break;
            }
            case 9:
            {
                this.bricksColor = Color.LIGHT_GRAY;
                break;
            }
            case 10:
            {
                this.bricksColor = Color.decode("#013220"); // DARKGREEN
                break;
            }
            default:
            {
                this.bricksColor = Color.BLUE;
                break;
            }
        }

        int vertPos = 50;
        int maxWidth = this.getWidth() - 40;
        int maxHeight = (this.getHeight() / 2) - vertPos - 10;
        int brickWidth = randInt(20, 100);
        int brickHeight = randInt(10, 20);
        int gap = randInt(10, 20);

        int maxRowsNum = maxHeight / brickHeight;
        int maxColsNum = maxWidth / brickWidth;

        while ((maxRowsNum * brickHeight) + ((maxRowsNum - 1) * gap) > maxHeight)
            maxRowsNum--;

        while ((maxColsNum * brickWidth) + ((maxColsNum - 1) * gap) > maxWidth)
            maxColsNum--;

        int iloscWierszy = randInt(1, maxRowsNum);
        int iloscKolumn = randInt(2, maxColsNum);

        this.bricks = Brick.createBricks(this.getWidth(), iloscWierszy, iloscKolumn, brickWidth, brickHeight, vertPos, gap);
        this.bricks.removeAll(Arrays.asList(null, new ArrayList<Brick>()));

        this.setVisible(true);
        BallEngine be = new BallEngine(ball);
        this.timer = new Timer(randInt(1, 5), be);
        this.isLost = false;
        this.isWon = false;
        this.timer.start();
    }

    private static int randInt(int min, int max)
    {
        return rng.nextInt((max - min) + 1) + min;
    }
    
    void updatePoints()
    {
        String name;
        Component[] components = this.pnlResults.getComponents();
        for (Component comp : components)
        {
            name = comp.getName();
            if (name != null && "lblScores".toLowerCase().equals(name.toLowerCase())) 
            {
                ((JLabel)comp).setText("Punkty: " + ++this.points); // this = plansza
                break;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.YELLOW);
        g2d.fill(ball);
        
        g2d.setColor(Color.WHITE);
        g2d.fill(bar);
        
        g2d.setColor(this.bricksColor);
        for (ArrayList<Brick> wiersz : bricks)
            for (Brick cegielka : wiersz)
                if (!cegielka.getWasHit())
                    g2d.fill(cegielka);

        if (this.isLost)
            renderText(g2d, "Przegrana", Color.decode("#88382D"));
        else if (this.isWon)
            renderText(g2d, "Wygrana", Color.GREEN);
    }

    private void renderText(Graphics2D g2d, String tekst, Color kolor)
    {
        g2d.setColor(kolor);
        Font f = getFont().deriveFont(Font.BOLD, 30);
        FontMetrics metrics = g2d.getFontMetrics(f);
        int x = (this.getWidth() - metrics.stringWidth(tekst)) / 2;
        int y = ((this.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g2d.setFont(f);
        g2d.drawString(tekst, x, y);
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        fixBarPosition(e);
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        fixBarPosition(e);
    }

    private void fixBarPosition(MouseEvent e)
    {
        int barPos = e.getX() - (int)(bar.getWidth() / 2);
        if (barPos >= 0 && barPos + bar.getWidth() <= this.getWidth())
            bar.setX(barPos);
        repaint();
    }
}
