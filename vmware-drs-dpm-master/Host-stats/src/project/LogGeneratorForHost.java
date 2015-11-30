package project;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogGeneratorForHost {
	public static LogGeneratorForHost obj;
	BufferedWriter writer = null;

	private LogGeneratorForHost() throws IOException {
		File logFile = new File("hostStatistics.log");
		System.out.println(logFile.getCanonicalPath());
		writer = new BufferedWriter(new FileWriter(logFile, true));
	}

	public static LogGeneratorForHost getInstance() throws IOException {
		if (obj == null) {
			obj = new LogGeneratorForHost();
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

