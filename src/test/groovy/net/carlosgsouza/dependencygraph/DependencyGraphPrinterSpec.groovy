package net.carlosgsouza.dependencygraph

import spock.lang.Specification;

class DependencyGraphPrinterSpec extends Specification {

	DependencyGraphService service
	
	def setup() {
		service = new DependencyGraphService()
		service.parser = Mock(DependencyGraphParser)
		service.printer = Mock(DependencyGraphParser)
	}
	
	def "should parse input file, create a dependency graph, and print it"() {
		given:
		DependencyGraph graph = Mock(DependencyGraph)
		
		when:
		service.printDependencyGraph("input.txt")
		
		then:
		1 * service.parser.parse("input.txt") >> graph
		1 * service.printer.print(graph)
	}
}
