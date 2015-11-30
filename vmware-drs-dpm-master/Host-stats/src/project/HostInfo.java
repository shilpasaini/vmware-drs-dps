package project;



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.oracle.webservices.internal.literal.ArrayList;
import com.vmware.vim25.HostSystemInfo;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class HostInfo
{

	ServiceInstance si = null;
	public void pingvms() throws IOException{
		URL url = new URL(SJSULabConfig.getvCenterURL());
		si = new ServiceInstance(url, SJSULabConfig.getvCenterUsername(), SJSULabConfig.getPassword(), true);
		Folder rootFolder = si.getRootFolder();
		String rootname = rootFolder.getName();
	
		ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");
		System.out.println("number of hosts"+ mes.length);
		
		if (mes == null || mes.length == 0) 
		{
			System.out.println("\n\tNo Hosts machine Found !!!");
			return;
		}
		java.util.ArrayList<HostThread> list = new java.util.ArrayList<HostThread>();
		for (int i = 0; i < mes.length; i++) 
		{
			
			String ip= mes[i].getName();
			System.out.println("Collecting statistics for hosts on vCenter : "+mes[i].getName());
			System.out.println(mes[i].getMOR().get_value());
			System.out.println("After call");
			Runnable task = new HostThread(mes[i].getName());
			Thread worker = new Thread(task);
			worker.start();
			
		}
		for (HostThread vmHealthUpdateThread : list) {
			
			vmHealthUpdateThread.run();
		}
	}
	public static void main(String[] args) throws Exception {
		
		HostInfo obj = new HostInfo();
		obj.pingvms();
	}
}

