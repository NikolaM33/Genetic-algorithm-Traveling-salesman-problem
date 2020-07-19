package algoritmiGenetski;

import java.util.Arrays;

public class GeneticAlgorithm {
	private int populationSize;
	private double mutationRate;
	private double crossoverRate;
	private int elitismCount;
	protected int tournamentSize;

	public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCOunt,
			int tournamentSize) {
		this.populationSize = populationSize;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.elitismCount = elitismCount;
		this.tournamentSize = tournamentSize;

	}

	public double calcFitness(Individual individual, City cities[]) {
		// izracunaj fitnes
		Route route = new Route(individual, cities);
		double fitness = 1 / route.getDistance();
		// Sacuvaj fitnes
		individual.setFitness(fitness);
		return fitness;
	}

	public Population initPopulation(int chromosomeLength) {
		Population population = new Population(this.populationSize, chromosomeLength);
		return population;
	}

	public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
		return (generationsCount > maxGenerations);
	}

	public void evalPopulation(Population population, City cities[]) {
		double populationFitness = 0;
		// Loop over population evaluating individuals and summing population
		// fitness
		for (Individual individual : population.getIndividuals()) {
			populationFitness += this.calcFitness(individual, cities);
		}
		double avgFitness = populationFitness / population.size();
		population.setPopulationFitness(avgFitness);
	}

	public Individual selectParent(Population population) {
		// Create tournament
		Population tournament = new Population(this.tournamentSize);
		// Add random individuals to the tournament
		population.shuffle();
		for (int i = 0; i < this.tournamentSize; i++) {
			Individual tournamentIndividual = population.getIndividual(i);
			tournament.setIndividual(i, tournamentIndividual);
		}
		// Return the best
		return tournament.getFittest(0);
	}

	public Population crossoverPopulation(Population population) {
		// Kreiranje nove populacije
		Population newPopulation = new Population(population.size());
		// Loop over current population by fitness
		for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
			// Roditelj1 
			Individual parent1 = population.getFittest(populationIndex);
			// Primeni ukrstanje na ovog pojedinca
			if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
				// Pronadji roditelja2 pomocu turnir selekcije
				Individual parent2 = this.selectParent(population);
				//  Kreiranje praznog hromoyoma potomstva
				int offspringChromosome[] = new int[parent1.getChromosomeLength()];
				Arrays.fill(offspringChromosome, -1);
				Individual offspring = new Individual(offspringChromosome);
				// Podskup hromozoma roditelja
				int substrPos1 = (int) (Math.random() * parent1.getChromosomeLength());
				int substrPos2 = (int) (Math.random() * parent1.getChromosomeLength());
				// manji na pocetku, veci na kraju
				final int startSubstr = Math.min(substrPos1, substrPos2);
				final int endSubstr = Math.max(substrPos1, substrPos2);
				// Petlja i dodavanje podskua Roditilja1 detetu
				for (int i = startSubstr; i < endSubstr; i++) {
					offspring.setGene(i, parent1.getGene(i));
				}
				// Petlja kroz rutu gradova Roditelja2
				for (int i = 0; i < parent2.getChromosomeLength(); i++) {
					int parent2Gene = i + endSubstr;
					if (parent2Gene >= parent2.getChromosomeLength()) {
						parent2Gene -= parent2.getChromosomeLength();
					}
					// Ako potomak nema grad dodaj ga
					if (offspring.containsGene(parent2.getGene(parent2Gene)) == false) {
						// trazenje mesta ya dodavanje grada
						for (int ii = 0; ii < offspring.getChromosomeLength(); ii++) {
							// mesto pronadjeno, dodaj grad
							if (offspring.getGene(ii) == -1) {
								offspring.setGene(ii, parent2.getGene(parent2Gene));
								break;
							}
						}
					}
				}
				// Dodaj dete
				newPopulation.setIndividual(populationIndex, offspring);
			} else {
				//Dodavanje pojedinca novoj populaciji bez primene ukrstanja
				newPopulation.setIndividual(populationIndex, parent1);
			}
		}
		return newPopulation;
	}

	public Population mutatePopulation(Population population) {
		// Inicijaliziranje nove populacije
		Population newPopulation = new Population(this.populationSize);
		// Prolozak kroz trenutnu populaciju na osnovu fitnesa
		for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
			Individual individual = population.getFittest(populationIndex);
			//  Preskoci mutaciju ako je ovo elitna jedinka
			if (populationIndex >= this.elitismCount) {
				// System.out.println("Mutacija clana populacije:  "+populationIndex);
				
				for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
					// Da li  treba mutacija ovom genu?
					if (this.mutationRate > Math.random()) {
						// Nadji novu poziciju gena
						int newGenePos = (int) (Math.random() * individual.getChromosomeLength());
						// Pronadji gene za zamenu
						int gene1 = individual.getGene(newGenePos);
						int gene2 = individual.getGene(geneIndex);
						// Zamena gena
						individual.setGene(geneIndex, gene1);
						individual.setGene(newGenePos, gene2);
					}
				}
			}
			// Dodavanje jedinke populaciji
			newPopulation.setIndividual(populationIndex, individual);
		}
		// Vrati mutiranu populaciju
		return newPopulation;
	}
}
