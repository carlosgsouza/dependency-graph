package net.carlosgsouza.dependencygraph

import spock.lang.Specification;

class DependencyGraphSpec extends Specification {

	DependencyGraph dependencyGraph
	
	def setup() {
		dependencyGraph = new DependencyGraph()
	}
	
	def "should build an empty dependency graph"() {
		expect: 
		dependencyGraph.root == null
		dependencyGraph.dependencies == [:]
	}
	
	def "should add dependencies to a dependency graph"() {
		when: 
		dependencyGraph.addDependency('A', 'B')
		dependencyGraph.addDependency('B', 'C')
		
		then: "the root is the first node"
		dependencyGraph.root == "A"
		
		and: "dependencies can be retrieved for a given node"
		dependencyGraph.dependencies["A"] == ["B"]
		dependencyGraph.dependencies["B"] == ["C"]
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
	
	def "should add unreachable nodes without complaining"() {
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
	
}
