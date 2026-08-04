import type { AsyncTaskStatus, TaskBillingResponse, TaskResponse } from '@runapi.ai/core';

export type PixVerseModel = 'pixverse-v6';
export type PixVerseOutputResolution = '360p' | '540p' | '720p' | '1080p';
export type PixVerseAspectRatio = '16:9' | '4:3' | '1:1' | '3:4' | '9:16' | '2:3' | '3:2' | '21:9';

export interface CommonVideoParams {
  model: PixVerseModel;
  prompt: string;
  output_resolution: PixVerseOutputResolution;
  duration_seconds: number;
  enable_audio?: boolean;
  seed?: number;
  callback_url?: string;
}

export interface TextToVideoParams extends CommonVideoParams {
  aspect_ratio: PixVerseAspectRatio;
  enable_multi_clip?: boolean;
}

export interface ImageToVideoParams extends CommonVideoParams {
  first_frame_image_url: string;
  enable_multi_clip?: boolean;
}

export interface EditVideoParams extends CommonVideoParams {
  reference_image_urls: string[];
  aspect_ratio: PixVerseAspectRatio;
}

export interface TransitionVideoParams extends CommonVideoParams {
  first_frame_image_url: string;
  last_frame_image_url: string;
}

export interface ExtendVideoParams extends CommonVideoParams {
  source_task_id: string;
}

export interface TaskCreateResponse extends TaskBillingResponse {
  id: string;
  status?: AsyncTaskStatus;
}

export interface Video { url: string }

export interface VideoTaskResponse extends TaskResponse {
  id: string;
  status: AsyncTaskStatus;
  videos?: Video[];
  error?: string;
  [key: string]: unknown;
}

export type CompletedVideoTaskResponse = VideoTaskResponse & { status: 'completed'; videos: Video[] };
export type TextToVideoResponse = VideoTaskResponse;
export type ImageToVideoResponse = VideoTaskResponse;
export type EditVideoResponse = VideoTaskResponse;
export type TransitionVideoResponse = VideoTaskResponse;
export type ExtendVideoResponse = VideoTaskResponse & { source_task_id?: string };
export type CompletedTextToVideoResponse = CompletedVideoTaskResponse;
export type CompletedImageToVideoResponse = CompletedVideoTaskResponse;
export type CompletedEditVideoResponse = CompletedVideoTaskResponse;
export type CompletedTransitionVideoResponse = CompletedVideoTaskResponse;
export type CompletedExtendVideoResponse = CompletedVideoTaskResponse & { source_task_id?: string };
