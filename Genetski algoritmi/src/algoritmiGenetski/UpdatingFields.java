package algoritmiGenetski;

import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class UpdatingFields extends SwingWorker<String, String> {
	JTextArea textArea;
	JProgressBar progressbar;
	private double mutationRate, crossoverRate;
	int tournamentSize, generations, populationSize;
	City cities[];
	private JTextField txtBestDistance;
	private JCheckBox samePopulation;

	public UpdatingFields(JTextArea textAreatoFill, JProgressBar pbar, double mutatonRate,double crossoverRate, int tournamentSize, int generations, int populationSize, JTextField result, JCheckBox check) {
		textArea = textAreatoFill;
		progressbar = pbar;
		mutationRate=mutatonRate;
		this.crossoverRate=crossoverRate;
		this.tournamentSize=tournamentSize;
		this.generations=generations;
		this.populationSize=populationSize;
		txtBestDistance=result;
		samePopulation=check;
		

	}

	@Override
	protected String doInBackground() throws Exception {
		// TODO Auto-generated method stub
		
			progressbar.setMaximum(generations);
			int numCities = populationSize;
			cities= new City[numCities];
			City[] populationGen=new City [numCities];
			Tsp tsp= new Tsp();
			populationGen=tsp.generatePopulation(populationSize);
			
			if (samePopulation.isSelected()) {
				cities=populationGen;
				
				// Loop to create random cities
				//for (int cityIndex = 0; cityIndex < numCities; cityIndex++) {
				//	int xPos = (int) (100 * Math.random());
				//	int yPos = (int) (100 * Math.random());
				//	cities[cityIndex] = new City(xPos, yPos);	
				//}

		} else {
				
				cities=tsp.generatePopulation(numCities);
				
			}
			

			// Initial GA
			GeneticAlgorithm ga = new GeneticAlgorithm(numCities, mutationRate, crossoverRate, 2,
					tournamentSize);
			// Initialize population
			Population population = ga.initPopulation(cities.length);
			// TODO: Evaluate population
			ga.evalPopulation(population, cities);
			// Keep track of current generation
			 int generation =1;
			// Start evolution loop
			 String result =new String();
			 
			while (ga.isTerminationConditionMet(generation, generations) == false) {
				// TODO: Print fittest individual from population
				Route route = new Route(population.getFittest(0), cities);
			//	setProgress(generations);
				progressbar.setValue(generation);
				publish ("");

						result+="G"+generation+"Best Distance: "+route.getDistance() + "\n";
				textArea.append("G" + generation + " Best distance: " + route.getDistance() + "\n");

				//System.out.println("G" + generation + " Best distance: " + route.getDistance());

				//  Apply crossover
				population = ga.crossoverPopulation(population);
				//  Apply mutation
				population = ga.mutatePopulation(population);
				//  Evaluate population
				ga.evalPopulation(population, cities);
				// Increment the current generation

				generation++;

			}
			
			
			// TODO: Display results
		//	System.out.println("Stopped after " + generations + " generations.");
			Route route = new Route(population.getFittest(0), cities);
			//System.out.println("Best distance: " + route.getDistance());
			String res = Double.toString(route.getDistance());

			//txtBestDistance.setText(res);
			

		

	return res;
} 
	protected void process(List<String> fragments) {
        for (String line : fragments) {
        	  if (isCancelled()) {
        	      break;
        	  }
	   //  progressbar.setValue(generations);
        }
  }   
	protected void done(){
		try {
			txtBestDistance.setText (get());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
