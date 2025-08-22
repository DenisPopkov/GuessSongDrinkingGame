import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView()
                .ignoresSafeArea(.keyboard)
                .ignoresSafeArea(.container, edges: .top) // Игнорируем верхнюю безопасную зону
                .ignoresSafeArea(.container, edges: .bottom) // Игнорируем нижнюю безопасную зону
                .background(Color.black) // Черный фон для системных баров
    }
}
