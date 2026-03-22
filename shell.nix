{ pkgs ? import <nixpkgs> {
    config.allowUnfree = true;
    config.android_sdk.accept_license = true;
  }
}:

let
  androidComposition = pkgs.androidenv.composeAndroidPackages {
    buildToolsVersions = [ "36.1.0" ];
    platformVersions = [ "36" ];
    includeEmulator = false;
    includeNDK = false;
    includeSources = false;
    includeSystemImages = false;
  };

  androidSdk = androidComposition.androidsdk;
in
pkgs.mkShell {
  packages = with pkgs; [
    jdk21

    androidSdk

    ruby
    bundler

    pkg-config

    git
  ];

  buildInputs = with pkgs; [
    libyaml
    openssl
    zlib
  ];

  ANDROID_HOME = "${androidSdk}/libexec/android-sdk";
  ANDROID_SDK_ROOT = "${androidSdk}/libexec/android-sdk";
  JAVA_HOME = "${pkgs.jdk21}";

  shellHook = ''
    # Ensure the Gradle wrapper is executable after a fresh clone
    [ -f ./gradlew ] && chmod +x ./gradlew
  '';
}
