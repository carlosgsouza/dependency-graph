package net.carlosgsouza.dependencygraph

import spock.lang.Specification

class DependencyGraphServiceISpec extends Specification {

	def "should run the command line application"() {
		when:
		Process process = "./build/install/dependency-graph/bin/dependency-graph src/test/resources/input2.txt".execute()
		
		then:
		process.text == """A
|_ B
|  |_ C
|  |  |_ E
|  |     |_ H
|  |     |  |_ L
|  |     |     |_ I
|  |     |        |_ O
|  |     |        |  |_ P
|  |     |        |     |_ Q
|  |     |        |_ P
|  |     |        |  |_ Q
|  |     |        |_ K
|  |     |           |_ N
|  |     |           |_ L (!)
|  |     |_ M
|  |        |_ N
|  |        |_ H
|  |           |_ L
|  |              |_ I
|  |                 |_ O
|  |                 |  |_ P
|  |                 |     |_ Q
|  |                 |_ P
|  |                 |  |_ Q
|  |                 |_ K
|  |                    |_ N
|  |                    |_ L (!)
|  |_ D
|     |_ F
|     |  |_ H
|     |     |_ L
|     |        |_ I
|     |           |_ O
|     |           |  |_ P
|     |           |     |_ Q
|     |           |_ P
|     |           |  |_ Q
|     |           |_ K
|     |              |_ N
|     |              |_ L (!)
|     |_ G
|     |_ J
|        |_ I
|        |  |_ O
|        |  |  |_ P
|        |  |     |_ Q
|        |  |_ P
|        |  |  |_ Q
|        |  |_ K
|        |     |_ N
|        |     |_ L
|        |        |_ I (!)
|        |_ Q
|_ J
   |_ I
   |  |_ O
   |  |  |_ P
   |  |     |_ Q
   |  |_ P
   |  |  |_ Q
   |  |_ K
   |     |_ N
   |     |_ L
   |        |_ I (!)
   |_ Q

(!) Circular Dependency
"""
	}
}
