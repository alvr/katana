import KatanaShared
import SwiftUI

struct KatanaView : UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> some UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        // no-op
    }
}

struct ContentView: View {
    var body: some View {
        KatanaView().ignoresSafeArea(.all)
    }
}

#Preview {
    ContentView()
}
