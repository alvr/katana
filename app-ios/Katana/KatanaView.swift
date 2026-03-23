import KatanaShared
import SwiftUI

internal struct KatanaView: UIViewControllerRepresentable {
    internal func makeUIViewController(context _: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    internal func updateUIViewController(_: UIViewController, context _: Context) {
        // no-op
    }
}
