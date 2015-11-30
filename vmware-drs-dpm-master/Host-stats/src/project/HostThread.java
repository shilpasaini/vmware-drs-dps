package project;




import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;




import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.PerfEntityMetric;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfMetricId;
import com.vmware.vim25.PerfMetricIntSeries;
import com.vmware.vim25.PerfMetricSeries;
import com.vmware.vim25.PerfProviderSummary;
import com.vmware.vim25.PerfQuerySpec;
import com.vmware.vim25.PerfSampleInfo;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class HostThread implements Runnable {


	static final String SERVER_NAME = "130.65.133.74"; 
	static final String USER_NAME = "administrator";
	static final String PASSWORD = "12!@qwQW";
	public String ip;
	private ServiceInstance serviceInst;
	private static final int SELECTED_COUNTER_ID = 6; 
	static Integer[] a = {  23 };
	static String[] aName = { "cpu" };
	private HashMap<String, String> infoList = new HashMap<String, String>();
	int counter = 0;
    static LogGeneratorForHost writer;
	

	public HostThread(String hostIP) throws IOException {
		this.ip = hostIP;
	
		
		try {
			writer = LogGeneratorForHost.getInstance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		try {

			while (true) {

				URL url = new URL(SJSULabConfig.getvCenterURL());
				try {
				
					ServiceInstance si = new ServiceInstance(url,SJSULabConfig.getvCenterUsername(),SJSULabConfig.getPassword(), true);
					HostSystem host = (HostSystem) new InventoryNavigator(
							si.getRootFolder()).searchManagedEntity(
							"HostSystem", ip); // 
					System.out.println("host value"+host);
					PerformanceManager perfMgr = si.getPerformanceManager();
					
					PerfProviderSummary summary = perfMgr.queryPerfProviderSummary(host); //vm
					
					int perfInterval = summary.getRefreshRate();
					
					PerfMetricId[] queryAvailablePerfMetric = perfMgr.queryAvailablePerfMetric(host, null, null,perfInterval);
					PerfCounterInfo[] pci = perfMgr.getPerfCounter();
					ArrayList<PerfMetricId> list = new ArrayList<PerfMetricId>();
					for (int i2 = 0; i2 < queryAvailablePerfMetric.length; i2++) 
					{
						PerfMetricId perfMetricId = queryAvailablePerfMetric[i2];
						if (SELECTED_COUNTER_ID == perfMetricId.getCounterId()) {
							list.add(perfMetricId);
						}
					}
					PerfMetricId[] pmis = list.toArray(new PerfMetricId[list
							.size()]);
					PerfQuerySpec qSpec = new PerfQuerySpec();
					qSpec.setEntity(host.getMOR());
					qSpec.setMetricId(pmis);

					qSpec.intervalId = perfInterval;
					PerfEntityMetricBase[] pembs = perfMgr
							.queryPerf(new PerfQuerySpec[] { qSpec });
					
					
					
					for (int i = 0; pembs != null && i < pembs.length; i++) {

						PerfEntityMetricBase val = pembs[i];
						PerfEntityMetric pem = (PerfEntityMetric) val;
						PerfMetricSeries[] vals = pem.getValue();
						PerfSampleInfo[] infos = pem.getSampleInfo();

						Date date1 = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy h:mm:ss");
						String formattedDate = sdf.format(date1);
				
						
						infoList.put(aName[counter], formattedDate);
						
						for (int j = 0; vals != null && j < vals.length; ++j) {
							PerfMetricIntSeries val1 = (PerfMetricIntSeries) vals[j];
							long[] longs = val1.getValue();
							System.out.println("Host CPU :"+ longs[5]);
							infoList.put(aName[0],String.valueOf(longs[5]));
							/*
							for (int k : a) {
								
								infoList.put(aName[counter+1],
										String.valueOf(longs[k]));
								counter++;
							}
							counter = 0;
							*/
						}
					}
					si.getServerConnection().logout();
				} catch (InvalidProperty e) {
					e.printStackTrace();
				} catch (RuntimeFault e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

			//	infoList.put("vmIP", ip);
			
				
				counter = 0;
			
				try {
					
				
					StringBuilder sb = new StringBuilder();
					sb.append("vmname:"+ip+",");
					int countTest=0;
					for (String str : aName) {
						sb.append( aName[countTest]+":"+infoList.get(str).toString()+",");
						countTest++;
					}
					String out = sb.toString();
					out = out.substring(0,out.length()-1);
					writer.write(out+"\n");
					System.out.println(out);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Thread.sleep(5000);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
