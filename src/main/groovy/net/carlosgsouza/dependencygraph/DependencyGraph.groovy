package net.carlosgsouza.dependencygraph


class DependencyGraph {

	Map<String, Set<String>> dependencies
	String firstNode
	
	public DependencyGraph() {
		this.dependencies = new TreeMap<String, Set<String>>()
		this.firstNode = null
	}
	
	public void addDependency(String from, String to) {
		if(dependencies[from] == null) {
			dependencies[from] = [to]
		} else if(dependencies[from].contains(to) == false) {
			dependencies[from] << to
		}
		
		firstNode = firstNode ?: from
	}
	
	public List<String> getRootNodes() {
		List<String> result = dependencies.keySet().inject([]){ list, node -> list << node }
		
		dependencies.each { from, tos ->
			tos.each { to ->
				result.remove(to)
			}
		}
		
		// Makes sure that the first node is a root node even if there are nodes which depend on it (which implies there is a circular dependency) 
		if(result.contains(firstNode) == false) {
			result.add(0, firstNode)
		}
		
		return result 
	}
	
	@Override
	public String toString() {
		dependencies?.toString()
	}
}
