package question1;

import java.util.ArrayList;

public class Question1 {

	public static void main(String[] args) {
		// Create and populate objects.
		ArrayList<Measurement> measurements1 = new ArrayList<Measurement>();
		WeatherStation station1 = new WeatherStation("Station1", measurements1);

		measurements1.add(new Measurement(1, 20.0));
		measurements1.add(new Measurement(2, 11.7));
		measurements1.add(new Measurement(3, -5.4));
		measurements1.add(new Measurement(4, 18.7));
		measurements1.add(new Measurement(5, 20.9));

		double station1MaxTemperature = station1.maxTemperature(2, 4);
		System.out.println(station1MaxTemperature);
		station1MaxTemperature = station1.maxTemperature(1, 3);
		System.out.println(station1MaxTemperature);
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
		// Filter measurements within the specified time range, convert the temperatures
		// to a DoubleStream, and find the maximum temperature.
		return measurements.stream().filter(m -> m.getTime() >= startTime && m.getTime() <= endTime)
				.mapToDouble(m -> m.getTemperature()).max().orElseThrow();
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
