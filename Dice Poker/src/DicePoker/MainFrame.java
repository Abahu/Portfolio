/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DicePoker;

/**
 *
 * @author DM
 */
public class MainFrame extends javax.swing.JFrame {
    private DiceButton[] buttonArray;
    
    byte stage = 0;
    
    Score.result p1Score;
    Score.result p2Score;

    public MainFrame() {
        initComponents();
        customInitComponents();
    }
    
    public MainFrame(Dice6[] dice)
    {
        initComponents();
        customInitComponents();
        if (dice.length == 10)
        {
            for (int i = 0; i < 10; i++)
                buttonArray[i].setDieContext(dice[i]);
        }
    }
    
    public void setRollEnabled(boolean val)
    {
        rollButton.setEnabled(val);
    }
    
    public void setDieEnabled(boolean val, int index)
    {
        buttonArray[index].setClickable(val);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tBtn1 = new DiceButton();
        tBtn2 = new DiceButton();
        tBtn3 = new DiceButton();
        tBtn4 = new DiceButton();
        tBtn5 = new DiceButton();
        rollButton = new javax.swing.JButton();
        tBtn6 = new DiceButton();
        tBtn7 = new DiceButton();
        tBtn8 = new DiceButton();
        tBtn9 = new DiceButton();
        tBtn10 = new DiceButton();
        p1ScoreText = new javax.swing.JTextField();
        p2ScoreText = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBtn1ActionPerformed(evt);
            }
        });

        tBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBtn2ActionPerformed(evt);
            }
        });

        tBtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBtn3ActionPerformed(evt);
            }
        });

        tBtn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBtn4ActionPerformed(evt);
            }
        });

        tBtn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBtn5ActionPerformed(evt);
            }
        });

        rollButton.setText("Roll");
        rollButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rollButtonActionPerformed(evt);
            }
        });

        tBtn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBtn6ActionPerformed(evt);
            }
        });

        tBtn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBtn7ActionPerformed(evt);
            }
        });

        tBtn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBtn8ActionPerformed(evt);
            }
        });

        tBtn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBtn9ActionPerformed(evt);
            }
        });

        tBtn10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBtn10ActionPerformed(evt);
            }
        });

        p1ScoreText.setEditable(false);
        p1ScoreText.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        p2ScoreText.setEditable(false);
        p2ScoreText.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(p1ScoreText)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tBtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tBtn6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tBtn7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rollButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tBtn8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tBtn9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tBtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tBtn10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(p2ScoreText, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p1ScoreText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBtn3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBtn2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBtn5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBtn4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rollButton)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tBtn6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tBtn9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tBtn7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBtn8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBtn10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(p2ScoreText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // DICE CODE
    private void tBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBtn1ActionPerformed
        tBtn1.toggleClicked();
        repaint();
    }//GEN-LAST:event_tBtn1ActionPerformed

    private void tBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBtn2ActionPerformed
        tBtn2.toggleClicked();
        repaint();
    }//GEN-LAST:event_tBtn2ActionPerformed

    private void tBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBtn3ActionPerformed
        tBtn3.toggleClicked();
        repaint();
    }//GEN-LAST:event_tBtn3ActionPerformed

    private void tBtn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBtn4ActionPerformed
        tBtn4.toggleClicked();
        repaint();
    }//GEN-LAST:event_tBtn4ActionPerformed

    private void tBtn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBtn5ActionPerformed
        tBtn5.toggleClicked();
        repaint();
    }//GEN-LAST:event_tBtn5ActionPerformed

    private void rollButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rollButtonActionPerformed
        Entry.theGame.rollDice();
    }//GEN-LAST:event_rollButtonActionPerformed

    private void tBtn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBtn6ActionPerformed
        tBtn6.toggleClicked();
        repaint();
    }//GEN-LAST:event_tBtn6ActionPerformed

    private void tBtn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBtn7ActionPerformed
        tBtn7.toggleClicked();
        repaint();
    }//GEN-LAST:event_tBtn7ActionPerformed

    private void tBtn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBtn8ActionPerformed
        tBtn8.toggleClicked();
        repaint();
    }//GEN-LAST:event_tBtn8ActionPerformed

    private void tBtn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBtn9ActionPerformed
        tBtn9.toggleClicked();
        repaint();
    }//GEN-LAST:event_tBtn9ActionPerformed

    private void tBtn10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBtn10ActionPerformed
        tBtn10.toggleClicked();
        repaint();
    }//GEN-LAST:event_tBtn10ActionPerformed

    private void customInitComponents() {
        buttonArray = new DiceButton[] {
        tBtn1,
        tBtn2,
        tBtn3,
        tBtn4,
        tBtn5,
        tBtn6,
        tBtn7,
        tBtn8,
        tBtn9,
        tBtn10};
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    
    public void setP1ScoreText(String s)
    {
        p1ScoreText.setText(s);
    }
    
    public void setP2ScoreText(String s)
    {
        p2ScoreText.setText(s);
    }
    
    public DiceButton[] getButtonArray()
    {
        return buttonArray;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField p1ScoreText;
    private javax.swing.JTextField p2ScoreText;
    private javax.swing.JButton rollButton;
    private DiceButton tBtn1;
    private DiceButton tBtn2;
    private DiceButton tBtn3;
    private DiceButton tBtn4;
    private DiceButton tBtn5;
	private DiceButton tBtn6;
    private DiceButton tBtn7;
    private DiceButton tBtn8;
    private DiceButton tBtn9;
    private DiceButton tBtn10;
    // End of variables declaration//GEN-END:variables
	
    /*
    private javax.swing.JButton rollButton;
    private DiceButton tBtn1;
    private DiceButton tBtn2;
    private DiceButton tBtn3;
    private DiceButton tBtn4;
    private DiceButton tBtn5;
    private DiceButton tBtn6;
    private DiceButton tBtn7;
    private DiceButton tBtn8;
    private DiceButton tBtn9;
    private DiceButton tBtn10;
	
	tBtn1 = new DiceButton();
	tBtn2 = new DiceButton();
	tBtn3 = new DiceButton();
	tBtn4 = new DiceButton();
	tBtn5 = new DiceButton();
    */
}
