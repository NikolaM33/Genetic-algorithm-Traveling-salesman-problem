package algoritmiGenetski;

public class Tsp {

	public City[] generatePopulation(int size) {
		City randomPopulation[] = new City[size];
		for (int cityIndex = 0; cityIndex < size; cityIndex++) {
			int xPos = (int) (100 * Math.random());
			int yPos = (int) (100 * Math.random());
			randomPopulation[cityIndex] = new City(xPos, yPos);
		} return randomPopulation;

	}
}
