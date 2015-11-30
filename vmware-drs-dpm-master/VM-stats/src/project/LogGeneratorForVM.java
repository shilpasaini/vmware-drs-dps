package project;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogGeneratorForVM 
{
	public static LogGeneratorForVM obj;
	BufferedWriter writer = null;

	private LogGeneratorForVM() throws IOException {
		File logFile = new File("vmStatistics.log");
		System.out.println(logFile.getCanonicalPath());
		writer = new BufferedWriter(new FileWriter(logFile, true));
	}

	public static LogGeneratorForVM getInstance() throws IOException {
		if (obj == null) {
			obj = new LogGeneratorForVM();
		}
		return obj;
	}

	public void write(String s) {

		try {
			writer.write(s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
				
			} catch (Exception e) {
			}
		}
	}
}

