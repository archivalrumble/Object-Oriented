import java.awt.*;

import javax.lang.model.util.ElementScanner6;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

public class DrawingPanel extends JPanel
{

    DrawingPanel()
    {

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        
        setPreferredSize(new Dimension(d.width, 70 * d.height/100));
        setBackground(Color.BLACK);

    }

    @Override
    protected void paintComponent(Graphics g)
    {

        Graphics2D g2D;

        if(DrawFrame.paintComponent.isSelected() == false)
        {
            super.paintComponent(g);
        }

   

        g2D = (Graphics2D) g;

        int amount = DrawFrame.livingThings.size();

        for (int i = 0; i < amount; i++)
        {
            BasicLivingThing thing = DrawFrame.livingThings.get(i);
            thing.draw(g2D);

        }

    }
}