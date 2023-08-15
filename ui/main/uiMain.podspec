Pod::Spec.new do |spec|
    spec.name                     = 'uiMain'
    spec.version                  = '0.0.1'
    spec.homepage                 = 'https://github.com/alvr/katana'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Cocoapod uiMain module'
    spec.vendored_frameworks      = 'build/cocoapods/framework/uiMain.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '14.1'
    spec.dependency 'Sentry', '~> 8.9.5'
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':ui:main',
        'PRODUCT_MODULE_NAME' => 'uiMain',
    }
                
    spec.script_phases = [
        {
            :name => 'Build uiMain',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
    spec.resources = ['build\compose\ios\uiMain\compose-resources']
end