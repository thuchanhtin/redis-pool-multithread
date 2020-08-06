/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redis.demo;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class ThreadTest {

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            Runnable thread = new RunnableMultiTask();
            new Thread(thread).start();
        }
    }
}

class RunnableSingleTask implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + ": " + 1001);
            RedisTest.execute(1001);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RunnableSingleTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

class RunnableMultiTask implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i <= 4; i++) {
            try {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                RedisTest.execute(i);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(RunnableMultiTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
