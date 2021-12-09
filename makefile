all:
	javac -d . src/jed.java src/cmd.java src/filebuffer.java
	jar cvmf src/MANIFEST.MF build/jed.jar jed/jed.class jed/cmd.class jed/filebuffer.class
run: all
	. build/jed
clean:
	rm -r build/jed.jar jed
