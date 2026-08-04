# frozen_string_literal: true

module RunApi
  module PixVerse
    module Resources
      # PixVerse text-to-video resource.
      # Generate video from a text prompt.
      class TextToVideo
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/pixverse/text_to_video"

        RESPONSE_CLASS = Types::TextToVideoResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedTextToVideoResponse

        def initialize(http)
          @http = http
        end

        # Create a text-to-video task and wait until complete.
        #
        # @param params [Hash] text-to-video parameters
        # @return [RunApi::PixVerse::Types::CompletedTextToVideoResponse] completed task with videos
        def run(options: nil, **params)
          task = create(options: options, **params)
          poll_until_complete { get(task.id, options: options) }
        end

        # Create a text-to-video task.
        #
        # @param params [Hash] text-to-video parameters
        # @return [RunApi::PixVerse::Types::TextToVideoResponse] task creation result with id
        def create(options: nil, **params)
          params = compact_params(params)
          validate_params!(params)
          request(:post, ENDPOINT, body: params, options: options)
        end

        # Get text-to-video task status by task ID.
        #
        # @param id [String] task ID
        # @return [RunApi::PixVerse::Types::TextToVideoResponse] current task status
        def get(id, options: nil)
          request(:get, "#{ENDPOINT}/#{id}", options: options)
        end

        private

        def validate_params!(params)
          validate_contract!(CONTRACT["text-to-video"], params)
        end
      end
    end
  end
end
