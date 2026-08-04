import type { HttpClient, PollingOptions, RequestOptions, ActionSchema } from '@runapi.ai/core';
import { compactParams, validateParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import { contract } from '../contract_gen';
import type {
  CompletedTextToVideoResponse,
  TaskCreateResponse,
  TextToVideoParams,
  TextToVideoResponse,
} from '../types';

const ENDPOINT = '/api/v1/pixverse/text_to_video';

/** Generate video from a text prompt. */
export class TextToVideo {
  constructor(private readonly http: HttpClient) {}

  /**
   * Create a text-to-video task and wait until complete.
   * @param params Text-to-video parameters.
   * @param options Per-request and polling overrides.
   * @returns The completed task with videos.
   */
  async run(params: TextToVideoParams, options?: RequestOptions & PollingOptions): Promise<CompletedTextToVideoResponse> {
    const { id } = await this.create(params, options);
    const response = await pollUntilComplete<TextToVideoResponse>(() => this.get(id, options), {
      maxWaitMs: options?.maxWaitMs,
      pollIntervalMs: options?.pollIntervalMs,
    });
    return response as CompletedTextToVideoResponse;
  }

  /**
   * Create a text-to-video task; returns immediately with a task id.
   * @param params Text-to-video parameters.
   * @param options Per-request overrides.
   * @returns The task creation result with id.
   */
  async create(params: TextToVideoParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    validateParams(contract['text-to-video'] as ActionSchema, body as Record<string, unknown>);
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, {
      body,
      ...options,
    });
  }

  /**
   * Fetch the current status of a text-to-video task.
   * @param id The task id.
   * @param options Per-request overrides.
   * @returns The current text-to-video task status.
   */
  async get(id: string, options?: RequestOptions): Promise<TextToVideoResponse> {
    return this.http.request<TextToVideoResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
