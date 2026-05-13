import KatanaShared
import SwiftUI

@main
internal struct KatanaApp: App {
    internal var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    MainViewControllerKt.handleDeepLink(url: url.absoluteString)
                }
        }
    }
}
