package dev.blackilykat.jpasimple;

public class SampleSpec {
	public final SampleFormat format;
	public final int rate;
	public final short channels;

	public SampleSpec(SampleFormat format, int rate, short channels) {
		this.format = format;
		this.rate = rate;
		this.channels = channels;
	}
}
