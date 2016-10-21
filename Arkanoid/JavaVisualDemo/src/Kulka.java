import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Kulka extends Ellipse2D.Double
{
    Pole plansza;
    double dx, dy;

    Kulka(Pole plansza, double x, double y, int dlugosc, int wysokosc, double dx, double dy)
    {
        this.x = x;
        this.y = y;
        this.width = dlugosc;
        this.height = wysokosc;

        this.plansza = plansza;
        this.dx = dx;
        this.dy = dy;
    }

    void nextKrok()
    {
        if (getMinX() <= 0 || getMaxX() >= plansza.getWidth())
            dx *= -1;
        
        if (getMinY() <= 0)
            dy *= -1;

        if (getMaxY() >= plansza.getHeight()) // warunek przegranej
        {
            dy *= -1;
            plansza.timer.stop();
            plansza.przegrana = true;
            plansza.repaint();
            return;
        }

        int belkaPozGora = (int) plansza.belka.y;
        int belkaPozDol = (int) (plansza.belka.y + plansza.belka.getHeight());
        int belkaPozPrawa = (int) (plansza.belka.x + plansza.belka.getWidth());
        int belkaPozLewa = (int) plansza.belka.x;

        if (this.intersects(plansza.belka)) // BUG FIXED: Jeśli paletka zostanie przesunięta i naryusowana w miejscu gdzie jest piłka, dajemy piłkę na górę
        {
            //this.y = (int) (belkaPozGora - this.getHeight());

            //showMessageDialog(null, "intersect!"); // TEST

            //

            java.lang.Double kulkaGoraBelkaDol = plansza.belka.y + plansza.belka.getHeight() - this.y;
            java.lang.Double kulkaDolBelkaGora = this.y + this.getHeight() - plansza.belka.y;
            java.lang.Double kulkaPrawoBelkaLewo = this.x + this.getWidth() - plansza.belka.x;
            java.lang.Double kulkaLewoBelkaPrawo = plansza.belka.x + plansza.belka.getWidth() - this.x;

            Map<String, java.lang.Double> odleglosci = new HashMap<>();
            odleglosci.put("kulkaGoraBelkaDol", kulkaGoraBelkaDol < 0 || kulkaGoraBelkaDol > plansza.belka.getHeight() ? -1 : kulkaGoraBelkaDol);
            odleglosci.put("kulkaDolBelkaGora", kulkaDolBelkaGora < 0 || kulkaDolBelkaGora > plansza.belka.getHeight() ? -1 : kulkaDolBelkaGora);
            odleglosci.put("kulkaPrawoBelkaLewo", kulkaPrawoBelkaLewo < 0 || kulkaPrawoBelkaLewo > plansza.belka.getWidth() ? -1 : kulkaPrawoBelkaLewo);
            odleglosci.put("kulkaLewoBelkaPrawo", kulkaLewoBelkaPrawo < 0 || kulkaLewoBelkaPrawo > plansza.belka.getWidth() ? -1 : kulkaLewoBelkaPrawo);

            Map.Entry<String, java.lang.Double> min = null;
            for (Map.Entry<String, java.lang.Double> entry : odleglosci.entrySet())
                if (entry.getValue() >= 0)
                    if (min == null || entry.getValue() < min.getValue())
                        min = entry;

            if (min != null)
            {
                if (Objects.equals(min.getKey(), "kulkaGoraBelkaDol"))
                    this.y = plansza.belka.y + plansza.belka.getHeight();
                else if (Objects.equals(min.getKey(), "kulkaDolBelkaGora"))
                    this.y = plansza.belka.y - this.getHeight();
                else if (Objects.equals(min.getKey(), "kulkaPrawoBelkaLewo"))
                    this.x = plansza.belka.x - this.getWidth();
                else if (Objects.equals(min.getKey(), "kulkaLewoBelkaPrawo"))
                    this.x = plansza.belka.x + plansza.belka.getWidth();
            }

            //
        }

        int kulkaPozGora = (int) y;
        int kulkaPozDol = (int) (y + this.getHeight());
        int kulkaPozPrawa = (int) (x + this.getWidth());
        int kulkaPozLewa = (int) x;

        // Chcę żeby odbicie kantem zawsze odwróciło obydwa wektory, nie tak jak przy cegiełce
        if ((kulkaPozDol == belkaPozGora || kulkaPozGora == belkaPozDol) && kulkaPozPrawa >= belkaPozLewa && kulkaPozLewa <= belkaPozPrawa) // uderzenie w dół lub górę
        {
            int dlBelki = (int)plansza.belka.getWidth();
            int miejsceUderzenia = kulkaPozLewa - belkaPozLewa + (int)(plansza.kulka.getWidth() / 2);

            if (miejsceUderzenia < 0 && miejsceUderzenia + (int)(plansza.kulka.getWidth() / 2) >= 0)
                miejsceUderzenia = 0;
            else if (miejsceUderzenia > dlBelki && miejsceUderzenia - (int)(plansza.kulka.getWidth() / 2) <= dlBelki)
                miejsceUderzenia = dlBelki;

            double mUdWzglSrodka = 0;

            if (miejsceUderzenia > (dlBelki / 2))
                mUdWzglSrodka = miejsceUderzenia - (dlBelki / 2);
            else if (miejsceUderzenia < (dlBelki / 2))
                mUdWzglSrodka = (dlBelki / 2) - miejsceUderzenia;

            double maxKat = 75.0;
            double mnoznikKata = mUdWzglSrodka / ((double)dlBelki / 2);
            double kat = mnoznikKata * maxKat;
            double katRadiany = Math.toRadians(kat);

            double sinKata = Math.sin(katRadiany);
            double cosKata = Math.cos(katRadiany);
            double sqrt2 = Math.sqrt(2);

            dy = dy < 0 ? -1 * cosKata * sqrt2 : cosKata * sqrt2;
            dx = dx < 0 ? -1 * sinKata * sqrt2 : sinKata * sqrt2;
            //double dwa = Math.pow(dx, 2) + Math.pow(dy, 2); // TEST POPRAWNOŚCI
            dy *= -1;
        }

        if ((kulkaPozPrawa == belkaPozLewa || kulkaPozLewa == belkaPozPrawa) && kulkaPozGora <= belkaPozDol && kulkaPozDol >= belkaPozGora) // uderzenie w lewo lub prawo
            dx *= -1;
        
        for (ArrayList<Cegielka> wiersz : plansza.cegielki)
        {
            for (Cegielka cegielka : wiersz)
            {
                if (!cegielka.getZbita())
                {
                    int cegielkaPozGora = (int) cegielka.y;
                    int cegielkaPozDol = (int) (cegielka.y + cegielka.getHeight());
                    int cegielkaPozPrawa = (int) (cegielka.x + cegielka.getWidth());
                    int cegielkaPozLewa = (int) cegielka.x;

                    if (this.intersects(cegielka)) // przesuń do styczności z najbliższą ścianą
                    {
                        //showMessageDialog(null, "intersect!"); // TEST

                        //

                        java.lang.Double kulkaGoraCegielkaDol = cegielka.y + cegielka.getHeight() - this.y;
                        java.lang.Double kulkaDolCegielkaGora = this.y + this.getHeight() - cegielka.y;
                        java.lang.Double kulkaPrawoCegielkaLewo = this.x + this.getWidth() - cegielka.x;
                        java.lang.Double kulkaLewoCegielkaPrawo = cegielka.x + cegielka.getWidth() - this.x;

                        Map<String, java.lang.Double> odleglosci = new HashMap<>();
                        odleglosci.put("kulkaGoraCegielkaDol", kulkaGoraCegielkaDol < 0 || kulkaGoraCegielkaDol > cegielka.getHeight() ? -1 : kulkaGoraCegielkaDol);
                        odleglosci.put("kulkaDolCegielkaGora", kulkaDolCegielkaGora < 0 || kulkaDolCegielkaGora > cegielka.getHeight() ? -1 : kulkaDolCegielkaGora);
                        odleglosci.put("kulkaPrawoCegielkaLewo", kulkaPrawoCegielkaLewo < 0 || kulkaPrawoCegielkaLewo > cegielka.getWidth() ? -1 : kulkaPrawoCegielkaLewo);
                        odleglosci.put("kulkaLewoCegielkaPrawo", kulkaLewoCegielkaPrawo < 0 || kulkaLewoCegielkaPrawo > cegielka.getWidth() ? -1 : kulkaLewoCegielkaPrawo);

                        Map.Entry<String, java.lang.Double> min = null;
                        for (Map.Entry<String, java.lang.Double> entry : odleglosci.entrySet())
                            if (entry.getValue() >= 0)
                                if (min == null || entry.getValue() < min.getValue())
                                    min = entry;

                        if (min != null)
                        {
                            if (Objects.equals(min.getKey(), "kulkaGoraCegielkaDol"))
                                this.y = cegielka.y + cegielka.getHeight();
                            else if (Objects.equals(min.getKey(), "kulkaDolCegielkaGora"))
                                this.y = cegielka.y - this.getHeight();
                            else if (Objects.equals(min.getKey(), "kulkaPrawoCegielkaLewo"))
                                this.x = cegielka.x - this.getWidth();
                            else if (Objects.equals(min.getKey(), "kulkaLewoCegielkaPrawo"))
                                this.x = cegielka.x + cegielka.getWidth();
                        }

                        //

                        kulkaPozGora = (int) y;
                        kulkaPozDol = (int) (y + this.getHeight());
                        kulkaPozPrawa = (int) (x + this.getWidth());
                        kulkaPozLewa = (int) x;
                    }

                    if (((kulkaPozDol == cegielkaPozGora || kulkaPozGora == cegielkaPozDol) && kulkaPozPrawa >= cegielkaPozLewa && kulkaPozLewa <= cegielkaPozPrawa) || ((kulkaPozPrawa == cegielkaPozLewa || kulkaPozLewa == cegielkaPozPrawa) && kulkaPozGora <= cegielkaPozDol && kulkaPozDol >= cegielkaPozGora))
                    {
                        if ((kulkaPozDol == cegielkaPozGora || kulkaPozGora == cegielkaPozDol) && (kulkaPozPrawa == cegielkaPozLewa || kulkaPozLewa == cegielkaPozPrawa)) // uderzenie w róg
                        {
                            if (kulkaPozDol == cegielkaPozGora && kulkaPozPrawa == cegielkaPozLewa) // lwey górny
                            {
                                if (dx > 0 && dy > 0) // kierunek: prawo, dół
                                {
                                    dy *= -1;
                                    dx *= -1;
                                }
                                else if (dx > 0 && dy < 0) // kierunek: prawo, góra
                                    dx *= -1;
                                else if (dx < 0 && dy > 0) // kierunek lewo, dół (lewo góra byłby wewnątrz cegiełki)
                                    dy *= -1;
                            }
                            else if (kulkaPozDol == cegielkaPozGora && kulkaPozLewa == cegielkaPozPrawa) // prawy górny
                            {
                                if (dx < 0 && dy > 0) // kierunek: lewo, dół
                                {
                                    dy *= -1;
                                    dx *= -1;
                                }
                                else if (dx < 0 && dy < 0) // kierunek: lewo, góra
                                    dx *= -1;
                                else if (dx > 0 && dy > 0) // kierunek prawo, dół (prawo góra byłby wewnątrz cegiełki)
                                    dy *= -1;
                            }
                            else if (kulkaPozGora == cegielkaPozDol && kulkaPozPrawa == cegielkaPozLewa) // lwey dolny
                            {
                                if (dx > 0 && dy < 0) // kierunek: prawo, góra
                                {
                                    dy *= -1;
                                    dx *= -1;
                                }
                                else if (dx > 0 && dy > 0) // kierunek: prawo, dół
                                    dx *= -1;
                                else if (dx < 0 && dy < 0) // kierunek lewo, góra (lewo dół byłby wewnątrz cegiełki)
                                    dy *= -1;
                            }
                            else if (kulkaPozGora == cegielkaPozDol && kulkaPozLewa == cegielkaPozPrawa) // prawy dolny
                            {
                                if (dx < 0 && dy < 0) // kierunek: lewo, góra
                                {
                                    dy *= -1;
                                    dx *= -1;
                                }
                                else if (dx < 0 && dy > 0) // kierunek: lewo, dół
                                    dx *= -1;
                                else if (dx > 0 && dy < 0) // kierunek prawo, góra (prawo dół byłby wewnątrz cegiełki)
                                    dy *= -1;
                            }
                        }
                        else
                        {
                            if ((kulkaPozDol == cegielkaPozGora || kulkaPozGora == cegielkaPozDol) && kulkaPozPrawa > cegielkaPozLewa && kulkaPozLewa < cegielkaPozPrawa) // uderzenie w dół lub górę
                                dy *= -1;

                            if ((kulkaPozPrawa == cegielkaPozLewa || kulkaPozLewa == cegielkaPozPrawa) && kulkaPozGora < cegielkaPozDol && kulkaPozDol > cegielkaPozGora) // uderzenie w lewo lub prawo
                                dx *= -1;
                        }

                        cegielka.setZbita(true);
                        this.plansza.aktualizujPunkty();
                    }
                }
            }
        }
        
        x += dx;
        y += dy;

        if (plansza.punkty == plansza.cegielki.size() * plansza.cegielki.get(0).size()) // warunek wygranej
        {
            plansza.timer.stop();
            plansza.wygrana = true;
        }

        plansza.repaint();
    }

}
