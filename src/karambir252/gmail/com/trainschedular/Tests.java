package karambir252.gmail.com.trainschedular;

import karambir252.gmail.com.trainschedular.data.DataUtils;
import karambir252.gmail.com.trainschedular.data.PersonDailyData;

public class Tests {

	public static void main(String[] args) {
		testPersonDailyData(DataUtils.constructDataFilePath(1, 1));
	}
	
	private static void testPersonDailyData(String filePath){
		PersonDailyData personDailyData = new PersonDailyData(filePath);
		
	}

}
