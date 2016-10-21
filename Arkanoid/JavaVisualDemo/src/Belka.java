import java.awt.geom.*;

class Belka extends Rectangle2D.Double
{
    Belka(double x, double y, double dlugosc, double wysokosc)
    {
        this.x = x;
        this.y = y;
        this.width = dlugosc;
        this.height = wysokosc;
    }

    void setX(double x)
    {
        this.x = x;
    }
}
