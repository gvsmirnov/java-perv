#include "jni_exports.h"

#ifndef MAX_FILE_NAME_LENGTH
// 17 is strlen(123.txt.digested) + 1 for the terminating zero byte
#define MAX_FILE_NAME_LENGTH 17
#endif

int digest(char *src_filename, char *dst_filename) {
  // For this demonstration, we do not actually need to do anything here
  // Assume that this function opens src file, calculates the checksum
  // and writes it to dst_filename.
  return rand();
}

JNIEXPORT jlong JNICALL Java_ru_gvsmirnov_perv_labs_rekt_ChecksumCalculator_calculateChecksum
  (JNIEnv * jniEnv, jclass clazz, jstring filename) {

    int result;
    const char *src_filename;
    char        dst_filename[MAX_FILE_NAME_LENGTH];

    // Get the source file name
    src_filename = (*jniEnv)->GetStringUTFChars(jniEnv, filename, NULL);

    // Get the name of the file to write checksum to (and "accidentally" overflow the buffer)
    sprintf(dst_filename, "%s.digested", src_filename);

    // Write the checksum to the destination file and also get its value
    result = digest(src_filename, dst_filename);

    // Free the memory used for source file name
    (*jniEnv)->ReleaseStringUTFChars(jniEnv, filename, src_filename);

    printf("Exiting native method with checksum: %d\n", result);

    return result;
}