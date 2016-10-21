import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Pole extends JPanel implements MouseMotionListener
{
    public static Random rng = new Random();

    Belka belka;
    Kulka kulka;
    ArrayList<ArrayList<Cegielka>> cegielki;
    JPanel wyniki;
    int punkty;
    Timer timer;
    Boolean przegrana;
    Boolean wygrana;
    Color kolorCegielek;
    
    Pole(int width, int height, JPanel wyniki)
    {
        super();
        double sqrt2 = Math.sqrt(2);
        double poczDx = rng.nextDouble() * (2 * sqrt2) - sqrt2;
        double poczDy = Math.sqrt(2 - Math.pow(poczDx, 2));
        //double dwa = poczDx * poczDx + poczDy * poczDy; // SPRAWDZENIE
        this.wyniki = wyniki;
        this.punkty = 0;
        this.setSize(width, height);
        this.belka = new Belka(width / 2 - 40, 420, 80, 10);
        this.kulka = new Kulka(this, width / 2 - 5, height / 2 - 5, 10, 10, poczDx, poczDy);

        int kolorNum = randInt(1, 10);
        switch (kolorNum)
        {
            case 1:
            {
                this.kolorCegielek = Color.BLUE;
                break;
            }
            case 2:
            {
                this.kolorCegielek = Color.GREEN;
                break;
            }
            case 3:
            {
                this.kolorCegielek = Color.RED;
                break;
            }
            case 4:
            {
                this.kolorCegielek = Color.YELLOW;
                break;
            }
            case 5:
            {
                this.kolorCegielek = Color.ORANGE;
                break;
            }
            case 6:
            {
                this.kolorCegielek = Color.ORANGE;
                break;
            }
            case 7:
            {
                this.kolorCegielek = Color.decode("#9400D3"); // VIOLET
                break;
            }
            case 8:
            {
                this.kolorCegielek = Color.MAGENTA;
                break;
            }
            case 9:
            {
                this.kolorCegielek = Color.LIGHT_GRAY;
                break;
            }
            case 10:
            {
                this.kolorCegielek = Color.decode("#013220"); // DARKGREEN
                break;
            }
            default:
            {
                this.kolorCegielek = Color.BLUE;
                break;
            }
        }

        int pozPion = 50;
        int maksSzer = this.getWidth() - 40;
        int maksWys = (this.getHeight() / 2) - pozPion - 10;
        int szerCegielki = randInt(20, 100);
        int wysCegielki = randInt(10, 20);
        int odstep = randInt(10, 20);

        int maksIloscWierszy = maksWys / wysCegielki;
        int maksIloscKolumn = maksSzer / szerCegielki;

        while ((maksIloscWierszy * wysCegielki) + ((maksIloscWierszy - 1) * odstep) > maksWys)
            maksIloscWierszy--;

        while ((maksIloscKolumn * szerCegielki) + ((maksIloscKolumn - 1) * odstep) > maksSzer)
            maksIloscKolumn--;

        int iloscWierszy = randInt(1, maksIloscWierszy);
        int iloscKolumn = randInt(2, maksIloscKolumn);

        this.cegielki = Cegielka.utworzCegielki(this.getWidth(), iloscWierszy, iloscKolumn, szerCegielki, wysCegielki, pozPion, odstep);
        this.cegielki.removeAll(Arrays.asList(null, new ArrayList<Cegielka>()));
        //this.kulka = new Kulka(this, (int)this.cegielki.get(0).get(3).getX() + (int)this.cegielki.get(0).get(3).getWidth() - 40, (int)this.cegielki.get(0).get(3).getY() - 10 - 40, 10, 10, 1, 1); // TEST ODBICIA OD BRZEGU
        this.setVisible(true);
        SilnikKulki sk = new SilnikKulki(kulka);
        this.timer = new Timer(randInt(1, 5), sk);
        this.przegrana = false;
        this.wygrana = false;
        this.timer.start();
    }

    public static int randInt(int min, int max)
    {
        return rng.nextInt((max - min) + 1) + min;
    }
    
    public void aktualizujPunkty()
    {
        String name;
        Component[] components = this.wyniki.getComponents();
        for (Component comp : components)
        {
            name = comp.getName();
            if (name != null && "lblScores".toLowerCase().equals(name.toLowerCase())) 
            {
                ((JLabel)comp).setText("Punkty: " + ++this.punkty); // this = plansza
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
        g2d.fill(kulka);
        
        g2d.setColor(Color.WHITE);
        g2d.fill(belka);
        
        g2d.setColor(this.kolorCegielek);
        for (ArrayList<Cegielka> wiersz : cegielki)
            for (Cegielka cegielka : wiersz)
                if (!cegielka.getZbita())
                    g2d.fill(cegielka);

        if (this.przegrana)
            renderujTekst(g2d, "Przegrana", Color.decode("#88382D"));
        else if (this.wygrana)
            renderujTekst(g2d, "Wygrana", Color.GREEN);
    }

    private void renderujTekst(Graphics2D g2d, String tekst, Color kolor)
    {
        g2d.setColor(kolor);
        Font f = getFont().deriveFont(Font.BOLD, 30);
        FontMetrics wymiary = g2d.getFontMetrics(f);
        int x = (this.getWidth() - wymiary.stringWidth(tekst)) / 2;
        int y = ((this.getHeight() - wymiary.getHeight()) / 2) + wymiary.getAscent();
        g2d.setFont(f);
        g2d.drawString(tekst, x, y);
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        poprawPozycjeBelki(e);
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        poprawPozycjeBelki(e);
    }

    private void poprawPozycjeBelki(MouseEvent e)
    {
        int pozBelki = e.getX() - (int)(belka.getWidth() / 2);
        if (pozBelki >= 0 && pozBelki + belka.getWidth() <= this.getWidth())
            belka.setX(pozBelki);
        repaint();
    }
}
