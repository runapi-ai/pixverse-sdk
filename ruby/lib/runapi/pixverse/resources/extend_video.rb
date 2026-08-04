# frozen_string_literal: true

module RunApi
  module PixVerse
    module Resources
      # PixVerse extend-video resource.
      # Extend a completed RunAPI PixVerse video task.
      class ExtendVideo
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/pixverse/extend_video"

        RESPONSE_CLASS = Types::ExtendVideoResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedExtendVideoResponse

        def initialize(http)
          @http = http
        end

        # Create a extend-video task and wait until complete.
        #
        # @param params [Hash] extend-video parameters
        # @return [RunApi::PixVerse::Types::CompletedExtendVideoResponse] completed task with videos
        def run(options: nil, **params)
          task = create(options: options, **params)
          poll_until_complete { get(task.id, options: options) }
        end

        # Create a extend-video task.
        #
        # @param params [Hash] extend-video parameters
        # @return [RunApi::PixVerse::Types::ExtendVideoResponse] task creation result with id
        def create(options: nil, **params)
          params = compact_params(params)
          validate_params!(params)
          request(:post, ENDPOINT, body: params, options: options)
        end

        # Get extend-video task status by task ID.
        #
        # @param id [String] task ID
        # @return [RunApi::PixVerse::Types::ExtendVideoResponse] current task status
        def get(id, options: nil)
          request(:get, "#{ENDPOINT}/#{id}", options: options)
        end

        private

        def validate_params!(params)
          validate_contract!(CONTRACT["extend-video"], params)
        end
      end
    end
  end
end
