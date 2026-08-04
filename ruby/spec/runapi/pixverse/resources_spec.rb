# frozen_string_literal: true

require "spec_helper"

RSpec.describe "PixVerse resources" do
  let(:http) { instance_double(RunApi::Core::HttpClient) }
  let(:common) do
    {
      model: "pixverse-v6",
      prompt: "A lantern crosses the night sky",
      output_resolution: "720p",
      duration_seconds: 5
    }
  end

  cases = {
    text_to_video: [RunApi::PixVerse::Resources::TextToVideo, "text_to_video", {aspect_ratio: "16:9"}],
    image_to_video: [RunApi::PixVerse::Resources::ImageToVideo, "image_to_video", {first_frame_image_url: "https://cdn.runapi.ai/public/samples/first.png"}],
    edit_video: [RunApi::PixVerse::Resources::EditVideo, "edit_video", {reference_image_urls: ["https://cdn.runapi.ai/public/samples/reference.png"], aspect_ratio: "16:9"}],
    transition_video: [RunApi::PixVerse::Resources::TransitionVideo, "transition_video", {first_frame_image_url: "https://cdn.runapi.ai/public/samples/first.png", last_frame_image_url: "https://cdn.runapi.ai/public/samples/last.png"}],
    extend_video: [RunApi::PixVerse::Resources::ExtendVideo, "extend_video", {source_task_id: "source_123"}]
  }

  cases.each do |name, (klass, endpoint, extra)|
    it "creates and gets #{name}" do
      resource = klass.new(http)
      params = common.merge(extra)
      expect(http).to receive(:request).with(:post, "/api/v1/pixverse/#{endpoint}", body: params).and_return("id" => "task_123")
      expect(resource.create(**params).id).to eq("task_123")

      expect(http).to receive(:request).with(:get, "/api/v1/pixverse/#{endpoint}/task_123").and_return("id" => "task_123", "status" => "processing")
      expect(resource.get("task_123").id).to eq("task_123")
    end
  end

  it "validates required fields from the generated contract" do
    resource = RunApi::PixVerse::Resources::TextToVideo.new(http)
    expect { resource.create(**common) }.to raise_error(RunApi::Core::ValidationError, /aspect_ratio is required/)
  end
end
