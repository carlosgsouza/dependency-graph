package net.carlosgsouza.dependencygraph


class DependencyGraphService {

	DependencyGraphParser parser
	DependencyGraphPrinter printer
	
	public DependencyGraphService() {
		parser = new DependencyGraphParser()
		printer = new DependencyGraphPrinter() 
	}
	
	public void process(String dependencyGraphPath) {
		DependencyGraph dependencyGraph = parser.parse(dependencyGraphPath)
		printer.print(dependencyGraph)
	}
}
