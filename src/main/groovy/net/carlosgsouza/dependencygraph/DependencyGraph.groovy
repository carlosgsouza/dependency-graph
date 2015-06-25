package net.carlosgsouza.dependencygraph


class DependencyGraph {

	Map<String, Set<String>> dependencies
	
	public DependencyGraph() {
		this.dependencies = new HashMap<String, Set<String>>()
	}
	
	public void addDependency(String from, String to) {
		if(dependencies[from] == null) {
			dependencies[from] = [to]
		} else if(dependencies[from].contains(to) == false) {
			dependencies[from] << to
		}
	}
	
	public Iterable<String> getRootNodes() {
		List<String> result = dependencies.keySet().inject([]){ list, node -> list << node }
		
		dependencies.each { from, tos ->
			tos.each { to ->
				result.remove(to)
			}
		}
		return result
	}
}
