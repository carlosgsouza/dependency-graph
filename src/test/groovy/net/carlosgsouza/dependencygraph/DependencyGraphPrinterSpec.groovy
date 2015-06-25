package net.carlosgsouza.dependencygraph

import spock.lang.Specification;

class DependencyGraphPrinterSpec extends Specification {

	DependencyGraphPrinter printer
	DependencyGraph dependencyGraph
	ByteArrayOutputStream output
	
	def setup() {
		printer = new DependencyGraphPrinter()
		dependencyGraph = new DependencyGraph()
		
		output = new ByteArrayOutputStream()
		printer.out = new PrintStream(output)
	}
	
	def "should print a dependency graph with one dependency"() {
		given:
		dependencyGraph.addDependency("A", "B")
		
		when:
		printer.print(dependencyGraph)
		
		then:
		output.toString() == """A
|_ B
"""
	}
	
	def "should print a dependency graph for A->B, A->C"() {
		given:
		dependencyGraph.addDependency("A", "B")
		dependencyGraph.addDependency("A", "C")
		
		when:
		printer.print(dependencyGraph)
		
		then:
		output.toString() == """A
|_ B
|_ C
"""
	}
	
	def "should print a dependency graph for A->B->C"() {
		given:
		dependencyGraph.addDependency("A", "B")
		dependencyGraph.addDependency("B", "C")
		
		when:
		printer.print(dependencyGraph)
		
		then:
		output.toString() == """A
|_ B
|_ C
   |_ C
"""
	}
	
	def "should print a dependency graph with words"() {
		given:
		dependencyGraph.addDependency("APPLE", "BANANA")
		dependencyGraph.addDependency("BANANA", "COCONUT")
		
		when:
		printer.print(dependencyGraph)
		
		then:
		output.toString() == """APPLE
|_ BANANA
   |_ COCONUT
"""
	}
	
	def "should mark circular dependencies"() {
		given:
		dependencyGraph.addDependency("A", "B")
		dependencyGraph.addDependency("A", "A")
		dependencyGraph.addDependency("B", "C")
		
		when:
		printer.print(dependencyGraph)
		
		then:
		output.toString() == """A
|_ A (!)
|_ B
   |_ A (!)

(!) Circular Dependency
"""
	}
	
	def "should print a complect dependency graph"() {
		given:
		dependencyGraph.addDependency("A", "B")
		dependencyGraph.addDependency("A", "J")
		dependencyGraph.addDependency("B", "C")
		dependencyGraph.addDependency("B", "D")
		dependencyGraph.addDependency("C", "E")
		dependencyGraph.addDependency("D", "F")
		dependencyGraph.addDependency("D", "G")
		dependencyGraph.addDependency("D", "J")
		dependencyGraph.addDependency("E", "H")
		dependencyGraph.addDependency("E", "M")
		dependencyGraph.addDependency("F", "H")
		dependencyGraph.addDependency("H", "L")
		dependencyGraph.addDependency("I", "O")
		dependencyGraph.addDependency("I", "P")
		dependencyGraph.addDependency("I", "K")
		dependencyGraph.addDependency("J", "I")
		dependencyGraph.addDependency("J", "Q")
		dependencyGraph.addDependency("K", "N")
		dependencyGraph.addDependency("K", "L")
		dependencyGraph.addDependency("L", "I")
		dependencyGraph.addDependency("M", "N")
		dependencyGraph.addDependency("M", "H")
		dependencyGraph.addDependency("O", "P")
		dependencyGraph.addDependency("P", "Q")
		
		when:
		printer.print(dependencyGraph)
		
		then:
		// TODO this is most likely not correct
		output.toString() == """A
|_ B
|  |_ C
|     |_ E
|     |  |_ H
|     |    |_ M
|     |       |_ N
|     |       |_ H
|     |          |_ M (!)
|	  |_ D
|   	 |_ F
|	     |  |_ H
|	     |     |_ L
|	     |        |_ 
|		 |_ G
|		 |_ J
|			|_ I
|   		   |_ O
|   		   |  |_ P
|   		   |     |_ Q
|   		   |_ P
|			   |  |_ Q
|			   |_ L
|			   |  |_ I (!)
|              |_ K
|                 |_ N
|                 |_ L
|_ J
   |_ I
      |_ O
      |  |_ P
      |     |_ Q
      |_ P
      |  |_ Q
      |_ L
   	  |_ I (!)
(!) Circular Dependency
"""
	}
	
	def "should print dependency graphs with multiple 'roots'"() {
		
	}
}

