package net.carlosgsouza.dependencygraph

import java.io.File.TempDirectory;

import spock.lang.Specification;

class DependencyGraphParserSpec extends Specification {

	DependencyGraphParser parser
	
	File tempFolder
	
	def setup() {
		parser = new DependencyGraphParser()
		createTempFolder()
	}
	
	def cleanup() {
		cleanTempFolder()
	}
	
	def "should parse an input file and create a dependency graph"() {
		given:
		File inputFile = new File(tempFolder, "input.txt")
		inputFile << """A->B
A->C
B->C
B->D
"""
		when:
		DependencyGraph dependencyGraph = parser.parse(inputFile.absolutePath)
		
		then:
		dependencyGraph.dependencies["A"] == ["B", "C"]
		dependencyGraph.dependencies["B"] == ["C", "D"]
	}
	
	def "should ignore white spaces"() {
		given:
		File inputFile = new File(tempFolder, "input.txt")
		inputFile << """
			
		A->B
A	->	C


"""
		when:
		DependencyGraph dependencyGraph = parser.parse(inputFile.absolutePath)
		
		then:
		dependencyGraph.rootNodes == ["A"]
		
		and:
		dependencyGraph.dependencies["A"] == ["B", "C"]
	}
	
	def "should support special characters"() {
		given:
		File inputFile = new File(tempFolder, "input.txt")
		inputFile << """
			
		#->á
@	->	ç


"""
		when:
		DependencyGraph dependencyGraph = parser.parse(inputFile.absolutePath)
		
		then:
		dependencyGraph.rootNodes == ["#", "@"]
		
		and:
		dependencyGraph.dependencies["A"] == ["á"]
		dependencyGraph.dependencies["@"] == ["ç"]
	}
	
	
	private void createTempFolder() {
		tempFolder = new File("src/test/resources/temp/")
		cleanTempFolder()
		tempFolder.mkdirs()
	}
	
	private void cleanTempFolder() {
		if(tempFolder?.exists()) {
			if(tempFolder.isDirectory()) {
				tempFolder.deleteDir()
			} else {
				tempFolder.delete()
			}
		}
	}
}

