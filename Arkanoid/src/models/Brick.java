package models;

import java.awt.geom.*;
import java.util.ArrayList;

public class Brick extends Rectangle2D.Double
{
    private boolean wasHit;
    
    Brick(double x, double y, double width, double height, boolean wasHit)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.wasHit = wasHit;
    }

    public boolean getWasHit()
    {
        return this.wasHit;
    }
    
    public void setWasHit(boolean wasHit)
    {
        this.wasHit = wasHit;
    }

    public static ArrayList<ArrayList<Brick>> createBricks(int fieldWidth, int rowsNum, int colsNum, int width, int height, int vertPos, double gap)
    {
        double InitHorizPos = (fieldWidth / 2) - ((colsNum * width + (colsNum - 1) * gap) / 2);
        double horizPos = InitHorizPos;
        
        ArrayList<ArrayList<Brick>> bricks = new ArrayList<>();
        
        for (int i = 0; i < rowsNum; i++)
        {
            bricks.add(new ArrayList<>());
            for (int j = 0; j < colsNum; j++)
            {
                bricks.get(i).add(new Brick(horizPos, vertPos, width, height, false));
                horizPos += width + gap;
            }
            horizPos = InitHorizPos;
            vertPos += height + gap;
        }
        
        return bricks;
    }
}
