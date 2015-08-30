#include "jni_exports.h"

JNIEXPORT jlong JNICALL Java_ru_gvsmirnov_perv_labs_rekt_ChecksumCalculator_calculateChecksum
  (JNIEnv * jniEnv, jclass clazz, jstring filename) {
    printf("Hello, world!");
    return 1337;
}