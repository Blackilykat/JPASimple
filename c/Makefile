.PHONY: all clean

all: target/java-pulse-simple.so

target/java-pulse-simple.so: java-pulse-simple.c | target
	gcc -shared -fPIC -o target/java-pulse-simple.so -lpulse-simple -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux java-pulse-simple.c

target:
	mkdir -p target

clean:
	rm -f target/java-pulse-simple.so
