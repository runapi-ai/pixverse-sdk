import type { HttpClient, PollingOptions, RequestOptions, ActionSchema } from '@runapi.ai/core';
import { compactParams, validateParams } from '@runapi.ai/core';
import { pollUntilComplete } from '@runapi.ai/core/internal';
import { contract } from '../contract_gen';
import type { CompletedExtendVideoResponse, ExtendVideoParams, ExtendVideoResponse, TaskCreateResponse } from '../types';

const ENDPOINT = '/api/v1/pixverse/extend_video';

export class ExtendVideo {
  constructor(private readonly http: HttpClient) {}
  async run(params: ExtendVideoParams, options?: RequestOptions & PollingOptions): Promise<CompletedExtendVideoResponse> {
    const { id } = await this.create(params, options);
    return pollUntilComplete<ExtendVideoResponse>(() => this.get(id, options), options) as Promise<CompletedExtendVideoResponse>;
  }
  async create(params: ExtendVideoParams, options?: RequestOptions): Promise<TaskCreateResponse> {
    const body = compactParams(params);
    validateParams(contract['extend-video'] as ActionSchema, body as Record<string, unknown>);
    return this.http.request<TaskCreateResponse>('POST', ENDPOINT, { body, ...options });
  }
  async get(id: string, options?: RequestOptions): Promise<ExtendVideoResponse> {
    return this.http.request<ExtendVideoResponse>('GET', `${ENDPOINT}/${id}`, options ?? {});
  }
}
