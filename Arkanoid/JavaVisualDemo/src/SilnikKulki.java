import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;
import static java.lang.Thread.*;

//class SilnikKulki extends Thread
//{
//    Kulka a;
//
//    SilnikKulki(Kulka a)
//    {
//        this.a = a;
//        start();
//    }
//
//    public void run()
//    {
//        try
//        {
//            while (true)
//            {
//                a.nextKrok();
//                sleep(15);
//            }
//        }
//        catch (InterruptedException e)
//        {
//        }
//    }
//}

public class SilnikKulki implements ActionListener
{
    Kulka a;

    public SilnikKulki(Kulka a)
    {
        this.a = a;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        a.nextKrok();
    }
}
