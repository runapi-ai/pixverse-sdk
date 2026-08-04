# frozen_string_literal: true

module RunApi
  module PixVerse
    module Resources
      # PixVerse transition-video resource.
      # Generate a transition between first and last frame images.
      class TransitionVideo
        include RunApi::Core::ResourceHelpers

        ENDPOINT = "/api/v1/pixverse/transition_video"

        RESPONSE_CLASS = Types::TransitionVideoResponse
        COMPLETED_RESPONSE_CLASS = Types::CompletedTransitionVideoResponse

        def initialize(http)
          @http = http
        end

        # Create a transition-video task and wait until complete.
        #
        # @param params [Hash] transition-video parameters
        # @return [RunApi::PixVerse::Types::CompletedTransitionVideoResponse] completed task with videos
        def run(options: nil, **params)
          task = create(options: options, **params)
          poll_until_complete { get(task.id, options: options) }
        end

        # Create a transition-video task.
        #
        # @param params [Hash] transition-video parameters
        # @return [RunApi::PixVerse::Types::TransitionVideoResponse] task creation result with id
        def create(options: nil, **params)
          params = compact_params(params)
          validate_params!(params)
          request(:post, ENDPOINT, body: params, options: options)
        end

        # Get transition-video task status by task ID.
        #
        # @param id [String] task ID
        # @return [RunApi::PixVerse::Types::TransitionVideoResponse] current task status
        def get(id, options: nil)
          request(:get, "#{ENDPOINT}/#{id}", options: options)
        end

        private

        def validate_params!(params)
          validate_contract!(CONTRACT["transition-video"], params)
        end
      end
    end
  end
end
