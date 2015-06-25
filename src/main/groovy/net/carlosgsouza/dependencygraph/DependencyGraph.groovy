package net.carlosgsouza.dependencygraph


class DependencyGraph {

	String root
	Map<String, Set<String>> dependencies
	
	public DependencyGraph() {
		this.root = null
		this.dependencies = new HashMap<String, Set<String>>()
	}
	
	public void addDependency(String from, String to) {
		
	}
}
