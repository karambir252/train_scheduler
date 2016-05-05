package karambir252.gmail.com.trainschedular.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Pattern;

import karambir252.gmail.com.trainschedular.constants.RegExConstant;

/*
 * Represent Passenger counts for one day
 * Read data directly from '.csv' file
 */

public class PersonDailyData {
	
	//interval in which passenger count is recorded
	private int intervalInMin;
	
	/*
	 * startTime and endTime during which passenger count is recorded
	 * unit: minutes passed after midnight
	 */
	private int startTime , endTime;
	
	/*
	 * key = Station ID
	 * value = passengers count for each interval
	 */
	private Map<Integer,List<Integer>> data;
	/*
	 * key = Station ID
	 * value = Station Name
	 */
	private Map<Integer,String> stationsName;
	
	//header of csv file
	private String csvHeader;
	
	private PersonDailyData(){
		data = new HashMap<>();
		stationsName = new HashMap();
	}
	
	public PersonDailyData(String filePath){
		this();
		readFromFile(filePath);
	}
	
	public void readFromFile(String filePath){
		clear();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			
			String line = reader.readLine();
			this.csvHeader = line;
			
			Pattern commaPattern = Pattern.compile(RegExConstant.CSV_SEPARATOR_STRING);
			
			String[] headers = commaPattern.split(csvHeader);
			
			this.intervalInMin = DataUtils.getInterval(headers[2]);
			this.startTime = DataUtils.getStartTime(headers[2]);
			this.endTime = DataUtils.getEndTime(headers[headers.length-1]);
			
			int numberOfIntervals = this.getNumberOfIntervals();
			
			
			while((line = reader.readLine()) != null){
				String[] row = commaPattern.split(line);
				int stationId = Integer.parseInt(row[0]);
				String stationName = row[1].trim();
				List<Integer> counts = new ArrayList<>(numberOfIntervals);
				for(int i=0; i < numberOfIntervals ; i++){
					int c = Integer.parseInt(row[2+i]);
					counts.add(c);
				}
				
				data.put(stationId, counts);
				stationsName.put(stationId, stationName);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(reader != null){
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void writeToFile(String filePath){
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)));
			
			writer.println(csvHeader);
			
			List<Entry<Integer,List<Integer>>> dataSet = 
					new ArrayList<Entry<Integer,List<Integer>>>(data.entrySet());
			
			Collections.sort(dataSet,new Comparator<Entry<Integer,List<Integer>>>(){
				@Override
				public int compare(Entry<Integer, List<Integer>> x, Entry<Integer, List<Integer>> y) {
					return x.getKey() - y.getKey();
				}
			});
			
			for(Entry<Integer,List<Integer>> e : dataSet){
				int key = e.getKey();
				List<Integer> counts = e.getValue();
				String stationName = stationsName.get(key);
				
				writer.print(key);
				writer.print(RegExConstant.CSV_SEPARATOR_CHAR);
				writer.print(stationName);
				for(Integer c : counts){
					writer.print(RegExConstant.CSV_SEPARATOR_CHAR);
					writer.print(c);
				}
				writer.println();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writer != null){
				writer.close();
			}
		}
	}
	
	public void clear(){
		data.clear();
		stationsName.clear();
	}
	
	public int getNumberOfIntervals(){
		return (endTime-startTime)/intervalInMin;
	}
	
	public void tweakValues(float multiplier,float error){
		Random random = new Random();
		
		Collection<List<Integer>> dataSet = data.values();
		for(List<Integer> d : dataSet){
			int length = d.size();
			for(int i=0; i < length ; i++){
				int value = (int)(multiplier * d.get(i));
				int e = (int)(value*error*random.nextFloat()*(random.nextBoolean()?1:-1));
				
				value += e;
				if(value < 0){
					value = 0;
				}
				
				d.set(i, value);
			}
		}
	}
}
