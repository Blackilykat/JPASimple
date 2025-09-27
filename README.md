# JPASimple

JNI bindings for the pa-simple library to play and record audio through PulseAudio.

## Usage

Here's an example playing back a basic sine wave:
```java
try(PASimple stream = new PASimple(
        null,              // Server (null = default)
        "Application",     // Application name
        false,             // this is not a recording stream (but a playback one)
        null,              // device name (null = default)
        "stream",          // stream name (e.g. song title)
        new SampleSpec(
                SampleFormat.S16LE,     // format
                44100,                  // sample rate
                (short) 1               // channels
        ),
        null)              // buffer attributes (null = default)
) {
    byte[] buffer = new byte[8800];
    while(shouldPlay) {
        for(int i = 0; i < buffer.length / 2; i++) {
            short sample = (short) (Math.sin(i / 50.0) * 100);
            buffer[i * 2] = (byte) (sample >> 8);
            buffer[i * 2 + 1] = (byte) (sample & 0xFF);
        }
        stream.write(buffer);
        // or stream.write(data, offset, length);
    }
}
```


## Building

### Build the C code:
 
Ensure you have gcc, java and pulseaudio's development packages installed (you'll get errors if you don't)

(in `./c`):
```bash
make
```

### Build the java library

The java side won't work unless you build the C code beforehand. Make sure you do that!

(in the project root):
```bash
./gradlew build
```

The library will be in `build/libs/JPASimple-1.0.jar`.

