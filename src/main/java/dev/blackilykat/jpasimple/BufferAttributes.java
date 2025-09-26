package dev.blackilykat.jpasimple;

public class BufferAttributes {

	/**
	 * Maximum length of the buffer in bytes. Setting this to -1
	 * will initialize this to the maximum value supported by server,
	 * which is recommended.
	 * <p>
	 * In strict low-latency playback scenarios you might want to set this to
	 * a lower value, likely together with the PA_STREAM_ADJUST_LATENCY flag.
	 * If you do so, you ensure that the latency doesn't grow beyond what is
	 * acceptable for the use case, at the cost of getting more underruns if
	 * the latency is lower than what the server can reliably handle.
	 */
	public int maxLength = -1;


	/**
	 * Playback only: target length of the buffer. The server tries
	 * to assure that at least targetLength bytes are always available in
	 * the per-stream server-side playback buffer. The server will
	 * only send requests for more data as long as the buffer has
	 * less than this number of bytes of data.
	 * <p>
	 * It is recommended to set this to -1, which will
	 * initialize this to a value that is deemed sensible by the
	 * server. However, this value will default to something like 2s;
	 * for applications that have specific latency requirements
	 * this value should be set to the maximum latency that the
	 * application can deal with.
	 */
	public int targetLength = -1;

	/**
	 * Playback only: pre-buffering. The server does not start with
     * playback before at least preBuffer bytes are available in the
     * buffer. It is recommended to set this to -1, which
     * will initialize this to the same value as {@link #targetLength}, whatever
     * that may be.
     * <p>
	 * pa_simple does not support setting this to 0.
	 */
	public int preBuffer = -1;

	/**
	 * Playback only: minimum request. The server does not request
	 * less than minimumRequest bytes from the client, instead waits until the
	 * buffer is free enough to request more bytes at once. It is
	 * recommended to set this to -1, which will initialize
	 * this to a value that is deemed sensible by the server. This
	 * should be set to a value that gives PulseAudio enough time to
	 * move the data from the per-stream playback buffer into the
	 * hardware playback buffer.
	 */
	public int minimumRequest = -1;


	/**
	 * Recording only: fragment size. The server sends data in
	 * blocks of fragmentSize bytes size. Large values diminish
	 * interactivity with other operations on the connection context
	 * but decrease control overhead. It is recommended to set this to
	 * -1, which will initialize this to a value that is
	 * deemed sensible by the server. However, this value will default
	 * to something like 2s; For applications that have specific
	 * latency requirements this value should be set to the maximum
	 * latency that the application can deal with.
	 */
	public int fragmentSize = -1;

}
