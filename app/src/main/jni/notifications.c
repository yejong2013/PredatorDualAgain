#include <jni.h>

// Links from InitializeActivity Activity
JNIEXPORT jstring JNICALL Java_com_it_1tech613_predator_ui_InitializeActivity_get1(JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env, "UHJlZGF0b3I=");
}
JNIEXPORT jstring JNICALL Java_com_it_1tech613_predator_ui_InitializeActivity_getTwo(JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env, "YUhSMGNEb3ZMM1JvWlhKaFpHbHZjMmhoYXk1amJ5NTFheTlwY0hSMkwybHVaR1Y0TG5Cb2NEOXNhV05sYm1ObFgydGxlUT09");
}
JNIEXPORT jstring JNICALL Java_com_it_1tech613_predator_ui_InitializeActivity_getThree(JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env, "aHR0cDovL3RoYUhSMGNEb3ZMM1JvYUhSMGNEb3ZMM1JvWlhKaFpHbHZjMmhoYXk1amJ5NTFheTlwY0hSMlgzQXlMMmx1WkdWNExuQm9jRDlzYVdObGJtTmxYMnRsZVQxelpERXk=");
}
JNIEXPORT jstring JNICALL Java_com_it_1tech613_predator_ui_InitializeActivity_getFour(JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env, "aHR0cDovL3RoYUhSMGNEb3ZMM1JvYUhSMGNEb3ZMM1JvWlhKaFpHbHZjMmhoYXk1amJ5NTFheTlwY0hSMlgzQXhMMmx1WkdWNExuQm9jRDlzYVdObGJtTmxYMnRsZVQxelpERXk=");
}
JNIEXPORT jstring JNICALL Java_com_it_1tech613_predator_ui_InitializeActivity_get2(JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env, "c2QxMg==");
}
JNIEXPORT jstring JNICALL Java_com_it_1tech613_predator_ui_InitializeActivity_get3(JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env, "c2QxMg==");
}
JNIEXPORT jstring JNICALL Java_com_it_1tech613_predator_ui_InitializeActivity_get4(JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env, "c2QxMg==");
}
JNIEXPORT jstring JNICALL Java_com_it_1tech613_predator_ui_InitializeActivity_getUrl1(JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env, "aHR0cDovL3RoZXJhZGlvc2hhay5jby51ay9sb2NramF3L2ljb24ucG5n");
}
JNIEXPORT jstring JNICALL Java_com_it_1tech613_predator_ui_InitializeActivity_getUrl2(JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env, "aHR0cDovL3RoZXJhZGlvc2hhay5jby51ay9sb2NrZHVvL2ljb24ucG5n");
}
JNIEXPORT jstring JNICALL Java_com_it_1tech613_predator_ui_InitializeActivity_getUrl3(JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env, "aHR0cDovL3RoZXJhZGlvc2hhay5jby51ay9sb2NrZHVvL2ljb24ucG5n");
}
JNIEXPORT jstring JNICALL Java_com_it_1tech613_predator_ui_InitializeActivity_getUrl4(JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env, "aHR0cDovL3RoZXJhZGlvc2hhay5jby51ay9sb2NrZHVvL2ljb24ucG5n");
}

JNIEXPORT jstring JNICALL Java_com_it_1tech613_predator_ui_liveTv_FragmentExoLiveTv_getLiveUrl(
        JNIEnv *env, jobject instance){
    return (*env)->NewStringUTF(env, "%slive/%s/%s/%d.m3u8");
}