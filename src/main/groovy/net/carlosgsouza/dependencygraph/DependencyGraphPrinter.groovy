package net.carlosgsouza.dependencygraph

class DependencyGraphPrinter {
	
	PrintStream out = System.out

	public void print(DependencyGraph dependencyGraph) {
		if(dependencyGraph == null) {
			throw new IllegalArgumentException("dependencyGraph is required")
		}
		
		Map<String, Boolean> isAncestor = [:]
		
		// Allows us to pass a parameter by "reference" in Java
		// We don't use a field for this since that would make this routine thread-unsafe 
		MutableBoolean circularDependencyFound = new MutableBoolean(false)
		
		dependencyGraph.rootNodes.each { root ->
			out.println(root)
			
			isAncestor[root] = true
			printChildren("", root, dependencyGraph, isAncestor, circularDependencyFound)
			isAncestor[root] = false
		}
		
		if(circularDependencyFound.value == true) {
			out.println("\n(!) Circular Dependency")
		}
	}
	
	private printChildren(String prefix, String node, DependencyGraph dependencyGraph, Map<String, Boolean> isAncestor, MutableBoolean circularDependencyFound) {
		int childrenCount = dependencyGraph.dependencies[node]?.size() ?: 0
		if(childrenCount == 0) {
			return
		}
		
		(childrenCount-1).times { i ->
			printChild(dependencyGraph.dependencies[node][i], prefix, "|  ", dependencyGraph, isAncestor, circularDependencyFound)
		}
		printChild(dependencyGraph.dependencies[node][-1], prefix, "   ", dependencyGraph, isAncestor, circularDependencyFound)
	}
	
	private printChild(String child, String prefix, String appendedPrefix, DependencyGraph dependencyGraph, Map<String, Boolean> isAncestor, MutableBoolean circularDependencyFound) {
		boolean circularDependency = isAncestor[child]
		
		out.println("$prefix|_ $child" + (circularDependency ? " (!)" : ""))
		
		if(!circularDependency) {
			isAncestor[child] = true
			printChildren(prefix + appendedPrefix, child, dependencyGraph, isAncestor, circularDependencyFound)
			isAncestor[child] = false
		} 
		
		circularDependencyFound.value = circularDependencyFound.value | circularDependency
	}
	
	static class MutableBoolean {
		boolean value
		
		public MutableBoolean(boolean value) {
			this.value = value
		}
	}
}
