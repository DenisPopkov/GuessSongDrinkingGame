import Foundation
import AVFoundation

@objc class NativeAudioService: NSObject {
    @objc static let shared = NativeAudioService()
    
    private var player: AVPlayer?
    private var currentItem: AVPlayerItem?
    private var audioSession: AVAudioSession
    
    override init() {
        audioSession = AVAudioSession.sharedInstance()
        super.init()
        setupAudioSession()
    }
    
    private func setupAudioSession() {
        do {
            try audioSession.setCategory(.playback, mode: .default, options: [.mixWithOthers, .allowBluetooth])
            try audioSession.setActive(true)
            print("iOS: Audio session configured successfully")
        } catch {
            print("iOS: Error setting up audio session: \(error)")
        }
    }
    
    @objc func playAudio(urlString: String) {
        guard let url = URL(string: urlString) else {
            print("iOS: Invalid URL: \(urlString)")
            return
        }
        
        print("iOS: NativeAudioService.playAudio called with URL: \(urlString)")
        
        // Stop any current playback
        stopAudio()
        
        // Create AVURLAsset
        let asset = AVURLAsset(url: url)
        
        // Create AVPlayerItem
        let playerItem = AVPlayerItem(asset: asset)
        currentItem = playerItem
        
        // Create AVPlayer
        player = AVPlayer(playerItem: playerItem)
        
        // Add observer for player item status
        playerItem.addObserver(self, forKeyPath: "status", options: [.new], context: nil)
        
        // Start playing
        player?.play()
        print("iOS: Audio playback started")
    }
    
    @objc func stopAudio() {
        // Remove observer
        currentItem?.removeObserver(self, forKeyPath: "status")
        
        // Stop player
        player?.pause()
        player = nil
        currentItem = nil
        
        print("iOS: Audio stopped and resources cleared")
    }
    
    @objc func testSystemSound() {
        print("iOS: NativeAudioService.testSystemSound called")
        AudioServicesPlaySystemSound(1007) // System sound ID for notification
    }
    
    @objc func cleanup() {
        print("iOS: NativeAudioService.cleanup called")
        stopAudio()
        do {
            try audioSession.setActive(false, options: .notifyOthersOnDeactivation)
        } catch {
            print("iOS: Error deactivating audio session: \(error)")
        }
    }
    
    // MARK: - KVO Observer
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        if keyPath == "status" {
            let status = currentItem?.status
            switch status {
            case .readyToPlay:
                print("iOS: Player item is ready to play")
            case .failed:
                print("iOS: Player item failed to load")
                if let error = currentItem?.error {
                    print("iOS: Error: \(error)")
                }
            case .unknown:
                print("iOS: Player item status unknown")
            case .none:
                print("iOS: Player item status is nil")
            @unknown default:
                print("iOS: Player item status: \(String(describing: status))")
            }
        }
    }
    
    deinit {
        stopAudio()
    }
}

// MARK: - C Interface Functions
@_cdecl("nativePlayAudio")
public func nativePlayAudio(_ urlString: UnsafePointer<CChar>) {
    let url = String(cString: urlString)
    print("iOS: C function nativePlayAudio called with URL: \(url)")
    NativeAudioService.shared.playAudio(urlString: url)
}

@_cdecl("nativeStopAudio")
public func nativeStopAudio() {
    print("iOS: C function nativeStopAudio called")
    NativeAudioService.shared.stopAudio()
}

@_cdecl("nativeTestSystemSound")
public func nativeTestSystemSound() {
    print("iOS: C function nativeTestSystemSound called")
    NativeAudioService.shared.testSystemSound()
}

@_cdecl("nativeCleanupAudio")
public func nativeCleanupAudio() {
    print("iOS: C function nativeCleanupAudio called")
    NativeAudioService.shared.cleanup()
} 