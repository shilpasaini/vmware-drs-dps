package project;

public class SJSULabConfig {
	
	public static String getvHostUsername() { return "root" ; }
	public static String getvCenterUsername() { return "Administrator" ; }
	public static String getPassword() { return "12!@qwQW" ; }
	public static String getvCenterURL() { return "https://130.65.133.74/sdk" ; }

	
	/*public static String getHost1URL() { return "https://130.65.133.47/sdk" ; }   
	public static String getHost1IpAddress() {return "130.65.133.47";}
	public static String getHost2URL() { return "https://130.65.133.46/sdk" ; }
	public static String getHost2IpAddress() {return "130.65.133.46";}*/
	//public static String getHost3URL() { return "https://130.65.133.45/sdk" ; }
	//public static String getHost3IpAddress() {return "130.65.133.45";}
	
	public static int PERF_METRIC_ID_CPU = 6; 
	public static int PERF_METRIC_ID_MEM = 33; 
	public static int PERF_METRIC_ID_DISK = 125; 
	public static int PERF_METRIC_ID_NETWORK = 143; 
	
	public static int PERF_METRIC_ID_DISK_LATENCY = 133;
	
	public static String[] getVMNames() {
		//String[] vmNames = {"T16-VM02Server-Lab3", "T16-VM05-Lab3", "T16-VM04-Lab3", "T16-VM03-Lab3", "T16-VM01-Lab3"};
		String[] vmNames = {"T19-vm02-ubuntu32-shubham"};
		return vmNames;
		
	}
		
}
