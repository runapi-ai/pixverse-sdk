"""PixVerse response models."""

from runapi.core import BaseModel, TaskResponse, optional, required

MODEL = "pixverse-v6"
CHARACTER_MODEL = MODEL

class MediaUrl(BaseModel):
    url = optional(str)

class VideoTaskResponse(TaskResponse):
    id = required(str)
    status = optional(str, enum=lambda: TaskResponse.Status.ALL)
    videos = optional([lambda: MediaUrl])
    source_task_id = optional(str)
    error = optional(str)

class CompletedVideoTaskResponse(VideoTaskResponse):
    videos = required([lambda: MediaUrl])

TextToVideoResponse = VideoTaskResponse
CompletedTextToVideoResponse = CompletedVideoTaskResponse
ImageToVideoResponse = VideoTaskResponse
CompletedImageToVideoResponse = CompletedVideoTaskResponse
EditVideoResponse = VideoTaskResponse
CompletedEditVideoResponse = CompletedVideoTaskResponse
TransitionVideoResponse = VideoTaskResponse
CompletedTransitionVideoResponse = CompletedVideoTaskResponse
ExtendVideoResponse = VideoTaskResponse
CompletedExtendVideoResponse = CompletedVideoTaskResponse
