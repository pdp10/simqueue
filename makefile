#
# MIT License
# 
# Copyright (c) 2005 Piero Dalle Pezze
# 
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.


JAVAC = javac
JAR = jar
JAVADOC = javadoc

sources = ./src/sim_queue/*.java
classes = ./sim_queue/*.class


all: clean compile jar

compile:
	$(JAVAC) $(sources) -d ./ -classpath ./sim_queue

jar:
	$(JAR) -cmf manifest.mf SimQueue.jar $(classes) $(sources) LICENSE readme.txt
	rm -rf sim_queue

doc:
	$(JAVADOC) -sourcepath ./src/sim_queue -d ./doc -classpath ./sim_queue -windowtitle "Source code documentation for SimQueue" ./src/sim_queue/*.java

clean:
	rm -f SimQueue.jar
	rm -rf doc/ 
	rm -rf bin/
	rm -rf *~
	rm -rf *.class
