#include <pulse/def.h>
#include <pulse/sample.h>
#include <pulse/simple.h>
#include <jni.h>
#include <stdlib.h>


jlong Java_dev_blackilykat_jpasimple_PASimple_c_1pa_1simple_1new(JNIEnv *env, jclass clazz, jstring server, jstring name, jboolean isRecording, jstring device, jstring streamName, jobject spec, jobject bufferAttributes) {
	const char *nativeServer = NULL;
	if(server != NULL) nativeServer = (*env)->GetStringUTFChars(env, server, NULL);
	const char *nativeName = NULL;
	if(name != NULL) nativeName = (*env)->GetStringUTFChars(env, name, NULL);
	const char *nativeDevice = NULL;
	if(device != NULL) nativeDevice = (*env)->GetStringUTFChars(env, device, NULL);

	if(streamName == NULL) {
		jclass exceptionClass = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
		return (*env)->ThrowNew(env, exceptionClass, "streamName cannot be null");
	}
	const char *nativeStreamName = (*env)->GetStringUTFChars(env, streamName, NULL);

	if(spec == NULL) {
		jclass exceptionClass = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
		return (*env)->ThrowNew(env, exceptionClass, "spec cannot be null");
	}
	jclass specClass = (*env)->GetObjectClass(env, spec);

	jobject jformat = (*env)->GetObjectField(env, spec, (*env)->GetFieldID(env, specClass, "format", "Ldev/blackilykat/jpasimple/SampleFormat;"));

	if(jformat == NULL) {
		jclass exceptionClass = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
		return (*env)->ThrowNew(env, exceptionClass, "spec.format cannot be null");
	}

	jclass formatClass = (*env)->GetObjectClass(env, jformat);
	jint formatIndex = (*env)->GetIntField(env, jformat, (*env)->GetFieldID(env, formatClass, "index", "I"));


	pa_sample_spec ss;
	switch(formatIndex) {
		case 0: ss.format = PA_SAMPLE_U8; break;
		case 1: ss.format = PA_SAMPLE_ALAW; break;
		case 2: ss.format = PA_SAMPLE_ULAW; break;
		case 3: ss.format = PA_SAMPLE_S16LE; break;
		case 4: ss.format = PA_SAMPLE_S16BE; break;
		case 5: ss.format = PA_SAMPLE_FLOAT32LE; break;
		case 6: ss.format = PA_SAMPLE_FLOAT32BE; break;
		case 7: ss.format = PA_SAMPLE_S32LE; break;
		case 8: ss.format = PA_SAMPLE_S32BE; break;
		case 9: ss.format = PA_SAMPLE_S24LE; break;
		case 10: ss.format = PA_SAMPLE_S24BE; break;
		case 11: ss.format = PA_SAMPLE_S24_32LE; break;
		case 12: ss.format = PA_SAMPLE_S24_32BE; break;
		case 13: ss.format = PA_SAMPLE_MAX; break;
		case -1: ss.format = PA_SAMPLE_INVALID; break;
		default:
			jclass exceptionClass = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
			return (*env)->ThrowNew(env, exceptionClass, "Unknown sample format");
	}

	ss.rate = (uint32_t) (*env)->GetIntField(env, spec, (*env)->GetFieldID(env, specClass, "rate", "I"));
	ss.channels = (uint8_t) (*env)->GetShortField(env, spec, (*env)->GetFieldID(env, specClass, "channels", "S"));

	pa_buffer_attr attributes;
	char hasAttributes = 0;
	if(bufferAttributes != NULL) {
		hasAttributes = 1;

		jclass attributesClass = (*env)->GetObjectClass(env, bufferAttributes);

		attributes.maxlength = (*env)->GetIntField(env, bufferAttributes, (*env)->GetFieldID(env, attributesClass, "maxLength",      "I"));
		attributes.tlength =   (*env)->GetIntField(env, bufferAttributes, (*env)->GetFieldID(env, attributesClass, "targetLength",   "I"));
		attributes.prebuf =    (*env)->GetIntField(env, bufferAttributes, (*env)->GetFieldID(env, attributesClass, "preBuffer",      "I"));
		attributes.minreq =    (*env)->GetIntField(env, bufferAttributes, (*env)->GetFieldID(env, attributesClass, "minimumRequest", "I"));
		attributes.fragsize =  (*env)->GetIntField(env, bufferAttributes, (*env)->GetFieldID(env, attributesClass, "fragmentSize",   "I"));
	}

	jlong ret = (jlong) pa_simple_new(nativeServer, nativeName, isRecording ? PA_DIRECTION_INPUT : PA_DIRECTION_OUTPUT, nativeDevice, nativeStreamName, &ss, NULL, hasAttributes ? &attributes : NULL, NULL);

	(*env)->ReleaseStringUTFChars(env, server, nativeServer);
	(*env)->ReleaseStringUTFChars(env, name, nativeName);
	(*env)->ReleaseStringUTFChars(env, device, nativeDevice);
	(*env)->ReleaseStringUTFChars(env, streamName, nativeStreamName);

	return ret;
}


void Java_dev_blackilykat_jpasimple_PASimple_c_1pa_1simple_1free(JNIEnv *env, jclass clazz, jlong c_pa_simple) {
	pa_simple_free((pa_simple*) c_pa_simple);
}

jint Java_dev_blackilykat_jpasimple_PASimple_c_1pa_1simple_1write(JNIEnv *env, jclass clazz, jlong c_pa_simple, jbyteArray data, jint offset, jint bytes) {
	jsize dataLen = (*env)->GetArrayLength(env, data);
	if(offset < 0 || bytes < 0 || offset + bytes > dataLen) {
		jclass exceptionClass = (*env)->FindClass(env, "java/lang/IndexOutOfBoundsException");
		return (*env)->ThrowNew(env, exceptionClass, "Invalid combination of array length, offset and given size");
	}

	jbyte *elements = malloc(bytes);

	(*env)->GetByteArrayRegion(env, data, offset, bytes, elements);
	jlong ret = pa_simple_write((pa_simple*) c_pa_simple, elements, bytes, NULL);

	free(elements);

	return ret;
}

jint Java_dev_blackilykat_jpasimple_PASimple_c_1pa_1simple_1drain(JNIEnv *env, jclass clazz, jlong c_pa_simple) {
	return pa_simple_drain((pa_simple*) c_pa_simple, NULL);
}

jint Java_dev_blackilykat_jpasimple_PASimple_c_1pa_1simple_1read(JNIEnv *env, jclass clazz, jlong c_pa_simple, jbyteArray data, jint offset, jlong bytes) {
	jsize dataLen = (*env)->GetArrayLength(env, data);
	if(offset < 0 || bytes < 0 || offset + bytes > dataLen) {
		jclass exceptionClass = (*env)->FindClass(env, "java/lang/IndexOutOfBoundsException");
		return (*env)->ThrowNew(env, exceptionClass, "Invalid combination of array length, offset and given size");
	}

	jbyte *elements = malloc(bytes);

	int ret = pa_simple_read((pa_simple*) c_pa_simple, elements, bytes, NULL);
	(*env)->SetByteArrayRegion(env, data, offset, bytes, elements);

	free(elements);

	return ret;
}

jlong Java_dev_blackilykat_jpasimple_PASimple_c_1pa_1simple_1get_1latency(JNIEnv *env, jclass clazz, jlong c_pa_simple) {
	return pa_simple_get_latency((pa_simple*) c_pa_simple, NULL);
}

jint Java_dev_blackilykat_jpasimple_PASimple_c_1pa_1simple_1flush(JNIEnv *env, jclass clazz, jlong c_pa_simple) {
	return pa_simple_flush((pa_simple*) c_pa_simple, NULL);
}

