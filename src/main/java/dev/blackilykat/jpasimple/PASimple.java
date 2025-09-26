package dev.blackilykat.jpasimple;

import javax.naming.OperationNotSupportedException;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PASimple implements Closeable, AutoCloseable {
	private static boolean pulseSupported = true;

	private boolean open = true;
	private final long c_pa_simple;

	/**
	 * @param server Server name, or null for default
	 * @param name A descriptive name for this client (application name, ...), can be null
	 * @param isRecording whether this stream is recording audio  (true) or playing audio (false)
	 * @param device Sink (resp. source) name, or null for default
	 * @param streamName A descriptive name for this stream (application name, song title, ...), cannot be null
	 * @param spec The sample type to use, cannot be null
	 * @param bufferAttributes Buffering attributes, or null for default
	 * @throws OperationNotSupportedException if pulseaudio is not installed
	 */
	public PASimple(String server, String name, boolean isRecording, String device, String streamName, SampleSpec spec, BufferAttributes bufferAttributes) throws OperationNotSupportedException {
		if(!pulseSupported) throw new OperationNotSupportedException();

		if(spec == null) throw new IllegalArgumentException("spec cannot be null");

		c_pa_simple = c_pa_simple_new(server, name, isRecording, device, streamName, spec, bufferAttributes);
	}

	/**
	 * Close and free the connection to the server. The connection becomes invalid when this is called.
	 */
	@Override
	public void close() {
		open = false;
		c_pa_simple_free(c_pa_simple);
	}

	/**
	 * Write some data to the server.
	 */
	public void write(byte[] data) throws PulseAudioException {
		write(data, 0, data.length);
	}

	/**
	 * Write some data to the server.
	 */
	public void write(byte[] data, int offset, int bytes) throws PulseAudioException {
		if(!open) throw new IllegalStateException("Stream is closed");

		int res = c_pa_simple_write(c_pa_simple, data, offset, bytes);
		if(res < 0) throw new PulseAudioException();
	}

	/**
	 * Read some data from the server. This function blocks until {@code data.length} bytes amount of data has been
	 * received from the server, or until an error occurs.
	 */
	public void read(byte[] data) throws PulseAudioException {
		read(data, 0, data.length);
	}

	/**
	 * Read some data from the server. This function blocks until {@code bytes} bytes amount of data has been received
	 * from the server, or until an error occurs.
	 */
	public void read(byte[] data, int offset, int bytes) throws PulseAudioException {
		if(!open) throw new IllegalStateException("Stream is closed");

		int res = c_pa_simple_read(c_pa_simple, data, offset, bytes);
		if(res < 0) throw new PulseAudioException();
	}

	/**
	 * Wait until all data already written is played by the daemon
	 */
	public void drain() throws PulseAudioException {
		if(!open) throw new IllegalStateException("Stream is closed");

		int res = c_pa_simple_drain(c_pa_simple);
		if(res < 0) throw new PulseAudioException();
	}

	/**
	 * @return the playback or record latency
	 */
	public long getLatency() {
		if(!open) throw new IllegalStateException("Stream is closed");

		return c_pa_simple_get_latency(c_pa_simple);
	}

	public void flush() throws PulseAudioException {
		if(!open) throw new IllegalStateException("Stream is closed");


		int res = c_pa_simple_flush(c_pa_simple);
		if(res < 0) throw new PulseAudioException();
	}

	public static boolean isPulseSupported() {
		return pulseSupported;
	}

	static {
		try(InputStream library = PASimple.class.getResourceAsStream("/java-pulse-simple.so")) {
			//noinspection DataFlowIssue (the NullPointerException is caught)
			byte[] bytes = library.readAllBytes();
			File tempFile = File.createTempFile("java-pulse-simple", ".so");
			try(FileOutputStream os = new FileOutputStream(tempFile)) {
				os.write(bytes);
			}

			System.load(tempFile.getAbsolutePath());
		} catch(UnsatisfiedLinkError | IOException | NullPointerException e) {
			pulseSupported = false;
		}
	}

	private static native long c_pa_simple_new(String server, String name, boolean isRecording, String device, String streamName, SampleSpec spec, BufferAttributes bufferAttributes);
	private static native void c_pa_simple_free(long c_pa_simple);
	private static native int c_pa_simple_write(long c_pa_simple, byte[] data, int offset, int bytes);
	private static native int c_pa_simple_drain(long c_pa_simple);
	private static native int c_pa_simple_read(long c_pa_simple, byte[] data, int offset, long bytes);
	private static native long c_pa_simple_get_latency(long c_pa_simple);
	private static native int c_pa_simple_flush(long c_pa_simple);
}
