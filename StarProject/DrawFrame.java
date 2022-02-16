import java.awt.*;
import java.awt.BorderLayout;

import javax.lang.model.type.NullType;
import javax.lang.model.util.ElementScanner6;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class DrawFrame extends JFrame implements ActionListener, ChangeListener, MortalityListener, MouseInputListener
{
  
    final Timer timer;
    int rotation;
    static double mouseX, mouseY;
    Random rand = new Random();
    static boolean mouseHeldDown = false;
    static boolean newPosReached = false;

    DrawingPanel drawPanel = new DrawingPanel();
    
    final static int DELAY = 10;

    static Vector<BasicLivingThing> livingThings = new Vector<BasicLivingThing>();

    JSlider zoom = new JSlider(1, 100, 10);
    static JSlider gravAccel = new JSlider(-20, 20, 0);
    static JSlider conserv = new JSlider(-50, 50, 0);
    static JSlider lifeSlider = new JSlider(50, 500, 100);
    JSlider multiAdd = new JSlider(2, 100, 15);

    static JCheckBox gravity = new JCheckBox("Grav");
    static JCheckBox randomColors = new JCheckBox("Random Colors");
    static JCheckBox mortality = new JCheckBox("Mortality");
    static JCheckBox paintComponent = new JCheckBox("Paint Componenet");
    static JCheckBox mouse = new JCheckBox("Mouse Chase");

    static JRadioButton idle = new JRadioButton("Idle");
    static JRadioButton backForth = new JRadioButton("Back and Forth");
    static JRadioButton randomMovement = new JRadioButton("Random Movement");
   
    static JButton clear = new JButton("Clear");
    static JButton addObject = new JButton("Add");
    static JButton addMulti = new JButton("Add Multiple");
    static JButton pauseUnpause = new JButton("Pause / Unpause");
    
    DrawFrame()
    {

        zoom.setBorder(BorderFactory.createTitledBorder("Play Speed"));
        zoom.addChangeListener(this);

        gravAccel.setBorder(BorderFactory.createTitledBorder("Gravity Strength"));
        gravAccel.addChangeListener(this);

        conserv.setBorder(BorderFactory.createTitledBorder("Conservation of Speed"));
        conserv.addChangeListener(this);

        lifeSlider.setBorder(BorderFactory.createTitledBorder("Lifetime"));
        lifeSlider.addChangeListener(this);

        multiAdd.setBorder(BorderFactory.createTitledBorder("Number of starts to add"));
        multiAdd.addChangeListener(this);

        mouse.setActionCommand("MOUSE");
        mouse.addActionListener(this);

        backForth.setActionCommand("BACK");
        backForth.addActionListener(this);
        
        randomMovement.setActionCommand("RAMO");
        randomMovement.addActionListener(this);

        idle.setActionCommand("IDLE");
        idle.addActionListener(this);
        idle.setSelected(true);
        idle.setEnabled(false);

        addMulti.setActionCommand("MULTI");
        addMulti.addActionListener(this);

        paintComponent.setActionCommand("PAINT");
        paintComponent.addActionListener(this);

        gravity.setActionCommand("GRAV");
        gravity.addActionListener(this);
       
        randomColors.setActionCommand("RAND");
        randomColors.addActionListener(this);
        randomColors.setSelected(true);

        mortality.setActionCommand("MORT");
        mortality.addActionListener(this);
        
        addObject.setActionCommand("ADD");
        addObject.addActionListener(this);

        clear.setActionCommand("CLEAR");
        clear.addActionListener(this);

        pauseUnpause.setActionCommand("PAUSE");
        pauseUnpause.addActionListener(this);

        drawPanel.addMouseListener(this);
        drawPanel.addMouseMotionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addObject);
        buttonPanel.add(addMulti);
        buttonPanel.add(multiAdd);
        buttonPanel.add(clear);
        buttonPanel.add(zoom);
        buttonPanel.add(pauseUnpause);
        
        
        JPanel topPanel = new JPanel();
        topPanel.add(idle);
        topPanel.add(randomMovement);
        topPanel.add(backForth);
        topPanel.add(mouse);
        topPanel.add(gravity);
        topPanel.add(gravAccel);
        topPanel.add(conserv);
        topPanel.add(mortality);
        topPanel.add(lifeSlider);
        topPanel.add(randomColors);
        topPanel.add(paintComponent);
       

        add(drawPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        repaint();

        setUpMainFrame();

        BasicLivingThing newObject = new BasicLivingThing();
        newObject.getRandomInstance(drawPanel);
        newObject.addMortalityListener(this);
        livingThings.add(newObject);

        mouseX = drawPanel.getWidth() / 2.0;
        mouseY = drawPanel.getHeight() / 2.0;

        timer = new Timer(DELAY, new ActionListener()
        {
            @Override
            public void actionPerformed(final ActionEvent e)
            {

                drawPanel.repaint();
                

                int amount = livingThings.size();

                for (int i = 0; i < amount; i++)
                {
                    BasicLivingThing thing = livingThings.get(i);
                    thing.update(drawPanel, i);
                    thing.deltaScaledMillis = (zoom.getValue() / 10.0);

                    if(livingThings.size() < amount)
                    {
                        amount -= 1;
                    }
                }
             
            }
        });

        timer.start();
        
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("ADD"))
        {
            BasicLivingThing newObject = new BasicLivingThing();
            newObject.getRandomInstance(drawPanel);
            newObject.addMortalityListener(this);
            livingThings.add(newObject);
        }

        if(e.getActionCommand().equals("MULTI"))
        {
            for(int i = 0; i < multiAdd.getValue(); i++)
            {
                BasicLivingThing newObject = new BasicLivingThing();
                newObject.getRandomInstance(drawPanel);
                newObject.addMortalityListener(this);
                livingThings.add(newObject);
            }
        }

        if(e.getActionCommand().equals("CLEAR"))
        {
            int amount = livingThings.size();

            for (int i = 0; i < amount; i++)
            {
               livingThings.clear();
            }
        }

        if(e.getActionCommand().equals("PAUSE"))
        {
            if(timer.isRunning())
            {
                timer.stop();
            }
            else
            {
                timer.start();
            }
        }

        if(e.getActionCommand().equals("BACK"))
        {
            if(backForth.isEnabled() == true)
            {
                idle.setEnabled(true);
                idle.setSelected(false);
                backForth.setEnabled(false);
                backForth.setSelected(true);
                randomMovement.setEnabled(true);
                randomMovement.setSelected(false);

                int amount = livingThings.size();

                for(int i = 0; i < amount; i++)
                {
                    BasicLivingThing thing = livingThings.get(i);

                    do
                    { 
                        thing.xSpeed = rand.nextInt( 10 ) - 5;
                        thing.ySpeed = rand.nextInt( 10 ) - 5;
                        
                        if(gravity.isSelected() == true)
                        {
                            thing.yAcceleration = gravAccel.getValue() / 10.0;
                        }   
        
                        else if(gravity.isSelected() == false)
                        {
                            thing.yAcceleration = 0;
                        }
                    }
                    while(thing.xSpeed == 0 || thing.ySpeed == 0);
                }
            }
        }

        if(e.getActionCommand().equals("RAMO"))
        {
            if(randomMovement.isEnabled() == true)
            {
                backForth.setEnabled(true);
                backForth.setSelected(false);
                idle.setEnabled(true);
                idle.setSelected(false);
                randomMovement.setEnabled(false);
                randomMovement.setSelected(true);


                int amount = livingThings.size();

                for(int i = 0; i < amount; i++)
                {
                    BasicLivingThing thing = livingThings.get(i);

                    do
                    { 
                        thing.xSpeed = rand.nextInt( 10 ) - 5;
                        thing.ySpeed = rand.nextInt( 10 ) - 5;

                        if(gravity.isSelected() == true)
                        {
                            thing.yAcceleration = gravAccel.getValue() / 10.0;
                        }   
        
                        else if(gravity.isSelected() == false)
                        {
                            thing.yAcceleration = 0;
                        }
                    }
                    while(thing.xSpeed == 0 || thing.ySpeed == 0);
                }
            }
        }

        if(e.getActionCommand().equals("IDLE"))
        {
            if(idle.isEnabled() == true)
            {
                backForth.setEnabled(true);
                backForth.setSelected(false);
                idle.setEnabled(false);
                idle.setSelected(true);
                randomMovement.setEnabled(true);
                randomMovement.setSelected(false);
                
                int amount = livingThings.size();

                for (int i = 0; i < amount; i++)
                {
                    BasicLivingThing thing = livingThings.get(i);

                    thing.newX = thing.xPosition;
                    thing.newY = thing.yPosition;
                }

            }
        }

        if(e.getActionCommand().equals("GRAV"))
        {
            int amount = livingThings.size();

            for (int i = 0; i < amount; i++)
            {
                BasicLivingThing thing = livingThings.get(i);

                if(gravity.isSelected() == true)
                {
                    thing.yAcceleration = gravAccel.getValue() / 10.0;
                }   

                else if(gravity.isSelected() == false)
                {
                    thing.yAcceleration = 0;
                }
            }
        }

        if(e.getActionCommand().equals("RAND"))
        {
            for(int i = 0; i < livingThings.size(); i++)
            {
                BasicLivingThing thing = livingThings.get(i);
                    
                if(DrawFrame.randomColors.isSelected())
                {
                    thing.currentColor = new Color(thing.r, thing.p, thing.b);
                }
                else if(!DrawFrame.randomColors.isSelected())
                {
                    thing.currentColor = new Color(255, 255, 255);
                }
            }
        }

    


        
    }

    public void setUpMainFrame()
    {
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension d = (tk.getScreenSize());

    setSize(d.width/2, d.height/2);
    setLocation(d.width/4, d.height/4);

    setTitle("LavaLamp.gif");

    this.setExtendedState(MAXIMIZED_BOTH);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setVisible(true);
    }

    public Vector<BasicLivingThing> copyVector()
    {
        return livingThings;
    }

    @Override
    public void stateChanged(ChangeEvent e) 
    {
        int amount = livingThings.size();

       

        for (int i = 0; i < amount; i++)
        {
            BasicLivingThing thing = livingThings.get(i);
            thing.deltaScaledMillis = (zoom.getValue() / 10.0);
        }

        for (int i = 0; i < amount; i++)
        {
            BasicLivingThing thing = livingThings.get(i);
            thing.energyConservation = conserv.getValue();
        }

        for (int i = 0; i < amount; i++)
        {
            BasicLivingThing thing = livingThings.get(i);

            if(gravity.isSelected() == true)
            {
                thing.yAcceleration = gravAccel.getValue() / 10.0;
            }   

            else if(gravity.isSelected() == false)
            {
                thing.yAcceleration = 0;
            }
        }
     
        
    }

    @Override
    public void onMortalityEvent(MortalityEvent e) 
    {
        if (e.isAlive == true)
        {
            for(int i = 0; i < livingThings.size(); i++)
            {
                if(livingThings.elementAt(i) == e.getSrc())
                {
                    BasicLivingThing thing = livingThings.get(i);
                    
                    if(DrawFrame.randomColors.isSelected())
                    {
                        thing.currentColor = new Color(thing.r, thing.p, thing.b, (int)((thing.lifetime * 256) / 50));
                    }
                    else if(!DrawFrame.randomColors.isSelected())
                    {
                        thing.currentColor = new Color(255, 255, 255, (int)((thing.lifetime * 256) / 50));
                    }


                }
            }
        }
        if (e.isDeathEvent == true)
        {
            for(int i = 0; i < livingThings.size(); i++)
            {
                if(livingThings.elementAt(i) == e.getSrc())
                {
                    //System.out.println(livingThings.elementAt(i) + " " + e.getSrc());
                    livingThings.removeElementAt(i);
                }
            }
            
        }
        
    }

    @Override
    public void mouseClicked(MouseEvent e) 
    {
        
        
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
        mouseHeldDown = true;

        if(mouse.isSelected())
        {
            mouseX = e.getX();
            mouseY = e.getY();  
            
            int amount = livingThings.size();

            for (int i = 0; i < amount; i++)
            {
                BasicLivingThing thing = livingThings.get(i);

    
                thing.newX = mouseX;
                thing.newY = mouseY;
               
                if(((thing.xPosition + thing.yPosition)) > mouseX + mouseY 
                || ((thing.xPosition + thing.yPosition)) < mouseX + mouseY)
                    {
                        thing.xSpeed = (((mouseX - thing.xPosition)) * .025);
                 
                        thing.ySpeed = (((mouseY - thing.yPosition)) * .025);
                 
                    }

                  

            }
        }
            
    }

    @Override
    public void mouseReleased(MouseEvent e) 
    {

        mouseHeldDown = false;

        if(mouse.isSelected())
        {
            mouseX = e.getX();
            mouseY = e.getY();  
            
            int amount = livingThings.size();

            for (int i = 0; i < amount; i++)
            {
                BasicLivingThing thing = livingThings.get(i);

                do
                { 
                    thing.newX = mouseX + (rand.nextInt((int)(thing.radius * 5)) * (rand.nextBoolean()? 1 : -1));
                    thing.newY = mouseY + (rand.nextInt((int)(thing.radius * 5)) * (rand.nextBoolean()? 1 : -1));
                }while(thing.newX == 0 || thing.newY == 0);
               
                    
                    if(idle.isSelected() == false)
                    {
                        if((thing.xSpeed <= .5 && thing.xSpeed >= -.5) 
                        || (thing.ySpeed <= .5 && thing.ySpeed >= -.5))
                        {
                            do
                            { 
                            thing.xSpeed = rand.nextInt( 10 ) - 5;
                            thing.ySpeed = rand.nextInt( 10 ) - 5;
                            }while(thing.xSpeed == 0 || thing.ySpeed == 0);
                        }
                    } 
                    else if(idle.isSelected() == true && newPosReached == false)
                    {
                        if(thing.xPosition != thing.newX && thing.yPosition != thing.newY)
                        {
                            DrawFrame.newPosReached = false;
                            thing.xAcceleration = 0;
                            thing.yAcceleration = 0;
                            thing.xSpeed = ((thing.newX - thing.xPosition) * .05);
                            thing.ySpeed = ((thing.newY - thing.yPosition) * .05);
                        }
                    }

            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) 
    {
        //mouseX = e.getX();
        //mouseY = e.getY();
        
        
    }

    @Override
    public void mouseExited(MouseEvent e) 
    {
        //mouseX = e.getX();
        //mouseY = e.getY();
        
        
    }

    @Override
    public void mouseDragged(MouseEvent e) 
    {
        if(mouse.isSelected())
        {
            mouseX = e.getX();
            mouseY = e.getY();

            int amount = livingThings.size();

            for (int i = 0; i < amount; i++)
            {
                BasicLivingThing thing = livingThings.get(i);

                thing.newX = mouseX;
                thing.newY = mouseY;


                if(((mouseX + mouseY) - (thing.xPosition + thing.yPosition)) > thing.radius * 3.5 
                || ((mouseX + mouseY) - (thing.xPosition + thing.yPosition)) < -thing.radius * 3.5)

                    {
                        thing.xSpeed = (((mouseX - thing.xPosition)) * .05);
        
                        thing.ySpeed = ((mouseY - thing.yPosition) * .05);

                    }

            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) 
    {
        //mouseX = e.getX();
        //mouseY = e.getY();
        
    }
}
