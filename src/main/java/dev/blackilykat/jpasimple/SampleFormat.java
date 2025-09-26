package dev.blackilykat.jpasimple;

public enum SampleFormat {
	/**
	 * Unsigned 8 Bit PCM
	 */
	U8(0),

	/**
	 * 8 Bit a-Law
	 */
	ALAW(1),

	/**
	 * 8 Bit mu-Law
	 */
	ULAW(2),

	/**
	 * Signed 16 Bit PCM, Little endian (PC)
	 */
	S16LE(3),

	/**
	 * Signed 16 Bit PCM, big endian
	 */
	S16BE(4),

	/**
	 * 32 Bit IEEE floating point, little endian (PC), range -1.0 to 1.0
	 */
	FLOAT32LE(5),

	/**
	 * 32 Bit IEEE floating point, big endian, range -1.0 to 1.0
	 */
	FLOAT32BE(6),

	/**
	 * Signed 32 Bit PCM, little endian (PC)
	 */
	S32LE(7),

	/**
	 * Signed 32 Bit PCM, big endian
	 */
	S32BE(8),

	/**
	 * Signed 24 Bit PCM, little endian (pc)
	 */
	S24LE(9),

	/**
	 * Signed 24 Bit PCM, big endian
	 */
	S24BE(10),

	/**
	 * Signed 24 Bit PCM in LSB of 32 Bit words, little endian (PC)
	 */
	S24_32LE(11),

	/**
	 * Signed 24 Bit PCM in LSB of 32 Bit words, big endian
	 */
	S24_32BE(12),

	/**
	 * Upper limit of valid sample types
	 */
	MAX(13),

	/**
	 * An invalid value
	 */
	INVALID(-1);

	final int index;

	SampleFormat(int index) {
		this.index = index;
	}
}
