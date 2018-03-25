package common;

import org.jinq.orm.stream.JinqStream;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils
{
    public static JinqStream<Component> arrToJinqStream(Component[] arr)
    {
        return JinqStream.from(new ArrayList<>(Arrays.asList(arr)));
    }

    public static Integer toInt(Double d)
    {
        return Integer.parseInt(Integer.valueOf(Utils.round(d).intValue()).toString());
    }

    public static Double round(Double d, Integer decimals)
    {
        Double pow = Math.pow(10, decimals);
        return (double)Math.round(d * pow) / pow;
    }

    public static Double round(Double d)
    {
        return round(d, 0);
    }

    public static void sizeToContent(JFrame jFrame) // content pane musi być ustawiony
    {
        jFrame.pack();
        Container contentPane = jFrame.getContentPane();
        Integer leftCornerX = Utils.toInt(Utils.arrToJinqStream(contentPane.getComponents()).sortedBy(c -> c.getBounds().getMinX()).toList().get(0).getBounds().getMinX());
        Integer leftCornerY = Utils.toInt(Utils.arrToJinqStream(contentPane.getComponents()).sortedBy(c -> c.getBounds().getMinY()).toList().get(0).getBounds().getMinY());
        Integer rightCornerX = Utils.toInt(Utils.arrToJinqStream(contentPane.getComponents()).sortedDescendingBy(c -> c.getBounds().getMaxX()).toList().get(0).getBounds().getMaxX());
        Integer rightCornerY = Utils.toInt(Utils.arrToJinqStream(contentPane.getComponents()).sortedDescendingBy(c -> c.getBounds().getMaxY()).toList().get(0).getBounds().getMaxY());

        Dimension size = new Dimension(rightCornerX + leftCornerX, rightCornerY + leftCornerY);
        contentPane.setPreferredSize(size);
        contentPane.setSize(size);

        Insets insets = jFrame.getInsets();
        Dimension jframeSize = new Dimension(rightCornerX + leftCornerX + insets.left + insets.right, rightCornerY + leftCornerY + insets.top + insets.bottom);
        jFrame.setPreferredSize(jframeSize);
        jFrame.setSize(jframeSize);
    }
}
