all: jed.java
	javac jed.java
	jar cvmf MANIFEST.MF jed.jar jed.class
run: all
	./jed
clean:
	rm jed.jar jed.class
