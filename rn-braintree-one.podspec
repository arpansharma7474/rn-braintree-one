require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "rn-braintree-one"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.swift_version = '4.0'
  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/arpansharma7474/rn-braintree.git", :tag => "#{s.version}" }

  
  s.source_files = "ios/**/*.{h,m,mm,swift}"
  

  s.dependency "BraintreeDropIn"
  s.dependency "React"
end
