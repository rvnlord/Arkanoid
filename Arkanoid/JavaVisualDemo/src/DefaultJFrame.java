import org.netbeans.swing.laf.dark.DarkMetalLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DefaultJFrame extends javax.swing.JFrame
{
    public DefaultJFrame()
    {
        initComponents();
        pnlMain.setLayout(null);
        setResizable(true);
        setContentPane(pnlMain);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setPreferredSize(new Dimension(670 + 12, 646 + 12 + 30 + 12));
        getContentPane().setPreferredSize(getPreferredSize());

        setSize(964, 604);
        
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
        btnStartGame.addActionListener(this::btnStartGameActionPerformed);
        getContentPane().add(btnStartGame);
        btnStartGame.setVisible(true);
        
        pack();
        setLocationRelativeTo(null);
        setTitle("Java Demo");
        getContentPane().setBackground(Color.decode("#202020"));

        setVisible(true);
        pnlMain.setFocusable(true);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        pnlMain = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hello");
        setBackground(new java.awt.Color(51, 0, 255));
        setLocationByPlatform(true);
        setName("frmMain"); // NOI18N
        setPreferredSize(new java.awt.Dimension(664, 580));
        setResizable(false);

        pnlMain.setPreferredSize(new java.awt.Dimension(664, 504));

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 664, Short.MAX_VALUE)
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartGameActionPerformed(java.awt.event.ActionEvent evt)                                         
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
                    Pole plansza = ((Pole)comp);
                    plansza.timer.stop();
                }
                break;
            }
        }
        
        Pole p = new Pole(640, 480, pnlScores);
        p.setLayout(null);
        p.setName("pnlBoard");
        p.setLocation(12, 12);
        p.addMouseMotionListener(p);
        p.setBackground(Color.decode("#101010"));
        p.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        getContentPane().add(p);
    }    
    
    public static void main(String args[])
    {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">        
        try
        {
            UIManager.setLookAndFeel(DarkMetalLookAndFeel.class.getCanonicalName());
        }
        catch (ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException ex)
        {
            Logger.getLogger(DefaultJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */       
        EventQueue.invokeLater(() -> {
            DefaultJFrame dFrame = new DefaultJFrame();
        });
    }

    private JPanel pnlScores;
    private JButton btnStartGame;
    private JLabel lblScores;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlMain;
    // End of variables declaration//GEN-END:variables
}
