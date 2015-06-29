package net.carlosgsouza.dependencygraph

class DependencyGraphPrinter {
	
	PrintStream out = System.out

	public void print(DependencyGraph dependencyGraph) {
		if(dependencyGraph == null) {
			throw new IllegalArgumentException("dependencyGraph is required")
		}
		
		LinkedList<NodeJob> S = new LinkedList<NodeJob>()
		Map<String, Boolean> isAncestor = [:]
		boolean circularDependencyFound = false
		
		dependencyGraph.rootNodes.each { node ->
			out.println node
			
			isAncestor[node] = true
			createJobsForChildren(S, dependencyGraph, node, "")
			
			while(S.isEmpty() == false) {
				NodeJob job = S.removeLast()
				
				if(job.printed == false) {				
					boolean circularDependency = isAncestor[job.node]
					
					if(!circularDependency) {
						job.printed = true
						S << job
						isAncestor[job.node] = true
						
						createJobsForChildren(S, dependencyGraph, job.node, job.childrenPrefix)
					} else {
						circularDependencyFound = true
					}
					
					out.println job.prefix + "|_ " + job.node + (circularDependency ? " (!)" : "")
				} else {
					isAncestor[job.node] = false
				}
			}
			
			isAncestor[node] = false
		}
		
		if(circularDependencyFound == true) {
			out.println("\n(!) Circular Dependency")
		}
	}

	private createJobsForChildren(LinkedList S, DependencyGraph dependencyGraph, String node, String prefix) {
		if(dependencyGraph.dependencies[node]) {
			S << new NodeJob(printed:false, node:dependencyGraph.dependencies[node][-1], prefix:prefix, childrenPrefix:prefix+"   ")

			for(int i = dependencyGraph.dependencies[node].size()-2; i >= 0; i--) {
				S << new NodeJob(printed:false, node:dependencyGraph.dependencies[node][i], prefix:prefix, childrenPrefix:prefix+"|  ")
			}
		}
	}
		
	static class MutableBoolean {
		boolean value
		
		public MutableBoolean(boolean value) {
			this.value = value
		}
	}
	
	static class NodeJob {
		boolean printed = false
		String node
		
		String childrenPrefix
		String prefix
		
		public String toString() {
			node + (printed ? "*" : "")
		}
	}
}
