public class MortalityEvent 
{
    LivingObject src;
    boolean isDeathEvent;
    boolean isAlive;


    MortalityEvent(LivingObject src, boolean isDeathEvent, boolean isAlive)
    {
    
        this.isDeathEvent = isDeathEvent;
        this.src = src;
        this.isAlive = isAlive;
      
    }


   LivingObject getSrc()
    {
        return src;
    }

}
