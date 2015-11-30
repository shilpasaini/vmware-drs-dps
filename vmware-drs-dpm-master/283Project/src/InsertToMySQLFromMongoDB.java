import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.AggregationOutput;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;
import com.mysql.jdbc.PreparedStatement;


public class InsertToMySQLFromMongoDB 
{
	private static DB db;

	private static Connection conn;
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	//Ip of vm-shubham . Has MYSQL installed in it 
	private static final String AH_URL = "jdbc:mysql://130.65.133.245:3306/lab283mysql?user=root";
	private static final String AH_USER = "root";
    private static final String AH_PASSWORD = "12!@qwQW";

	private static DB getConnection() throws UnknownHostException {
		if (db == null) {
			
			MongoClient client = new MongoClient("130.65.133.244",27017);
			System.out.println("YEs.. Connected.. ");
			System.out.println("the value of db object"+client);
			db = client.getDB("lab283");
			System.out.println("the db object :::"+ db);
		} 
			
		
		return db;
	}

	public static Connection getMysqlConnection() 
	{
		if (conn == null)
		{
			try {
				Class.forName(DRIVER);
				
				conn = DriverManager.getConnection(AH_URL);
		
				
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	private static void archivedata() throws UnknownHostException {
		DBCollection tbl = getConnection().getCollection("NewStats");
		Date today = new Date();
		@SuppressWarnings("deprecation")
		String atblname = "archive"+today.getYear()+today.getMonth()+today.getDate();
		DBCollection atbl = getConnection().getCollection(atblname);
		DBCursor cur = tbl.find();
		while (cur.hasNext()) {
			atbl.insert(cur.next());
		}
		tbl.drop();
	}

	public static String getAggregateData() throws UnknownHostException {
		

		DBCollection tbl = getConnection().getCollection("vmlogs");
		System.out.println("value of tb1 inside getAggregate::"+tbl);
		
		String grp = "{$group:{_id:'$vmname',avgcpu:{$avg:'$cpu'},avgmemory:{$avg:'$mem'},"
				+ "avgdisk:{$avg:'$disk'},avgnetwork:{$avg:'$net'},avgsystem:{$avg:'$sys'}}}";

		DBObject group = (DBObject) JSON.parse(grp);
		System.out.println("JSON group"+group);
		
		
		
		AggregationOutput output = tbl.aggregate(group);  //(group);
		System.out.println("Agggregate outpot::"+ output);
		
		ArrayList<DBObject> list = (ArrayList<DBObject>) output.results();
for (DBObject dbObject : list) {
			System.out.println(dbObject);
			insertMysql(dbObject);
		}
		archivedata();
		return "";
	}

	public static void insertMysql(DBObject obj) {
		try {
			PreparedStatement st = (PreparedStatement) getMysqlConnection().prepareStatement("insert"
					+ " into lab283mysql.vmlogs(id,host,vmname,time,cpu,memory,disk,network,system) "
					+ "values(?,?,?,now(),?,?,?,?,?)");
			st.setString(1, "1");
			st.setString(2, "team19");
			st.setString(3, obj.get("_id").toString());
			st.setDouble(4, Double.parseDouble( obj.get("avgcpu").toString()));
			st.setDouble(5, Double.parseDouble( obj.get("avgmemory").toString()));
			st.setDouble(6, Double.parseDouble( obj.get("avgdisk").toString()));
			st.setDouble(7, Double.parseDouble( obj.get("avgnetwork").toString()));
			st.setDouble(8, Double.parseDouble( obj.get("avgsystem").toString()));
			st.executeUpdate();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	static Thread t1 = new Thread()
	{
		public void run(){
			while(true){
			try{
			getAggregateData();
			Thread.sleep(6000000);
			}catch(UnknownHostException e){
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	};

	public static void main(String[] args) throws UnknownHostException 
	{
		t1.start();
//		System.out.println("Calling 1...");
//		getConnection();
//		System.out.println("Calling 2..");
//		getMysqlConnection();
//		System.out.println("Finished calling..");
	}
}
