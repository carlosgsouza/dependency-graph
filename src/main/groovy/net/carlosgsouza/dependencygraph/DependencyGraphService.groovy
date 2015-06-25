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
	
	public void run(String[] args) {
		if(args == null || args.size() != 1) {
			println "Exactly one parameter is required, the path of the input file"
			return
		}
		
		process(args[0])
	}
	
	public static void main(String[] args) {
		new DependencyGraphService().run(args)
	}
}
