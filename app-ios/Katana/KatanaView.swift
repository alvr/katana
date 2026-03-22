import KatanaShared
import SwiftUI

internal struct KatanaView: UIViewControllerRepresentable {
    internal func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    internal func updateUIViewController(_: UIViewController, context: Context) {
        // no-op
    }
}
