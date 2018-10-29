package com.example.myfirstapp.comm;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;


public class SocketClient{
    private Socket socket;
    private BufferedReader bufferedReader;

    public SocketClient(int port){
        String server_name = "PARKRPI.WV.CC.CMU.EDU";
        try
        {
            // open a socket
            socket = openSocket(server_name, port);
            
            // write-to, and read-from the socket.
            // in this case just write a simple command to a web server.
//            String result = writeToAndReadFromSocket(socket, "GET /\n\n");
            
            // print out the result we got back from the server
            //System.out.println(result);
            Log.d("socket", "connected to "+server_name);
            // close the socket, and we're done
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Open a socket connection to the given server on the given port.
     * This method currently sets the socket timeout value to 10 seconds.
     * (A second version of this method could allow the user to specify this timeout.)
     */
    private Socket openSocket(String server, int port) throws Exception
    {
        Socket socket;

        // create a socket with a timeout
        try
        {
            InetAddress inteAddress = InetAddress.getByName(server);
            SocketAddress socketAddress = new InetSocketAddress(inteAddress, port);

            // create a socket
            socket = new Socket();

            // this method will block no more than timeout ms.
            int timeoutInMs = 10*1000;   // 10 seconds
            socket.connect(socketAddress);

            return socket;
        }
        catch (SocketTimeoutException ste)
        {
            Log.d("socket err","Timed out waiting for the socket.");
            ste.printStackTrace();
            throw ste;
        }
    }

    public String writeToAndReadFromSocket(String writeTo) 
    {
        try
        {
            // write text to the socket
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(writeTo);
            bufferedWriter.flush();
            
            // read text from the socket
            if(bufferedReader == null)
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String str;
            str = bufferedReader.readLine();

            return str;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "";
        }
    }


}