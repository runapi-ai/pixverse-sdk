# frozen_string_literal: true

Dir.chdir(__dir__) do

  Gem::Specification.new do |spec|
    spec.name = "runapi-pixverse"
    spec.version = "0.1.1"
    spec.metadata["runapi_slug"] = "pixverse"
    spec.authors = ["RunAPI"]
    spec.email = ["contact@runapi.ai"]

    spec.summary = "PixVerse Ruby SDK for RunAPI"
    spec.description = "Install `runapi-pixverse` and use `RunApi::PixVerse::Client` for all five PixVerse V6 video resources."
    spec.homepage = "https://runapi.ai/models/pixverse"
    spec.license = "Apache-2.0"
    spec.required_ruby_version = ">= 3.1.0"
    spec.metadata["homepage_uri"] = "https://runapi.ai/models/pixverse"
    spec.metadata["documentation_uri"] = "https://github.com/runapi-ai/pixverse-sdk/blob/main/ruby/README.md"
    spec.metadata["source_code_uri"] = "https://github.com/runapi-ai/pixverse-sdk"
    spec.metadata["bug_tracker_uri"] = "https://github.com/runapi-ai/pixverse-sdk/issues"
    spec.metadata["changelog_uri"] = "https://github.com/runapi-ai/pixverse-sdk/blob/main/CHANGELOG.md"


    spec.files = Dir.glob("lib/**/*") + %w[LICENSE README.md]
    spec.extra_rdoc_files = ["README.md"]
        spec.require_paths = ["lib"]

    spec.add_dependency "runapi-core", "~> 0.3.1"
  end
end
