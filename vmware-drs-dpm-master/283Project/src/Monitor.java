import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.Scanner;


public class Monitor 
{

	public static void main(String[] args) 
	{
		System.out.println("Options: ");
		boolean f= true;
		while(f)
		{
			System.out.println("1: Start VM Logs\n2: Stop VM Logs\n3:Start Host Logs\n4:Stop Host Logs\n"
					+ "5:Exit");
			System.out.println("Enter your choice:");
			Scanner console = new Scanner(System.in);
			int number = console.nextInt();
			System.out.println("You chose : "+ number);
			
			switch(number){
			case 1: System.out.println("Starting VM log collection..");
					runvm();
					break;
			case 2: System.out.println("Stopping VM Logs...");
			    	stopvm();
			    	break;
			case 3: System.out.println("Starting Host log collection..");
					runhost();
					break; 
			case 4: System.out.println("Stopping Host Logs...");
	    			stophost();
	    			break;		
			case 5: f=false;
					break;
			
			default: System.out.println("Wrong choice");	break;
					
			}
				
				
		}
		
		
	}
	
	static void session()
	{
		
	}
	
	static void stophost()
	{

		String user = "administrator";
	    String password = "12!@qwQW";
	    String host = "130.65.133.244"; //ip of fresh-build-vm
	    int port=22;

	 
	    
	    try
	    {
	        JSch jsch = new JSch();
	        Session session = jsch.getSession(user, host, port);
	            session.setPassword(password);
	            session.setConfig("StrictHostKeyChecking", "no");
	        System.out.println("Establishing Connection...");
	        session.connect();
	            System.out.println("Connection established.");
	           
	           Channel channel = session.openChannel("exec");
	           ((ChannelExec)channel).setCommand("/var/tmp/terminateHostStats.sh");
	           channel.setInputStream(null);
	           ((ChannelExec)channel).setErrStream(System.err);
	           
	           InputStream in=channel.getInputStream();
	      
	           channel.connect();
	           byte[] tmp=new byte[1024];
	           while(true)
	           {
	               while(in.available()>0)
	               {
	                 int i=in.read(tmp, 0, 1024);
	                 if(i<0)break;
	                 System.out.print(new String(tmp, 0, i));
	               }
	               if(channel.isClosed())
	               {
	                 if(in.available()>0) continue; 
	                 System.out.println("exit-status: "+channel.getExitStatus());
	                 break;
	               }
	               
	               try
	               {
	            	   Thread.sleep(1000);
	               }
	               catch(Exception ee)
	               {
	            	   
	               }
	           }
	             channel.disconnect();
	             session.disconnect();
	    
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	
	}
	
	static void stopvm()
	{
		String user = "administrator";
	    String password = "12!@qwQW";
	    String host = "130.65.133.244"; //ip of fresh-build-vm
	    int port=22;

	 
	    
	    try
	    {
	        JSch jsch = new JSch();
	        Session session = jsch.getSession(user, host, port);
	            session.setPassword(password);
	            session.setConfig("StrictHostKeyChecking", "no");
	        System.out.println("Establishing Connection...");
	        session.connect();
	            System.out.println("Connection established.");
	           
	           Channel channel = session.openChannel("exec");
	           ((ChannelExec)channel).setCommand("/var/tmp/terminateVMStats.sh");
	           channel.setInputStream(null);
	           ((ChannelExec)channel).setErrStream(System.err);
	           
	           InputStream in=channel.getInputStream();
	      
	           channel.connect();
	           byte[] tmp=new byte[1024];
	           while(true)
	           {
	               while(in.available()>0)
	               {
	                 int i=in.read(tmp, 0, 1024);
	                 if(i<0)break;
	                 System.out.print(new String(tmp, 0, i));
	               }
	               if(channel.isClosed())
	               {
	                 if(in.available()>0) continue; 
	                 System.out.println("exit-status: "+channel.getExitStatus());
	                 break;
	               }
	               
	               try
	               {
	            	   Thread.sleep(1000);
	               }
	               catch(Exception ee)
	               {
	            	   
	               }
	           }
	             channel.disconnect();
	             session.disconnect();
	    
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	           
	static void runhost()
	{
		// TODO Auto-generated method stub
		String user = "administrator";
	    String password = "12!@qwQW";
	    String host = "130.65.133.244"; //ip of fresh-build-vm
	    int port=22;

	   
	    
	    try
	    {
	        JSch jsch = new JSch();
	        Session session = jsch.getSession(user, host, port);
	            session.setPassword(password);
	            session.setConfig("StrictHostKeyChecking", "no");
	        System.out.println("Establishing Connection...");
	        session.connect();
	            System.out.println("Connection established.");
	          
	           Channel channel = session.openChannel("exec");
	           ((ChannelExec)channel).setCommand("/var/tmp/runHostStats.sh");
	           channel.setInputStream(null);
	           ((ChannelExec)channel).setErrStream(System.err);
	           
	           InputStream in=channel.getInputStream();
	      
	           channel.connect();
	           byte[] tmp=new byte[1024];
	           boolean flag= true;
	           int count=0;
	           while(flag)
	           {
	               while(in.available()>0)
	               {
	                 int i=in.read(tmp, 0, 1024);
	                 if(i<0)break;
	                 System.out.print(new String(tmp, 0, i));
	                 count++;
	                 if(count>5)
	                	 flag=false;
	                 
	               }
	               if(channel.isClosed())
	               {
	                 if(in.available()>0) continue; 
	                 System.out.println("exit-status: "+channel.getExitStatus());
	                 break;
	               }
	               
	               try
	               {
	            	   Thread.sleep(1000);
	               }
	               catch(Exception ee)
	               {
	            	   
	               }
	           }
	             channel.disconnect();
	             session.disconnect();
	           
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
   }
       
	           
	static void runvm()
	{
		// TODO Auto-generated method stub
		String user = "administrator";
	    String password = "12!@qwQW";
	    String host = "130.65.133.244"; //ip of fresh-build-vm
	    int port=22;

	    String remoteFile="/var/tmp/hello.txt";
	    
	    try
	    {
	        JSch jsch = new JSch();
	        Session session = jsch.getSession(user, host, port);
	            session.setPassword(password);
	            session.setConfig("StrictHostKeyChecking", "no");
	        System.out.println("Establishing Connection...");
	        session.connect();
	            System.out.println("Connection established.");
	          
	           Channel channel = session.openChannel("exec");
	           ((ChannelExec)channel).setCommand("/var/tmp/runVMStats.sh");
	           channel.setInputStream(null);
	           ((ChannelExec)channel).setErrStream(System.err);
	           
	           InputStream in=channel.getInputStream();
	      
	           channel.connect();
	           byte[] tmp=new byte[1024];
	           boolean flag= true;
	           int count=0;
	           while(flag)
	           {
	               while(in.available()>0)
	               {
	                 int i=in.read(tmp, 0, 1024);
	                 if(i<0)break;
	                 System.out.print(new String(tmp, 0, i));
	                 count++;
//	                 if(count>5)
//	                	 flag=false;
	                 
	               }
	               if(channel.isClosed())
	               {
	                 if(in.available()>0) continue; 
	                 System.out.println("exit-status: "+channel.getExitStatus());
	                 break;
	               }
	               
	               try
	               {
	            	   Thread.sleep(1000);
	               }
	               catch(Exception ee)
	               {
	            	   
	               }
	           }
	             channel.disconnect();
	             session.disconnect();
	           
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	    
   }

}


