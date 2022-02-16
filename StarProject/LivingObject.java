import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

public abstract class LivingObject
{
    //Data Members
    double currentPos;
    double angularVelocity;
    double xAcceleration;
    double yAcceleration;
    double lifetime;
    double currentAngle;
    double xSpeed;
    double ySpeed;
    double mouseSpeed;
    double angularSpeed = (2.0 * Math.PI / 100.0);
    double energyConservation = DrawFrame.conserv.getValue() / 10.0;

    double deltaScaledMillis;
    double xPosition;
    double yPosition;
    double newX;
    double newY;
    double width;
    double height;
    double radius;
    double innerRadius;
    double sqrt2;
    int[] xCord;
    int[] yCord;
    double alpha;
    double deltaAlpha;
    int r;
    int p;
    int b;

    Vector<MortalityListener> mortListeners = new Vector<MortalityListener>();

    int w, h;
    int i;
    
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension d = tk.getScreenSize();

    static int timeScale;
  
    //Concrete Methods
    void update(DrawingPanel panel, int i)
    {
        w = panel.getWidth();
        h = panel.getHeight();
        updateCurrentPos(deltaScaledMillis);
        reflectOffVerticalWall();
        reflectOffHorizontalWall();
        updateLinearVelocity(deltaScaledMillis);
        updateAngularVelocity(deltaScaledMillis);
        updateCurrentOrientation(deltaScaledMillis);

        if(DrawFrame.mortality.isSelected())
        {
            updateLife(deltaScaledMillis);
        }
    }

    void updateCurrentPos(double deltaScaledMillis)
    {  

        if(xSpeed > 0)
        {
            xPosition = (xPosition + deltaScaledMillis * xSpeed);
        }

        else if(xSpeed < 0)
        {
            xPosition = (xPosition + deltaScaledMillis * xSpeed);
        }

        yPosition = (yPosition + deltaScaledMillis * ySpeed);

    }

    void updateLinearVelocity(double deltaScaledMillis)
    {
        if(DrawFrame.backForth.isSelected() == true && DrawFrame.mouseHeldDown != true)
        {
            if(xSpeed > 0)
            {
                xAcceleration = .05;
                xSpeed = xSpeed + (deltaScaledMillis * xAcceleration);
            }
            else if(xSpeed < 0)
            {
                xAcceleration = .05;
                xSpeed = (xSpeed - (deltaScaledMillis * xAcceleration));
            }
            ySpeed = ySpeed + (deltaScaledMillis * yAcceleration);
        }
        else if(DrawFrame.idle.isSelected() && DrawFrame.mouseHeldDown != true)
        {
            xSpeed = 0;
            ySpeed = 0;
        }
        else if(DrawFrame.randomMovement.isSelected() && DrawFrame.mouseHeldDown != true)
        {
            ySpeed = ySpeed + (deltaScaledMillis * yAcceleration);
        }

        if(DrawFrame.mouse.isSelected())
        {

            if(DrawFrame.gravity.isSelected())
            {
               
                if(DrawFrame.gravity.isSelected() == true)
                {
                    yAcceleration = DrawFrame.gravAccel.getValue() / 10.0;
                }   

                else if(DrawFrame.gravity.isSelected() == false)
                {
                    yAcceleration = 0;
                }
                
            }
            
            if(DrawFrame.mouseHeldDown == true)
            {
                if(((xPosition + yPosition)) > newX + newY 
                || ((xPosition + yPosition)) < newX + newY)
                {
                    xSpeed = (((newX - xPosition)) * .05);
                 
                    ySpeed = (((newY - yPosition)) * .05);
                 
                }
                else
                {
                    xSpeed = 0;
                    ySpeed = 0;
                }
   
            }
            else if(DrawFrame.mouseHeldDown == false && DrawFrame.newPosReached == false && DrawFrame.idle.isSelected() == true)
            {
                if(((xPosition + yPosition)) > newX + newY 
                || ((xPosition + yPosition)) < newX + newY)
                {
                    xSpeed = ((newX - xPosition) * .05);
                 
                    ySpeed = ((newY - yPosition) * .05);
                 
                }
            }
            

           

            
        }
       
       
    }

    void updateAngularVelocity(double deltaScaledMillis)
    {
        if(DrawFrame.backForth.isSelected() == true)
        {
            if(xSpeed > 0)
            {
                angularVelocity = (2.0 * Math.PI / 10000.0);
                angularSpeed = angularSpeed + deltaScaledMillis * angularVelocity;
            }

            else if(xSpeed < 0)
            {
                angularVelocity = -(2.0 * Math.PI / 10000.0);
                angularSpeed = angularSpeed - deltaScaledMillis * angularVelocity;
            }
        }
    }

    void updateCurrentOrientation(double deltaScaledMillis)
    {
        if(DrawFrame.idle.isSelected() == true)
        {
            if (currentAngle >= 150)
            {
                currentAngle = currentAngle + deltaScaledMillis * angularSpeed;
            }

            else if (currentAngle < 150)
            {
                currentAngle = currentAngle - deltaScaledMillis * angularSpeed;
            }
        }
        else 
        {
            if(xSpeed == 0)
            {
                if (currentAngle >= 150)
                {
                    currentAngle = currentAngle + deltaScaledMillis * angularSpeed;
                }
    
                else if (currentAngle < 150)
                {
                    currentAngle = currentAngle - deltaScaledMillis * angularSpeed;
                }
            }
            else
            {
                if (xSpeed > 0)
                {
                    currentAngle = currentAngle + deltaScaledMillis * angularSpeed;
                }

                else if (xSpeed < 0)
                {
                    currentAngle = currentAngle - deltaScaledMillis * angularSpeed;
                }
            }
        }

    }

    void reflectOffVerticalWall()
    {
        if(DrawFrame.backForth.isSelected() == true)
        {
            if(xPosition >= (w - radius) || xPosition <= 0 + radius)
            {
                if(xPosition >= (w - radius))
                {
                    xPosition = (w - radius );
                    xSpeed = -1;
                    xAcceleration = 0.0;
                    angularSpeed = 0.0;
                    angularVelocity = 0.0;
                }

                else if(xPosition <= 0 + radius)
                {
                    xPosition = 0 + radius;
                    xSpeed = 1;
                    xAcceleration = 0.0;
                    angularSpeed = 0.0;
                    angularVelocity = 0.0;
                }
                
            // xSpeed *= -1;
            }
        }

        else
        {
            if(xPosition >= (w - radius) || xPosition <= 0 + radius)
            {
                if(xPosition >= (w - radius))
                {
                    xPosition = (w - radius);
                }

                else if(xPosition <= 0 + radius)
                {
                    xPosition = 0 + radius;
                }
                xSpeed *= -1;
            }
        }
    }

    void reflectOffHorizontalWall()
    {
        if(yPosition >= (h  - radius) || yPosition <= 0 + radius)
        {
            
            if(yPosition >= (h  - radius))
            {
                if(DrawFrame.gravity.isSelected())
                {
                    bounce();
                }
                yPosition = (h - radius -1);
            }
            else if(yPosition <= 0 + radius)
            {
                if(DrawFrame.gravity.isSelected())
                {
                    bounce();
                }
                yPosition =  radius + 1;
            }

            ySpeed *= -1;
        }
    }

    void bounce()
    {
    
        if(yPosition >= (h  - radius))
        {
            ySpeed = ySpeed - energyConservation;

            if(ySpeed < 0)
            {
                ySpeed = 0;
            }
        }
        else if(yPosition <= (radius))
        {
            ySpeed = ySpeed + energyConservation;
            if(ySpeed > 0)
            {
                ySpeed = 0;
            }
        }
      
    }

    void updateLife(double deltaScaledMillis)
    {
        lifetime = lifetime - (1 * deltaScaledMillis);

    
        if(lifetime <= 0)
        {  
            MortalityEvent newMort = new MortalityEvent(this, true, false);

            for(int i = 0; i < mortListeners.size(); i++)
            {
                mortListeners.get(i).onMortalityEvent(newMort);
            }
        }
        if(lifetime > 0 && lifetime <= 30)
        {
            MortalityEvent newMort = new MortalityEvent(this, false, true);

            for(int i = 0; i < mortListeners.size(); i++)
            {
                mortListeners.get(i).onMortalityEvent(newMort);
            }
        }
    }

    void addMortalityListener(MortalityListener e)
    {
        mortListeners.add(e);
    }

    //Abstract Methods
    abstract void draw(Graphics2D g);
  
}