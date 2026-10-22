package question2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Question2 {

	public static void main(String[] args) {
		// Create and populate objects.
		ArrayList<Measurement> measurements1 = new ArrayList<Measurement>();
		ArrayList<Measurement> measurements2 = new ArrayList<Measurement>();

		measurements1.add(new Measurement(1, 20.0));
		measurements1.add(new Measurement(2, 11.7));
		measurements1.add(new Measurement(3, -5.4));
		measurements1.add(new Measurement(4, 18.7));
		measurements1.add(new Measurement(5, 20.9));

		measurements2.add(new Measurement(1, 8.4));
		measurements2.add(new Measurement(2, 19.2));
		measurements2.add(new Measurement(3, 7.2));

		WeatherStation station1 = new WeatherStation("Station1", measurements1);
		WeatherStation station2 = new WeatherStation("Station2", measurements2);

		// Call countTemperatures.
		List<Pair<Double, Integer>> stationCountTemperatures = station1.countTemperatures(19.0, 10.8, 2.1);
		System.out.println(stationCountTemperatures);
	}
}

class WeatherStation {

	// Variables.
	private String city;
	private ArrayList<Measurement> measurements;
	private static ArrayList<WeatherStation> stations = new ArrayList<WeatherStation>();

	// Methods.
	public WeatherStation(String city, ArrayList<Measurement> measurements) {
		this.city = city;
		this.measurements = measurements;
		stations.add(this);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public ArrayList<Measurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(ArrayList<Measurement> measurements) {
		this.measurements = measurements;
	}

	public static ArrayList<WeatherStation> getStations() {
		return stations;
	}

	public static void setStations(ArrayList<WeatherStation> stations) {
		WeatherStation.stations = stations;
	}

	public double maxTemperature(int startTime, int endTime) {
		// Filter measurements within the specified time range, convert their
		// temperatures to a DoubleStream, and find the maximum temperature.
		return measurements.stream().filter(m -> m.getTime() >= startTime && m.getTime() <= endTime)
				.mapToDouble(m -> m.getTemperature()).max().orElseThrow();
	}

	public List<Pair<Double, Integer>> countTemperatures(double t1, double t2, double r) {
		// Variables for the upper and lower ranges for t1 and t2.
		double t1_upper = t1 + r;
		double t1_lower = t1 - r;
		double t2_upper = t2 + r;
		double t2_lower = t2 - r;

		// Map.
		// Get all of the stations in parallel, flatten the measurements into one
		// stream, and create a pair for each if the temperature is within the ranges.
		List<Pair<Double, Integer>> map = stations.parallelStream().flatMap(s -> s.getMeasurements().parallelStream())
				.flatMap(m -> {
					List<Pair<Double, Integer>> pairs = new ArrayList<>();
					double temperature = m.getTemperature();

					// If the measurement is within the t1 range, add it to pairs.
					if (temperature >= t1_lower && temperature <= t1_upper) {
						pairs.add(new Pair<Double, Integer>(t1, 1));
					}

					// If the measurement is within the t2 range, add it to pairs.
					if (temperature >= t2_lower && temperature <= t2_upper) {
						pairs.add(new Pair<Double, Integer>(t2, 2));
					}

					return pairs.parallelStream();
				}).collect(Collectors.toList());

		// Shuffle.
		// Get the map in parallel, and group the pairs with same temperature key.
		// Reduce.
		// Sum all of the values for each key.
		Map<Double, Integer> shuffleAndReduce = map.parallelStream()
				.collect(Collectors.groupingBy(Pair::getKey, Collectors.summingInt(Pair::getValue)));

		// Convert the Map to a List of Pair objects.
		List<Pair<Double, Integer>> countTemperaturesList = shuffleAndReduce.entrySet().parallelStream()
				.map(e -> new Pair<Double, Integer>(e.getKey(), e.getValue())).collect(Collectors.toList());

		return countTemperaturesList;
	}

	@Override
	public String toString() {
		return "WeatherStation [city=" + city + ", measurements=" + measurements + "]";
	}
}

class Measurement {

	// Variables.
	private int time;
	private double temperature;

	// Methods.
	public Measurement(int time, double temperature) {
		this.time = time;
		this.temperature = temperature;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	@Override
	public String toString() {
		return "Measurement [time=" + time + ", temperature=" + temperature + "]";
	}
}

// Pair class from https://www.geeksforgeeks.org/java/pair-class-in-java/. 
class Pair<K, V> {

	private final K key;
	private final V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Pair [key=" + key + ", value=" + value + "]";
	}

}
