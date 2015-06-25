package net.carlosgsouza.dependencygraph


class DependencyGraphParser {

	public DependencyGraph parse(String dependencyGraphPath) {
		if(!dependencyGraphPath) {
			throw new IllegalArgumentException("the dependency graph file path is required")
		}
		
		DependencyGraph result = new DependencyGraph()
		new File(dependencyGraphPath).eachLine { line ->
			List<String> from_to = line.split(/->/)*.trim()
			
			if(from_to.size() != 2) {
				return
			}
			
			result.addDependency(from_to[0], from_to[1])
		}
		
		return result
	}
	
}
