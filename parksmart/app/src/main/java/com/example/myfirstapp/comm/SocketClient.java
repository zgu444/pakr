package com.example.myfirstapp.comm;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;


public class SocketClient{
    private Socket socket;
    private BufferedReader bufferedReader;

    public SocketClient(int port) throws IOException {
        String server_name = "PARKRPI.WV.CC.CMU.EDU";

        // open a socket
        socket = openSocket(server_name, port);
        if (socket == null) throw new IOException();

        // close the socket, and we're done

    }

    /**
     * Open a socket connection to the given server on the given port.
     * This method currently sets the socket timeout value to 10 seconds.
     * (A second version of this method could allow the user to specify this timeout.)
     */
    private Socket openSocket(String server, int port)
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
        catch (IOException ste)
        {
            Log.d("socket err","Timed out waiting for the socket.");
            ste.printStackTrace();
            return null;
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

    public void close() throws IOException {
        socket.close();
    }


}