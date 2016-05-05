package karambir252.gmail.com.trainschedular.data;

/*
 * Class have utilities methods to format the data from csv file
 */

public class DataUtils {
	
	/*
	 * week from {1,2,3,4}
	 * day from {1,2,3,4,5,6,7} , 1 = sunday , 7 = saturday
	 */
	public static String constructDataFilePath(int week,int day){
		if(week < 1 || week > 4){
			throw new IllegalArgumentException("Invalid week: "+week);
		}
		
		if(day < 1 || day > 7){
			throw new IllegalArgumentException("Invalid day: "+day);
		}
		
		
		String dayString = null;
		switch(day){
		case 1:
			dayString = "sun";
			break;
		case 2:
			dayString = "mon";
			break;
		case 3:
			dayString = "tue";
			break;
		case 4:
			dayString = "wed";
			break;
		case 5:
			dayString = "thu";
			break;
		case 6:
			dayString = "fri";
			break;
		case 7:
			dayString = "sat";
			break;
		}
		
		return "data/counts/"+week+"/"+dayString+".csv";
	}
	
	/*
	 * convert 0415 -> 4*60+15  == 4:15AM
	 */
	public static int stringToIntTime(String timeInMin){
		if(timeInMin.length() != 4){
			throw new IllegalArgumentException("Invalid time: "+timeInMin);
		}
		int hours = Integer.parseInt(timeInMin.substring(0, 2));
		int mins = Integer.parseInt(timeInMin.substring(2));
		return hours*60 + mins;
	}
	
	/*
	 * convert 255(4*60+15) -> 0415    == 4:15AM
	 */
	
	public static String intToStringTime(int timeInMin){
		int hours = timeInMin / 60;
		int mins = timeInMin % 60;
		return String.format("%2d%2d",hours,mins);
	}
	
	/*
	 * interval = '0400-0415'
	 * return 15 minutes
	 */
	
	public static int getInterval(String interval){
		if(interval.length() != 9 || interval.charAt(4) != '-'){
			throw new IllegalArgumentException("Invalid interval: "+interval);
		}
		
		int startTime = stringToIntTime(interval.substring(0, 4));
		int endTime = stringToIntTime(interval.substring(5));
		
		return endTime-startTime;
	}
	
	/*
	 * interval = '0400-0415'
	 * return stringToInt(0400)
	 */
	
	public static int getStartTime(String interval){
		if(interval.length() != 9 || interval.charAt(4) != '-'){
			throw new IllegalArgumentException("Invalid interval: "+interval);
		}
		return stringToIntTime(interval.substring(0, 4));
	}
	
	/*
	 * interval = '0400-0415'
	 * return stringToInt(0415)
	 */
	
	public static int getEndTime(String interval){
		if(interval.length() != 9 || interval.charAt(4) != '-'){
			throw new IllegalArgumentException("Invalid interval: "+interval);
		}
		return stringToIntTime(interval.substring(5));
	}
	
}
