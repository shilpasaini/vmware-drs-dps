package project;



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;



import com.oracle.webservices.internal.literal.ArrayList;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class VMInfo 
{
	
	ServiceInstance si = null;
	public void pingvms() throws IOException
	{
		URL url = new URL(SJSULabConfig.getvCenterURL());
		si = new ServiceInstance(url, SJSULabConfig.getvCenterUsername(), SJSULabConfig.getPassword(), true);
		Folder rootFolder = si.getRootFolder();
		String rootname = rootFolder.getName();
	
		ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
		
		if (mes == null || mes.length == 0) 
		{
			System.out.println("\n\tNo Virtual machine Found !!!");
			return;
		}
		
		java.util.ArrayList<VMThread> list = new java.util.ArrayList<VMThread>();
		for (int i = 0; i < mes.length; i++)
		{
			VirtualMachine vm = (VirtualMachine) mes[i];
			
			System.out.println("Collecting statistics all VMs: "+vm.getName());
			
			Runnable task = new VMThread(vm.getName());
			Thread worker = new Thread(task);
			worker.start();
			
		}
		for (VMThread vmHealthUpdateThread : list) {
			
			vmHealthUpdateThread.run();
		}
	}
	public static void main(String[] args) throws Exception {
		
		VMInfo obj = new VMInfo();
		obj.pingvms();
	}
}

