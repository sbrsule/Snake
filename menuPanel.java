import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class menuPanel extends JPanel implements ActionListener {
    JButton resume = new JButton("Resume");
    menuPanel() {
        this.setPreferredSize(new Dimension(600, 600));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.add(resume);
        resume.setBackground(Color.white);
        resume.setForeground(Color.black);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
}
