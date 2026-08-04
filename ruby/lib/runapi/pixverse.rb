# frozen_string_literal: true

require "runapi/core"
require_relative "pixverse/types"
require_relative "pixverse/contract_gen"
require_relative "pixverse/resources/edit_video"
require_relative "pixverse/resources/image_to_video"
require_relative "pixverse/resources/text_to_video"
require_relative "pixverse/resources/transition_video"
require_relative "pixverse/resources/extend_video"
require_relative "pixverse/client"

module RunApi
  module PixVerse
    AuthenticationError = RunApi::Core::AuthenticationError
    RateLimitError = RunApi::Core::RateLimitError
    InsufficientCreditsError = RunApi::Core::InsufficientCreditsError
    NotFoundError = RunApi::Core::NotFoundError
    ValidationError = RunApi::Core::ValidationError
    TaskFailedError = RunApi::Core::TaskFailedError
    TaskTimeoutError = RunApi::Core::TaskTimeoutError
  end
end
