#include "jni_exports.h"

#ifndef MAX_FILE_NAME_LENGTH
#define MAX_FILE_NAME_LENGTH 256
#endif

int digest(char *src_filename, char *dst_filename) {
  int checksum = rand(); // TODO: calculate the actual checksum

  FILE* digested = fopen(dst_filename, "w");

  if(digested != NULL) {
    fprintf(digested, "%d\n", checksum);
    fclose(digested);
    return checksum;
  } else {
    return -1;
  }
}

JNIEXPORT jlong JNICALL Java_ru_gvsmirnov_perv_labs_rekt_ChecksumCalculator_calculateChecksum
  (JNIEnv * jniEnv, jclass clazz, jstring filename) {

    int result;
    const char *src_filename;
    char        dst_filename[MAX_FILE_NAME_LENGTH];

    src_filename = (*jniEnv)->GetStringUTFChars(jniEnv, filename, NULL);

    sprintf(dst_filename, "%s.digested", src_filename);

    result = digest(src_filename, dst_filename);

    (*jniEnv)->ReleaseStringUTFChars(jniEnv, filename, src_filename);

    return result;
}