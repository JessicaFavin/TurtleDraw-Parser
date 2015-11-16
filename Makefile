JAVAC=javac
SOURCES = $(wildcard *.java)
CLASSES = $(SOURCES:.java=.class)
JFLEX = jflex
MAIN = Main
JVM = java

all: 
	$(JFLEX) *.flex
	$(JAVAC) *.java
		
flex: $(JFLEX) *.flex

clean:
	rm -f *.class

%.class: %.java
	$(JAVAC) $<

run: $(MAIN).class
	$(JVM) $(MAIN)

jar: $(CLASSES)
	jar cvmf MANIFEST.MF $(MAIN).jar $(CLASSES) logoTurtleDraw.png signature.png 
