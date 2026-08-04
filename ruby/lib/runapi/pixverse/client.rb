# frozen_string_literal: true

module RunApi
  module PixVerse
    # PixVerse V6 video generation, editing, transition, and extension API client.
    #
    # @example
    #   client = RunApi::PixVerse::Client.new(api_key: "your-api-key")
    #   result = client.text_to_video.run(
    #     model: "pixverse-v6", prompt: "A horse galloping across a sunset beach",
    #     output_resolution: "720p", duration_seconds: 5, aspect_ratio: "16:9"
    #   )
    class Client < RunApi::Core::Client
      # @return [Resources::TextToVideo] Text-to-video generation.
      attr_reader :text_to_video
      # @return [Resources::ImageToVideo] Image-to-video animation from a first-frame image.
      attr_reader :image_to_video
      # @return [Resources::EditVideo] Video editing with text prompts and reference images.
      attr_reader :edit_video
      attr_reader :transition_video
      attr_reader :extend_video

      def initialize(api_key: nil, **options)
        super
        @text_to_video = Resources::TextToVideo.new(http)
        @image_to_video = Resources::ImageToVideo.new(http)
        @edit_video = Resources::EditVideo.new(http)
        @transition_video = Resources::TransitionVideo.new(http)
        @extend_video = Resources::ExtendVideo.new(http)
      end
    end
  end
end
