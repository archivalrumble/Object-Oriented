import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.util.Random;
import java.awt.geom.Rectangle2D;

public class BasicLivingThing extends LivingObject
{
 
    Color currentColor;

    @Override
    void draw(Graphics2D g)
    {        
  
        alpha = currentAngle;

        g.setColor(currentColor);

        
        for (int n = 0; n < 10; n++)
        {
            if(n % 2 == 0)
            {
                xCord[n] = (int)(xPosition + innerRadius * Math.cos(alpha));
                yCord[n] = (int)(yPosition + innerRadius * Math.sin(alpha));

            }
            
            else
            {
                xCord[n] = (int)(xPosition + radius * Math.cos(alpha));
                yCord[n] = (int)(yPosition + radius * Math.sin(alpha));
            }

            alpha = alpha + deltaAlpha;

            //System.out.println(xCord[n] + " " + yCord[n]);

        }

        //System.out.println(xCord + " " + yCord);

        g.drawPolygon(xCord, yCord, 10);
        g.fillPolygon(xCord, yCord, 6);

        sqrt2 = Math.sqrt(2);

        //System.out.println(xCord + " " + yCord + " " + xPosition + " " + yPosition + " " + radius + " " + " " + alpha);
        //Rectangle2D rect = new Rectangle2D.Double(xPosition,yPosition, width, height);
        //g.draw(rect);
        //g.drawRect((int)(xPosition), (int)(yPosition), width, height);
        //g.fillRect(xPosition, yPosition, width, height);
        
    }

    BasicLivingThing getRandomInstance(DrawingPanel panel)
    {       
        BasicLivingThing thing = new BasicLivingThing();
        Random rand = new Random();

        w = panel.getWidth();
        h = panel.getHeight();

        r = rand.nextInt(106) + 150;
        p = rand.nextInt(106) + 150;
        b = rand.nextInt(106) + 150;

        if(DrawFrame.randomColors.isSelected())
        {
            currentColor = new Color(r, p, b);
        }
        else if(!DrawFrame.randomColors.isSelected())
        {
            currentColor = new Color(255, 255, 255);
        }
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        lifetime = rand.nextInt(50) + DrawFrame.lifeSlider.getValue();

        xPosition = rand.nextInt((85 * w / 85) + 1);
        yPosition = rand.nextInt(((75 * h / 75)) + 1);

        do
        { 
        xSpeed = rand.nextInt( 10 ) - 5;
        ySpeed = rand.nextInt( 10 ) - 5;
        }
        while(xSpeed == 0 || ySpeed == 0);

       

        do
        {
            mouseSpeed = rand.nextDouble() * rand.nextInt(4);
        }while(mouseSpeed < .3);
        //xAcceleration = rand.nextDouble() * (rand.nextBoolean() ? -1 : 1);
        currentAngle = rand.nextInt(361); 

        xCord = new int[10];
        yCord = new int[10];

        newX = xPosition;
        newY = yPosition;
    

       
        
        if( DrawFrame.gravity.isSelected() == true)
        {
        yAcceleration += DrawFrame.gravAccel.getValue() / 10.0;
        }
        else if( DrawFrame.gravity.isSelected() == false)
        {
        yAcceleration = 0;
        }

        width = rand.nextInt((30 - 10) + 1) + 10;
        height = width;
        radius = Math.sqrt((width * width) + (height * height));
        innerRadius = radius * ( 1.0 / 3.0);

        //System.out.println(radius + " " + innerRadius);

        deltaAlpha = (Math.PI / 5);

        return thing;

    }
}
