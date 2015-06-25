package net.carlosgsouza.dependencygraph

import spock.lang.Specification;

class DependencyGraphSpec extends Specification {

	def "should build a dependency graph given the dependencies between components"() {
		when: "a new dependency graph is created"
		DependencyGraph dependencyGraph = new DependencyGraph()
		
		then: "it contains no nodes"
		dependencyGraph.root == null
		
		when: "dependencies are added"
		dependencyGraph.addDependency('A', 'B')
		dependencyGraph.addDependency('B', 'C')
		
		then: "the root is the first node"
		dependencyGraph.root == "A"
		
		and: "dependencies can be retrieved for a given node"
		dependencyGraph.dependencies["A"] == ["B"]
		dependencyGraph.dependencies["B"] == ["C"]
		
		when: "repeated dependencies are added again"
		dependencyGraph.addDependency('A', 'B')
		dependencyGraph.addDependency('A', 'B')
				
		then: "repeated dependencies are not kept"
		dependencyGraph.dependencies["A"] == ["B"]
		
		when: "circular dependencies are added, such as A->B->C->A"
		dependencyGraph.addDependency("C", "A")
		
		then: "the dependencyGraph does not complain"
		dependencyGraph.dependencies["A"] == ["B"]
		dependencyGraph.dependencies["B"] == ["C"]
		dependencyGraph.dependencies["C"] == ["A"]
		
		when: "unreachable depencies are added"
		dependencyGraph.addDependency("X", "Y")
		
		then: "the dependencyGraph does not complain"
		dependencyGraph.dependencies["A"] == ["B"]
		dependencyGraph.dependencies["B"] == ["C"]
		dependencyGraph.dependencies["C"] == ["A"]
		dependencyGraph.dependencies["X"] == ["Y"]
	}
	
}
