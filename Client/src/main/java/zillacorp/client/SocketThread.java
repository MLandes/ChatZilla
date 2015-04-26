/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zillacorp.client;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class SocketThread implements Runnable {
    
    private Thread baseThread;
    private volatile boolean isRunning;
    
    public void start() {
        if (this.baseThread == null) {
            this.baseThread = new Thread(this);
            this.isRunning = true;
            this.baseThread.start();
        }
    }
    public void terminate() {
        if (this.baseThread != null) {
            try {
                this.isRunning = false;
                this.baseThread.join();
                // Alternative: this.baseThread.interrupt();
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketThread.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                this.baseThread = null;
            }
        }
    }
    
    @Override
    public void run() {
        while(this.isRunning) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
