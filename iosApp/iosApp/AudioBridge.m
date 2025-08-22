#import "AudioBridge.h"
#import "GuessSong-Swift.h"

void audio_play_from_url(const char* url) {
    NSString *urlString = [NSString stringWithUTF8String:url];
    [NativeAudioService.shared playAudioWithUrlString:urlString];
}

void audio_stop_playback(void) {
    [NativeAudioService.shared stopAudio];
}

void audio_test_system_sound(void) {
    [NativeAudioService.shared testSystemSound];
}

void audio_cleanup_resources(void) {
    [NativeAudioService.shared cleanup];
} 