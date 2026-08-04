import type { HttpClient, PollingOptions, RequestOptions, ActionSchema } from '@runapi.ai/core';
import { compactParams, validateParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import { contract } from '../contract_gen';
import type { CompletedTransitionVideoResponse, TaskCreateResponse, TransitionVideoParams, TransitionVideoResponse } from '../types';

const ENDPOINT = '/api/v1/pixverse/transition_video';

export class TransitionVideo {
  constructor(private readonly http: HttpClient) {}
  async run(params: TransitionVideoParams, options?: RequestOptions & PollingOptions): Promise<CompletedTransitionVideoResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<TransitionVideoResponse>(() => this.get(id, options), options) as Promise<CompletedTransitionVideoResponse>;
  }
  async create(params: TransitionVideoParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    validateParams(contract['transition-video'] as ActionSchema, body as Record<string, unknown>);
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, { body, ...options });
  }
  async get(id: string, options?: RequestOptions): Promise<TransitionVideoResponse> {
    return this.http.request<TransitionVideoResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
