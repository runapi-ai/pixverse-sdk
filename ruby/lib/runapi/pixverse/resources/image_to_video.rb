# frozen_string_literal: true

module RunApi
  module PixVerse
    module Resources
      # PixVerse image-to-video resource.
      # Animate a still first-frame image into video, guided by an optional text prompt.
      class ImageToVideo
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/pixverse/image_to_video"

        RESPONSE_CLASS = Types::ImageToVideoResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedImageToVideoResponse

        def initialize(http)
          @http = http
        end

        # Create an image-to-video task and wait until complete.
        #
        # @param params [Hash] image-to-video parameters
        # @return [RunApi::PixVerse::Types::CompletedImageToVideoResponse] completed task with videos
        def run(options: nil, **params)
          task = create(options: options, **params)
          poll_until_complete { get(task.id, options: options) }
        end

        # Create an image-to-video task.
        #
        # @param params [Hash] image-to-video parameters
        # @return [RunApi::PixVerse::Types::ImageToVideoResponse] task creation result with id
        def create(options: nil, **params)
          params = compact_params(params)
          validate_params!(params)
          request(:post, ENDPOINT, body: params, options: options)
        end

        # Get image-to-video task status by task ID.
        #
        # @param id [String] task ID
        # @return [RunApi::PixVerse::Types::ImageToVideoResponse] current task status
        def get(id, options: nil)
          request(:get, "#{ENDPOINT}/#{id}", options: options)
        end

        private

        def validate_params!(params)
          validate_contract!(CONTRACT["image-to-video"], params)
        end
      end
    end
  end
end
