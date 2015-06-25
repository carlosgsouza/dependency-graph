package net.carlosgsouza.dependencygraph

import spock.lang.Specification
import spock.lang.Unroll

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
	
	@Unroll
	def "should fail when the given file path #description"() {
		when:
		parser.parse(filePath)
		
		then:
		thrown Exception
		
		where: 
		filePath				| description
		null					| "is null"
		""						| "is empty"
		"inexistent/file.txt"	| "points to inexistent file"
		"src/test/resources/"	| "points to a directory"
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
	
	def "should ignore white spaces and invalid lines"() {
		given:
		File inputFile = new File(tempFolder, "input.txt")
		inputFile << """
			
		A->B
A	->	C
D->E->F
this is an invalid line

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
		dependencyGraph.rootNodes.containsAll(["#", "@"])
		dependencyGraph.rootNodes.size() == 2
		
		and:
		dependencyGraph.dependencies["#"] == ["á"]
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

