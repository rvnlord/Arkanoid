package models;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Ball extends Ellipse2D.Double
{
    private Field field;
    private double dx, dy;

    Ball(Field field, double x, double y, int width, int height, double dx, double dy)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.field = field;
        this.dx = dx;
        this.dy = dy;
    }

    void nextStep()
    {
        if (getMinX() <= 0 || getMaxX() >= field.getWidth())
            dx *= -1;
        
        if (getMinY() <= 0)
            dy *= -1;

        if (getMaxY() >= field.getHeight()) // warunek przegranej
        {
            dy *= -1;
            field.timer.stop();
            field.isLost = true;
            field.repaint();
            return;
        }

        int barPosTop = (int) field.bar.y;
        int barPosBottom = (int) (field.bar.y + field.bar.getHeight());
        int barPosRight = (int) (field.bar.x + field.bar.getWidth());
        int barPosLeft = (int) field.bar.x;

        if (this.intersects(field.bar)) // BUG FIXED: Jeśli paletka zostanie przesunięta i naryusowana w miejscu gdzie jest piłka, dajemy piłkę na górę
        {
            java.lang.Double ballTopBarBottom = field.bar.y + field.bar.getHeight() - this.y;
            java.lang.Double ballBottomBarTop = this.y + this.getHeight() - field.bar.y;
            java.lang.Double ballRightBarLeft = this.x + this.getWidth() - field.bar.x;
            java.lang.Double ballLeftBarRight = field.bar.x + field.bar.getWidth() - this.x;

            Map<String, java.lang.Double> distances = new HashMap<>();
            distances.put("ballTopBarBottom", ballTopBarBottom < 0 || ballTopBarBottom > field.bar.getHeight() ? -1 : ballTopBarBottom);
            distances.put("ballBottomBarTop", ballBottomBarTop < 0 || ballBottomBarTop > field.bar.getHeight() ? -1 : ballBottomBarTop);
            distances.put("ballRightBarLeft", ballRightBarLeft < 0 || ballRightBarLeft > field.bar.getWidth() ? -1 : ballRightBarLeft);
            distances.put("ballLeftBarRight", ballLeftBarRight < 0 || ballLeftBarRight > field.bar.getWidth() ? -1 : ballLeftBarRight);

            Map.Entry<String, java.lang.Double> min = null;
            for (Map.Entry<String, java.lang.Double> entry : distances.entrySet())
                if (entry.getValue() >= 0)
                    if (min == null || entry.getValue() < min.getValue())
                        min = entry;

            if (min != null)
            {
                if (Objects.equals(min.getKey(), "ballTopBarBottom"))
                    this.y = field.bar.y + field.bar.getHeight();
                else if (Objects.equals(min.getKey(), "ballBottomBarTop"))
                    this.y = field.bar.y - this.getHeight();
                else if (Objects.equals(min.getKey(), "ballRightBarLeft"))
                    this.x = field.bar.x - this.getWidth();
                else if (Objects.equals(min.getKey(), "ballLeftBarRight"))
                    this.x = field.bar.x + field.bar.getWidth();
            }
        }

        int ballPosTop = (int) y;
        int ballPosBottom = (int) (y + this.getHeight());
        int ballPosRight = (int) (x + this.getWidth());
        int ballPosLeft = (int) x;

        // Chcę żeby odbicie kantem zawsze odwróciło obydwa wektory, nie tak jak przy cegiełce
        if ((ballPosBottom == barPosTop || ballPosTop == barPosBottom) && ballPosRight >= barPosLeft && ballPosLeft <= barPosRight) // uderzenie w dół lub górę
        {
            int barWidth = (int)field.bar.getWidth();
            int hitPoint = ballPosLeft - barPosLeft + (int)(field.ball.getWidth() / 2);

            if (hitPoint < 0 && hitPoint + (int)(field.ball.getWidth() / 2) >= 0)
                hitPoint = 0;
            else if (hitPoint > barWidth && hitPoint - (int)(field.ball.getWidth() / 2) <= barWidth)
                hitPoint = barWidth;

            double hitPointCenterRelative = 0;

            if (hitPoint > (barWidth / 2))
                hitPointCenterRelative = hitPoint - (barWidth / 2);
            else if (hitPoint < (barWidth / 2))
                hitPointCenterRelative = (barWidth / 2) - hitPoint;

            double maxAngle = 75.0;
            double angleMultiplier = hitPointCenterRelative / ((double)barWidth / 2);
            double angle = angleMultiplier * maxAngle;
            double angleRadians = Math.toRadians(angle);

            double angleSin = Math.sin(angleRadians);
            double angleCos = Math.cos(angleRadians);
            double sqrt2 = Math.sqrt(2);

            dy = dy < 0 ? -1 * angleCos * sqrt2 : angleCos * sqrt2;
            dx = dx < 0 ? -1 * angleSin * sqrt2 : angleSin * sqrt2;
            dy *= -1;
        }

        if ((ballPosRight == barPosLeft || ballPosLeft == barPosRight) && ballPosTop <= barPosBottom && ballPosBottom >= barPosTop) // uderzenie w lewo lub prawo
            dx *= -1;
        
        for (ArrayList<Brick> row : field.bricks)
        {
            for (Brick brick : row)
            {
                if (!brick.getWasHit())
                {
                    int brickPosTop = (int) brick.y;
                    int brickPosBottom = (int) (brick.y + brick.getHeight());
                    int brickPosRight = (int) (brick.x + brick.getWidth());
                    int brickPosLeft = (int) brick.x;

                    if (this.intersects(brick)) // przesuń do styczności z najbliższą ścianą
                    {
                        java.lang.Double ballTopBrickBottom = brick.y + brick.getHeight() - this.y;
                        java.lang.Double ballBottomBrickTop = this.y + this.getHeight() - brick.y;
                        java.lang.Double ballRightBrickLeft = this.x + this.getWidth() - brick.x;
                        java.lang.Double ballLeftBrickRight = brick.x + brick.getWidth() - this.x;

                        Map<String, java.lang.Double> distances = new HashMap<>();
                        distances.put("ballTopBrickBottom", ballTopBrickBottom < 0 || ballTopBrickBottom > brick.getHeight() ? -1 : ballTopBrickBottom);
                        distances.put("ballBottomBrickTop", ballBottomBrickTop < 0 || ballBottomBrickTop > brick.getHeight() ? -1 : ballBottomBrickTop);
                        distances.put("ballRightBrickLeft", ballRightBrickLeft < 0 || ballRightBrickLeft > brick.getWidth() ? -1 : ballRightBrickLeft);
                        distances.put("ballLeftBrickRight", ballLeftBrickRight < 0 || ballLeftBrickRight > brick.getWidth() ? -1 : ballLeftBrickRight);

                        Map.Entry<String, java.lang.Double> min = null;
                        for (Map.Entry<String, java.lang.Double> entry : distances.entrySet())
                            if (entry.getValue() >= 0)
                                if (min == null || entry.getValue() < min.getValue())
                                    min = entry;

                        if (min != null)
                        {
                            if (Objects.equals(min.getKey(), "ballTopBrickBottom"))
                                this.y = brick.y + brick.getHeight();
                            else if (Objects.equals(min.getKey(), "ballBottomBrickTop"))
                                this.y = brick.y - this.getHeight();
                            else if (Objects.equals(min.getKey(), "ballRightBrickLeft"))
                                this.x = brick.x - this.getWidth();
                            else if (Objects.equals(min.getKey(), "ballLeftBrickRight"))
                                this.x = brick.x + brick.getWidth();
                        }

                        ballPosTop = (int) y;
                        ballPosBottom = (int) (y + this.getHeight());
                        ballPosRight = (int) (x + this.getWidth());
                        ballPosLeft = (int) x;
                    }

                    if (((ballPosBottom == brickPosTop || ballPosTop == brickPosBottom) && ballPosRight >= brickPosLeft && ballPosLeft <= brickPosRight) || ((ballPosRight == brickPosLeft || ballPosLeft == brickPosRight) && ballPosTop <= brickPosBottom && ballPosBottom >= brickPosTop))
                    {
                        if ((ballPosBottom == brickPosTop || ballPosTop == brickPosBottom) && (ballPosRight == brickPosLeft || ballPosLeft == brickPosRight)) // uderzenie w róg
                        {
                            if (ballPosBottom == brickPosTop && ballPosRight == brickPosLeft) // lwey górny
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
                            else if (ballPosBottom == brickPosTop && ballPosLeft == brickPosRight) // prawy górny
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
                            else if (ballPosTop == brickPosBottom && ballPosRight == brickPosLeft) // lwey dolny
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
                            else if (ballPosTop == brickPosBottom && ballPosLeft == brickPosRight) // prawy dolny
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
                            if ((ballPosBottom == brickPosTop || ballPosTop == brickPosBottom) && ballPosRight > brickPosLeft && ballPosLeft < brickPosRight) // uderzenie w dół lub górę
                                dy *= -1;

                            if ((ballPosRight == brickPosLeft || ballPosLeft == brickPosRight) && ballPosTop < brickPosBottom && ballPosBottom > brickPosTop) // uderzenie w lewo lub prawo
                                dx *= -1;
                        }

                        brick.setWasHit(true);
                        this.field.updatePoints();
                    }
                }
            }
        }
        
        x += dx;
        y += dy;

        if (field.points == field.bricks.size() * field.bricks.get(0).size()) // warunek wygranej
        {
            field.timer.stop();
            field.isWon = true;
        }

        field.repaint();
    }

}
