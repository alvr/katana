source "https://rubygems.org"

gem "faraday", ">= 1.10.5", "< 2.14.3"
gem "fastlane", "~> 2.236"

plugins_path = File.join(File.dirname(__FILE__), 'fastlane', 'Pluginfile')
eval_gemfile(plugins_path) if File.exist?(plugins_path)
