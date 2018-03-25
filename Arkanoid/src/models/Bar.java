package models;

import java.awt.geom.*;

class Bar extends Rectangle2D.Double
{
    Bar(double x, double y, double width, double height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    void setX(double x)
    {
        this.x = x;
    }
}
