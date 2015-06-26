package net.carlosgsouza.dependencygraph

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import spock.lang.Specification
import spock.lang.Timeout

class DependencyGraphPrinterPSpec extends Specification {

	DependencyGraph dependencyGraph
	DependencyGraphPrinter printer
	
	ByteArrayOutputStream output
	
	def setup() {
		dependencyGraph = new DependencyGraph()
		printer = new DependencyGraphPrinter()
		
		output = new ByteArrayOutputStream()
		printer.out = new PrintStream(output)
	}
	
	@Timeout(10)
	def "should be able to print a dependency graph with a huge depth"() {
		given:
		println "Generating dependency graph with height=100k"
		100000.times {
			dependencyGraph.addDependency("$it", "${it+1}")
		}
		
		when:
		println "Printing dependency graph"
		printer.print(dependencyGraph)
		
		then:
		notThrown Exception
	}
	
	@Timeout(10)
	def "should be able to print the same dependency graph in parallel"() {
		given:
		100.times {
			dependencyGraph.addDependency("$it", "${it+1}")
		}
		
		and:
		printer.print(dependencyGraph)
		String expectedResult = output.toString()
		
		and:
		int threads = 100
		ExecutorService pool = Executors.newFixedThreadPool(threads)
		
		and:
		List<ByteArrayOutputStream> outputs = []
		List<DependencyGraphPrinter> printers = []
		
		threads.times {
			DependencyGraphPrinter printer_i = new DependencyGraphPrinter()
			ByteArrayOutputStream output_i = new ByteArrayOutputStream()
			
			printers << printer_i
			outputs << output_i
			
			printer_i.out = new PrintStream(output_i)
		}
		
		when: "the same graph is printer by 100 printers in parallel"
		threads.times { i ->
			pool.submit({
				printers[i].print(dependencyGraph)
			} as Callable)
		}
		pool.shutdown()
		pool.shutdownNow()
		
		then: "all printers complete successfuly"
		outputs.every{ it.toString() == expectedResult }
	}
}
