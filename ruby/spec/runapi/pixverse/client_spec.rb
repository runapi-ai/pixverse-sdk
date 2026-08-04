# frozen_string_literal: true

require "spec_helper"

RSpec.describe RunApi::PixVerse::Client do
  before { allow(ConnectionPool).to receive(:new).and_return(instance_double(ConnectionPool)) }
  after { RunApi.api_key = nil }

  it "exposes all five video resources" do
    client = described_class.new(api_key: "test-key")
    expect(client.text_to_video).to be_a(RunApi::PixVerse::Resources::TextToVideo)
    expect(client.image_to_video).to be_a(RunApi::PixVerse::Resources::ImageToVideo)
    expect(client.edit_video).to be_a(RunApi::PixVerse::Resources::EditVideo)
    expect(client.transition_video).to be_a(RunApi::PixVerse::Resources::TransitionVideo)
    expect(client.extend_video).to be_a(RunApi::PixVerse::Resources::ExtendVideo)
  end
end
