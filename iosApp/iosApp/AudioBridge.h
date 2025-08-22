#ifndef AudioBridge_h
#define AudioBridge_h

#ifdef __cplusplus
extern "C" {
#endif

// Objective-C interface for Swift NativeAudioService
void audio_play_from_url(const char* url);
void audio_stop_playback(void);
void audio_test_system_sound(void);
void audio_cleanup_resources(void);

#ifdef __cplusplus
}
#endif

#endif /* AudioBridge_h */ 