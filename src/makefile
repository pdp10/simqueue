
# SimulatedQueue makefile
# License: MIT
# Year: 2005


SimulatedQueue: SimulatedQueue.java  
	#javac -d ../ *.java
	javac *.java -d ./ -classpath ./
	jar cvf SimulatedQueue.jar ./*.class
	mv SimulatedQueue.jar ../
	rm -rf *.class
	@echo "Simulated Queue created!"

doc:
	javadoc -d ../doc *.java
	@echo "Created java documentation!"

clean:
	rm -rf ../*.jar
	rm -rf *.class
	rm -rf *~
	rm -rf ../doc
	@echo "Removed temporary files!"
