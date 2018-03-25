package models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BallEngine implements ActionListener
{
    Ball a;

    public BallEngine(Ball a)
    {
        this.a = a;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        a.nextStep();
    }
}
