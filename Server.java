

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	
	private int port;
	public static ArrayList<Socket> listSK;

	public Server(int port) {
		this.port = port;
	}
	
	private void execute() {
		
		try {
			ServerSocket server = new ServerSocket(port);
			WriteServer write = new WriteServer();
			write.start();
			System.out.println("Server is listening...");
			while(true) {
				Socket socket = server.accept();
				System.out.println("Connected with " + socket);
				Server.listSK.add(socket);
				ReadServer read = new ReadServer(socket);
				read.start();
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void main(String[] args) {
		Server.listSK = new ArrayList<>();
		Server s = new Server(15797);
		s.execute();
	}
	
	class ReadServer extends Thread{
		private Socket socket;

		public ReadServer(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			DataInputStream dis = null;
			
			try {
				dis = new DataInputStream(socket.getInputStream());
				
				while(true) {
					String sms = dis.readUTF();
					if(sms.contains("exit")) {
						Server.listSK.remove(socket);
						dis.close();
						socket.close();	
						System.out.println("Disconnected");
						continue;
					}
					for(Socket item : Server.listSK) {
						if(item.getPort() != socket.getPort()) {
							DataOutputStream dos = new DataOutputStream(item.getOutputStream());
							dos.writeUTF(sms);
						}
					}
					System.out.println(sms);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
			
	}

	class WriteServer extends Thread{
		
		private Scanner kb = null;

		
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			DataOutputStream dos = null;
			kb = new Scanner(System.in);
			try {
				while(true) {
					String sms = kb.nextLine();
					for(Socket item : Server.listSK) {
						dos = new DataOutputStream(item.getOutputStream());
						dos.writeUTF("Server: " + sms);
					}
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
}
