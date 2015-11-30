import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.demo.PieChartDemo1;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mysql.jdbc.PreparedStatement;
import com.orsoncharts.data.PieDataset3D;


	
	public class GenerateChart extends ApplicationFrame {

	    

		private static final long serialVersionUID = 1L;

	    {
	        // set a theme using the new shadow generator feature available in
	        // 1.0.14 - for backwards compatibility it is not enabled by default
	        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow",true));
	    }

	    /**
	077     * Creates a new demo instance.
	078     *
	079     * @param title  the frame title.
	080     
	     * @throws InterruptedException */
	    public GenerateChart(String title) throws InterruptedException {
	    	super(title);
	        	CategoryDataset dataset = createDataset();
	        	  JFreeChart chart = createChart(dataset);
	  	        ChartPanel chartPanel = new ChartPanel(chart);
	        	//chart = createChart(dataset);
	        	//chartPanel=new ChartPanel(chart);
	        	chartPanel.setFillZoomRectangle(true);
	        	chartPanel.setMouseWheelEnabled(true);
	 	        chartPanel.setPreferredSize(new Dimension(500, 270));
	 	        setContentPane(chartPanel);
	    } 
	    
	    public GenerateChart(String title, JFreeChart jf)
	    {
	    	super(title);
	    	
	        JFreeChart chartnew = jf;
	        ChartPanel chartPanel = new ChartPanel(chartnew);
	        chartPanel.setFillZoomRectangle(true);
	        chartPanel.setMouseWheelEnabled(true);
	        chartPanel.setPreferredSize(new Dimension(500, 270));
	        setContentPane(chartPanel);
	    }

	    /**
	     * Returns a sample dataset.
	094     *
	095     * @return The dataset.
	096     */
	    private static CategoryDataset createDataset() {

	    	Connection conn;
	    	String DRIVER = "com.mysql.jdbc.Driver";
	    	//Ip of vm-shubham . Has MYSQL installed in it 
	    	String AH_URL = "jdbc:mysql://130.65.133.245:3306/lab283mysql?user=root";
	    	String AH_USER = "root";
	         String AH_PASSWORD = "12!@qwQW";
	         ResultSet rs=null;
	         List<String> vmNames=new ArrayList<String>();
	         List<Double> vmMetrics=new ArrayList<Double>();
	         
	         Map<String, HashMap<String,Double>> vmMetricValues = new HashMap<String, HashMap<String,Double>>();
	         
	    	try{
	        conn=DriverManager.getConnection(AH_URL);
	        rs=conn.prepareStatement("select distinct vmname from vmlogs").executeQuery();
	        while(rs.next())
	        {
	        	vmNames.add(rs.getString("vmname"));
	        }
	        
	        
	        ResultSet rs2=conn.prepareStatement("select vmname, avg(cpu) as cpu, avg(memory) as memory, avg(disk) as disk from vmlogs group by vmname").executeQuery();
	        while(rs2.next())
	        {
	        	//List<Double> metrics=new ArrayList<Double>();
	        	HashMap<String,Double> temp=new HashMap<String,Double>();
	        	temp.put("cpu",(Double)rs2.getDouble("cpu"));
	        	temp.put("memory",(Double)rs2.getDouble("memory"));
	        	temp.put("disk",(Double)rs2.getDouble("disk"));
	        	vmMetricValues.put(rs2.getString("vmname"),temp);
	        }
	    	}catch(Exception e){
	    		System.out.println(e);
	    	}
	    	
	    	
	    	//
	    	
	        

	        
	        //series
	        String column1="cpu";
	        String column2="memory";
	        String column3="disk";
	        String column4="network";
	        String column5="system";
	        
	        
	        //category query = select distinct vmname from vmlogs;
	        // column keys...
	        
	        
	      //  PieDataset dataset = new PieDataset();
	        
	        // create the dataset...
	       DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	        for(int i=0;i<vmNames.size();i++)
	        {
	        	HashMap<String,Double> tempMap=new HashMap<String,Double>();
	        	tempMap=vmMetricValues.get(vmNames.get(i));
	        dataset.addValue(tempMap.get("cpu"), column1, vmNames.get(i));
	        dataset.addValue(tempMap.get("memory"), column2, vmNames.get(i));
	        dataset.addValue(tempMap.get("disk"), column3, vmNames.get(i));
	        
	        
	        }
	        
	        
	        
	       

	        return dataset;

	    }

	    /**
	     * Creates a sample chart.
	     *
	     * @param dataset  the dataset.
	     *
	     * @return The chart.
	     */
	    private static JFreeChart createChart(CategoryDataset dataset) {
	        // create the chart...
	        JFreeChart chart = ChartFactory.createBarChart3D(
	            "Bar Chart Demo 1",       // chart title
	           "Category",               // domain axis label
	            "Value",                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                     // include legend
	            true,                     // tooltips?
	            false                     // URLs?
	        );

	       // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

	        // set the background color for the chart...
	        chart.setBackgroundPaint(Color.white);

	        // get a reference to the plot for further customisation...
	        CategoryPlot plot = (CategoryPlot) chart.getPlot();

	        // ******************************************************************
	        //  More than 150 demo applications are included with the JFreeChart
	        //  Developer Guide...for more information, see:
	        //
	        //  >   http://www.object-refinery.com/jfreechart/guide.html
	        //
	        // ******************************************************************

	        // set the range axis to display integers only...
	        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

	        // disable bar outlines...
	        BarRenderer renderer = (BarRenderer) plot.getRenderer();
	        renderer.setDrawBarOutline(false);
	        
	        //renderer.setBaseItemLabelGenerator(CategoryItemLabelGenerator generator);
	        // set up gradient paints for series...

	        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.magenta, 0.0f, 0.0f, new Color(0, 0, 64));
	        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,0.0f, 0.0f, new Color(0, 64, 0));
	        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.orange,0.0f, 0.0f, new Color(64, 0, 0));
	        renderer.setSeriesPaint(0, gp0);
	        renderer.setSeriesPaint(1, gp1);
	        renderer.setSeriesPaint(2, gp2);

	
	        	
	        CategoryAxis domainAxis = plot.getDomainAxis();
	        domainAxis.setCategoryLabelPositions(
	                CategoryLabelPositions.createUpRotationLabelPositions(
	                        Math.PI / 6.0));
	        // OPTIONAL CUSTOMISATION COMPLETED.
	        
	       

	        return chart;

	    }
	    
   
	    /**
	     * Starting point for the demonstration application.
	     *
	     * @param args  ignored.
	     * @throws InterruptedException 
	     */
	    public static void main(String[] args) throws InterruptedException {
	        
	    	GenerateChart demo = new GenerateChart("Bar Chart Demo 1");
	    	int counter=0;
		       while(true)
		       {
		        demo.setVisible(false);
		        demo.removeAll();
		        demo=new GenerateChart("Bar Chart Demo "+counter);
		        demo.pack();
		        
		        RefineryUtilities.centerFrameOnScreen(demo);
		        demo.setVisible(true);
		        counter++;
		        try
		        {
		        	Thread.sleep(20000);
		        }
		        catch(Exception e){e.printStackTrace();}
		     //  Object w =  new Object();
		//	       while(true)
		//	       {
		//	    	   CategoryDataset datasetnew = createDataset();
		//		        JFreeChart jf = createChart(datasetnew);
		//	    	   w.wait(5000);
		//	    	  // GenerateChart demo = new GenerateChart("Bar Chart Demo 1",jf);
		//	    	   //demo("Bar Chart Demo 1", jf);
		//	    	   demo.changeParameters("Bar Chart Demo 1", jf);
		//	    	   demo.repaint();
		//	       }
		       }
	    }
	}



