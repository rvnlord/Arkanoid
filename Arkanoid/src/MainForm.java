import common.Utils;
import models.Field;
import org.netbeans.swing.laf.dark.DarkMetalLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class MainForm extends javax.swing.JFrame
{
    private JPanel pnlScores;
    private JButton btnStartGame;
    private JLabel lblScores;

    private JPanel pnlMain;

    private MainForm()
    {
        initComponents();
    }

    private void initComponents()
    {
        pnlMain = new JPanel();
        pnlMain.setLayout(null);
        setResizable(true);
        setContentPane(pnlMain);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        
        pnlScores = new JPanel();
        pnlScores.setLayout(null);
        pnlScores.setBounds(new Rectangle(12, 504, 640, 100));
        pnlScores.setBackground(Color.decode("#101010"));
        getContentPane().add(pnlScores);

        lblScores = new JLabel();
        lblScores.setLayout(null);
        lblScores.setFont(new Font("Serif", Font.PLAIN, 30));
        lblScores.setBounds(0, 0, 640, 100);
        lblScores.setText("Punkty: 0");
        lblScores.setName("lblScores");
        lblScores.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        pnlScores.add(lblScores);
        lblScores.setVisible(true);

        lblScores.setHorizontalAlignment(SwingConstants.CENTER);
        lblScores.setVerticalAlignment(SwingConstants.CENTER);

        JPanel pnlEmptyBoard = new JPanel();
        pnlEmptyBoard.setName("pnlEmptyBoard");
        pnlEmptyBoard.setLayout(null);
        pnlEmptyBoard.setBounds(12, 12, 640, 480);
        pnlEmptyBoard.setBackground(Color.decode("#101010"));
        pnlEmptyBoard.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        getContentPane().add(pnlEmptyBoard);

        btnStartGame = new JButton();
        btnStartGame.setBounds(12, 12 + pnlEmptyBoard.getHeight() + 12 + pnlScores.getHeight() + 12, 180, 30);
        btnStartGame.setBackground(Color.decode("#101010"));
        btnStartGame.setForeground(Color.decode("#FFFFFF"));
        btnStartGame.setFont(new Font("Serif", Font.PLAIN, 16));
        btnStartGame.setText("Rozpocznij Nową Grę");
        btnStartGame.addActionListener(this::btnStartGame_Click);
        getContentPane().add(btnStartGame);
        btnStartGame.setVisible(true);


        Utils.sizeToContent(this);

        setLocationByPlatform(true);
        setLocationRelativeTo(null);
        setName("frmMain");
        setTitle("Arkanoid");
        getContentPane().setBackground(Color.decode("#202020"));

        setVisible(true);
        pnlMain.setFocusable(true);
    }

    private void btnStartGame_Click(ActionEvent e)                                         
    {              
        String name;
        Component[] components = this.getContentPane().getComponents();
        for (Component comp : components)
        {
            name = comp.getName();
            if ((name != null && "pnlBoard".toLowerCase().equals(name.toLowerCase())) || (name != null && "pnlEmptyBoard".toLowerCase().equals(name.toLowerCase()))) 
            {
                lblScores.setText("Punkty: 0");
                this.remove(comp);
                if ("pnlBoard".toLowerCase().equals(name.toLowerCase()))
                {
                    Field field = ((Field)comp);
                    field.timer.stop();
                }
                break;
            }
        }
        
        Field p = new Field(640, 480, pnlScores);
        p.setLayout(null);
        p.setName("pnlBoard");
        p.setLocation(12, 12);
        p.addMouseMotionListener(p);
        p.setBackground(Color.decode("#101010"));
        p.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        getContentPane().add(p);
    }

    public static void main(String args[]) throws ClassNotFoundException, IllegalAccessException, UnsupportedLookAndFeelException, InstantiationException
    {
        UIManager.setLookAndFeel(DarkMetalLookAndFeel.class.getCanonicalName());
        new MainForm();
    }
}
