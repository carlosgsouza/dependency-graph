package net.carlosgsouza.dependencygraph


class DependencyGraph {

	Map<String, Set<String>> dependencies
	
	public DependencyGraph() {
		this.dependencies = new HashMap<String, Set<String>>()
	}
	
	public void addDependency(String from, String to) {
		
	}
	
	public Iterable<String> getRootNodes() {
		
	}
}
