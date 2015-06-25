package net.carlosgsouza.dependencygraph

import spock.lang.Specification;

class DependencyGraphSpec extends Specification {

	DependencyGraph dependencyGraph
	
	def setup() {
		dependencyGraph = new DependencyGraph()
	}
	
	def "should build an empty dependency graph"() {
		expect: 
		dependencyGraph.dependencies == [:]
	}
	
	def "should add dependencies to a dependency graph"() {
		when: 
		dependencyGraph.addDependency('A', 'B')
		dependencyGraph.addDependency('B', 'C')
		
		then: 
		dependencyGraph.dependencies["A"] == ["B"]
		dependencyGraph.dependencies["B"] == ["C"]
		
		and:
		dependencyGraph.rootNodes == ["A"]
	}
	
	def "should create a dependency graph with multiple root nodes"() {
		when: 
		dependencyGraph.addDependency('A', 'B')
		dependencyGraph.addDependency('B', 'C')
		dependencyGraph.addDependency('D', 'E')
		dependencyGraph.addDependency('F', 'E')
		dependencyGraph.addDependency('G', 'H')
		
		then: "all nodes that don't with no other depending nodes are considering root nodes" 
		dependencyGraph.rootNodes.containsAll(["A", "D", "F", "G"])
		dependencyGraph.rootNodes.size() == 4
	}
	
	def "should ignore duplicate dependencies"() {
		given: 
		dependencyGraph.addDependency('A', 'B')
		
		expect:
		dependencyGraph.dependencies["A"] == ["B"]
		
		when:
		dependencyGraph.addDependency('A', 'B')
		dependencyGraph.addDependency('A', 'B')
		dependencyGraph.addDependency('A', 'B')
		
		then: 
		dependencyGraph.dependencies["A"] == ["B"]
	}
	
	def "should add circular dependencies without complaining"() {
		given:
		dependencyGraph.addDependency("A", "B")
		dependencyGraph.addDependency("B", "C")
		
		when:
		dependencyGraph.addDependency("C", "A")
		
		then: 
		dependencyGraph.dependencies["A"] == ["B"]
		dependencyGraph.dependencies["B"] == ["C"]
		dependencyGraph.dependencies["C"] == ["A"]
	}
	
	def "should add new 'root' nodes"() {
		given:
		dependencyGraph.addDependency("A", "B")
		dependencyGraph.addDependency("B", "C")
		
		when: 
		dependencyGraph.addDependency("X", "Y")
		
		then: 
		dependencyGraph.dependencies["A"] == ["B"]
		dependencyGraph.dependencies["B"] == ["C"]
		dependencyGraph.dependencies["X"] == ["Y"]
	}
	
	def "should consider nodes whose names have different cases different nodes"() {
		given:
		dependencyGraph.addDependency("A", "B")
		
		when:
		dependencyGraph.addDependency("A", "a")
		dependencyGraph.addDependency("a", "b")
		dependencyGraph.addDependency("b", "B")
		
		then:
		dependencyGraph.dependencies["A"] == ["B", "a"]
		dependencyGraph.dependencies["a"] == ["b"]
		dependencyGraph.dependencies["b"] == ["B"]
	}
	
	def "should support special characters and words"() {
		when:
		dependencyGraph.addDependency("a.b.c", "#")
		dependencyGraph.addDependency("#", "éã")
		dependencyGraph.addDependency("junit:junit:4.12", "org.hamcrest:hamcrest-core:1.3")
		
		then:
		dependencyGraph.dependencies["a.b.c"] == ["#"]
		dependencyGraph.dependencies["#"] == ["éã"]
	}
	
	def "should provide a toString() implementation"() {
		when:
		dependencyGraph.addDependency('A', 'B')
		dependencyGraph.addDependency('B', 'C')
		
		then:
		dependencyGraph.toString() == dependencyGraph.dependencies.toString()
	}
	
	def "should consider the first node as a root node even if there are other nodes which depend on it"() {
		when:
		dependencyGraph.addDependency("A", "B")
		dependencyGraph.addDependency("B", "A")
		dependencyGraph.addDependency("C", "D")
		
		then: "the first root node is the first node, even through there are nodes which depend on it"
		dependencyGraph.rootNodes == ["A", "C"]
	}
	
	
	
}
