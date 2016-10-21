import java.awt.geom.*;
import java.util.ArrayList;

public class Cegielka extends Rectangle2D.Double
{
    private boolean zbita;
    
    Cegielka(double x, double y, double dlugosc, double wysokosc, boolean zbita)
    {
        this.x = x;
        this.y = y;
        this.width = dlugosc;
        this.height = wysokosc;
        this.zbita = zbita;
    }

    public boolean getZbita()
    {
        return this.zbita;
    }
    
    public void setZbita(boolean zbita)
    {
        this.zbita = zbita;
    }
    
    void setX(int x)
    {
        this.x = x;
    }
    
    public static ArrayList<ArrayList<Cegielka>> utworzCegielki(int planszaDlugosc, int wiersze, int kolumny, int dlugosc, int wysokosc, int pozPion, double odstep)
    {
        double poczatkowaPozPoziom = (planszaDlugosc / 2) - ((kolumny * dlugosc + (kolumny - 1) * odstep) / 2);
        double pozPoziom = poczatkowaPozPoziom;
        
        ArrayList<ArrayList<Cegielka>> cegielki = new ArrayList<>();
        
        for (int i = 0; i < wiersze; i++)
        {
            cegielki.add(new ArrayList<>());
            for (int j = 0; j < kolumny; j++)
            {
                cegielki.get(i).add(new Cegielka(pozPoziom, pozPion, dlugosc, wysokosc, false));
                pozPoziom += dlugosc + odstep;
            }
            pozPoziom = poczatkowaPozPoziom;
            pozPion += wysokosc + odstep;
        }
        
        return cegielki;
    }
}
