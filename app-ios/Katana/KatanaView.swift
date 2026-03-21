import KatanaShared
import SwiftUI

internal struct KatanaView: UIViewControllerRepresentable {
    internal func makeUIViewController(_: Context) -> some UIViewController {
        MainViewControllerKt.MainViewController()
    }

    internal func updateUIViewController(_: UIViewControllerType, _: Context) {
        // no-op
    }
}
