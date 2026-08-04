# frozen_string_literal: true

module RunApi
  module PixVerse
    # Type definitions and constants for PixVerse video generation and editing.
    module Types
      MODEL = "pixverse-v6"

      # A generated video file with a download URL.
      class MediaUrl < RunApi::Core::BaseModel
        optional :url, String
      end

      class TextToVideoResponse < RunApi::Core::TaskResponse
        required :id, String
        optional :status, String, enum: -> { RunApi::Core::TaskResponse::Status::ALL }
        optional :videos, [-> { MediaUrl }]
        optional :error, String
      end

      # Narrowed response returned by +run()+ once polling observes +status: "completed"+.
      # +videos+ is required so consumers never have to null-check it on a successful task.
      class CompletedTextToVideoResponse < TextToVideoResponse
        required :videos, [-> { MediaUrl }]
      end

      ImageToVideoResponse = TextToVideoResponse
      CompletedImageToVideoResponse = CompletedTextToVideoResponse
      EditVideoResponse = TextToVideoResponse
      CompletedEditVideoResponse = CompletedTextToVideoResponse
      TransitionVideoResponse = TextToVideoResponse
      CompletedTransitionVideoResponse = CompletedTextToVideoResponse
      ExtendVideoResponse = TextToVideoResponse
      CompletedExtendVideoResponse = CompletedTextToVideoResponse
    end
  end
end
