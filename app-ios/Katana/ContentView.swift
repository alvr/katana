import KatanaShared
import SwiftUI

internal struct ContentView: View {
    internal var body: some View {
        KatanaView().ignoresSafeArea(.all)
    }
}

#Preview {
    ContentView()
}
