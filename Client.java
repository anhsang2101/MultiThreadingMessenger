

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private InetAddress host;
	private int port;
	
	public Client(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}
	
	private void execute() {
		
		try {
			
			Scanner kb = new Scanner(System.in);
			System.out.println("Type name: ");
			String name = kb.nextLine();
			
			Socket client = new Socket(host, port);
			ReadClient read = new ReadClient(client);
			read.start();
			WriteClient write = new WriteClient(client, name);
			write.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public static void main(String[] args) throws UnknownHostException {
		Client c = new Client(InetAddress.getLocalHost(), 15797);
		c.execute();
	}
}

class ReadClient extends Thread{
	private Socket client;

	public ReadClient(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DataInputStream dis = null;
		
		try {
			dis = new DataInputStream(client.getInputStream());
			
			while(true) {
				String sms = dis.readUTF();
				System.out.println(sms);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
		
}

class WriteClient extends Thread{
	private Socket client;
	private String name;
	private Scanner kb = null;

	public WriteClient(Socket client, String name) {
		this.client = client;
		this.name = name;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DataOutputStream dos = null;
		kb = new Scanner(System.in);
		try {
			dos = new DataOutputStream(client.getOutputStream());
			while(true) {
				String sms = kb.nextLine();
				dos.writeUTF(name + ": " + sms);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
