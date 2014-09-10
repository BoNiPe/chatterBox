/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import client.Client;
import client.EchoListener;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import server.Server;

/**
 *
 * @author Desting
 */
public class TestSeveralClients {

    public TestSeveralClients() {
    }

    String msg;
    String msg2;
    String msg3;
    String msg4;
    Client boyko = new Client();
    Client nik = new Client();
    Client peter = new Client();
    Client superman = new Client();

    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Server.main(null);
            }
        }).start();

    }

    @AfterClass
    public static void tearDownClass() {
        Server.stopServer();
    }

    @Before
    public void setUp() throws IOException, InterruptedException {

//        EchoListener listen1 = new EchoListener() {
//            @Override
//            public void messageArrived(String data) {
//                System.out.println(data);
//            }
//        };
//        client1.registerEchoListener(listen1);
        boyko.connect("localhost", 9090);
        boyko.send("CONNECT#Boyko");
        Thread.sleep(10);

//        EchoListener listen2 = new EchoListener() {
//            @Override
//            public void messageArrived(String data) {
//                System.out.println(data);
//            }
//        };
//        client2.registerEchoListener(listen2);
        nik.connect("localhost", 9090);
        nik.send("CONNECT#Nik");
        Thread.sleep(10);

//        EchoListener listen3 = new EchoListener() {
//            @Override
//            public void messageArrived(String data) {
//                System.out.println(data);
//            }
//        };
//        client3.registerEchoListener(listen3);
        peter.connect("localhost", 9090);
        peter.send("CONNECT#Peter");
        Thread.sleep(10);
    }
    
    @After
    public void tearDown() {
        boyko.send("CLOSE#");
        nik.send("CLOSE#");
        peter.send("CLOSE#");
        superman.send("CLOSE#");
    }
    

    //-----------------CONNECT-----------------
    @Test
    public void connect() throws IOException, InterruptedException {

        EchoListener listen4 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg = data;
            }
        };
        superman.registerEchoListener(listen4);
        superman.connect("localhost", 9090);
        superman.send("CONNECT#Superman");
        Thread.sleep(10);

        assertEquals("ONLINE#Boyko,Nik,Peter,Superman", msg);
    }

    //-----------------SEND-----------------
    @Test
    public void sendPrivate() throws IOException, InterruptedException {

        EchoListener listen4 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg = data;
            }
        };

        superman.registerEchoListener(listen4);
        superman.connect("localhost", 9090);
        superman.send("CONNECT#Superman");
        Thread.sleep(10);

        boyko.send("SEND#Superman#Wazap");
        Thread.sleep(10);
        assertEquals("MESSAGE#Boyko#Wazap", msg);

    }

    public void sendToSeveral() throws IOException, InterruptedException {
        //Setting up listeners to test who recieves the message
        EchoListener listen1 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg=data;
            }
        };
        boyko.registerEchoListener(listen1);
        
        EchoListener listen2 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg2=data;
            }
        };
        nik.registerEchoListener(listen2);
        
        EchoListener listen3 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg3=data;
            }
        };
        peter.registerEchoListener(listen3);
        
        //Connecting the Superman
        superman.connect("localhost", 9090);
        superman.send("CONNECT#Superman");
        Thread.sleep(10);
        
        //Sending the message to Boyko and Nik
        superman.send("SEND#Boyko,Nik#Hey dudes");
        
        assertEquals("MESSAGE#Superman#Hey dudes", msg);
        assertEquals("MESSAGE#Superman#Hey dudes", msg2);
        assertEquals("", msg3);
    }

    public void sendToAll() throws IOException, InterruptedException {
//Setting up listeners to test who recieves the message
        EchoListener listen1 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg=data;
            }
        };
        boyko.registerEchoListener(listen1);
        
        EchoListener listen2 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg2=data;
            }
        };
        nik.registerEchoListener(listen2);
        
        EchoListener listen3 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg3=data;
            }
        };
        peter.registerEchoListener(listen3);
        
        //Connecting the Superman
        superman.connect("localhost", 9090);
        superman.send("CONNECT#Superman");
        Thread.sleep(10);
        
        //Sending the message to Boyko and Nik
        superman.send("SEND#*#Hey dudes");
        
        assertEquals("MESSAGE#Superman#Hey dudes", msg);
        assertEquals("MESSAGE#Superman#Hey dudes", msg2);
        assertEquals("MESSAGE#Superman#Hey dudes", msg3);
    }

    @Test
    public void sendNonsense() throws IOException, InterruptedException {

        EchoListener listen4 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg = data;
            }
        };

        superman.registerEchoListener(listen4);
        superman.connect("localhost", 9090);
        superman.send("CONNECT#Superman");
        Thread.sleep(10);
        msg = "hej";
        superman.send("lolfag");
        Thread.sleep(10);

        assertFalse(msg=="hej");
        assertEquals("", msg);

    }

    //-----------------CLOSE-----------------
    @Test
    public void close() throws InterruptedException {
        EchoListener listen4 = new EchoListener() {
            @Override
            public void messageArrived(String data) {
                System.out.println(data);
                msg = data;
            }
        };

        boyko.registerEchoListener(listen4);
        boyko.send("CLOSE#");
        Thread.sleep(10);
        assertEquals("CLOSE#", msg);

    }

}