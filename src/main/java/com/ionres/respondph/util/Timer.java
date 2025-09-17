package com.ionres.respondph.util;

import com.ionres.respondph.main.MainFrame;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer{
    MainFrame mf;
    DateFormat df = new SimpleDateFormat("dd/MM/yy");
    DateFormat tf = new SimpleDateFormat("hh:mm:ss a");
    
    public Timer(MainFrame mf)
    {
        this.mf = mf;
        setTime();
    }

    public void setTime()
    {  
        Worker worker = new Worker();
        worker.start();
    }
    
    class Worker extends Thread 
    {
        @Override
        public void run() {
            while(true)
            {
                Date dateobj = new Date();
                mf.dateLabel.setText(df.format(dateobj));
                mf.realTimeLabel.setText(tf.format(dateobj));
                
                // Sleep for a while
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Interrupted exception will occur if
                    // the Worker object's interrupt() method
                    // is called. interrupt() is inherited
                    // from the Thread class.
                    break;
                }
            }
        }
    }
}
