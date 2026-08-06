import { beforeEach, describe, expect, it, vi } from 'vitest';
import type { HttpClient } from '@runapi.ai/core';
import { EditVideo } from '../../src/resources/edit-video';
import { ExtendVideo } from '../../src/resources/extend-video';
import { ImageToVideo } from '../../src/resources/image-to-video';
import { TextToVideo } from '../../src/resources/text-to-video';
import { TransitionVideo } from '../../src/resources/transition-video';

describe('PixVerse resources', () => {
  const mockHttp: HttpClient = { request: vi.fn() };
  const common = {
    model: 'pixverse-v6' as const,
    prompt: 'A lantern crosses the night sky',
    output_resolution: '720p' as const,
    duration_seconds: 5,
  };

  beforeEach(() => vi.clearAllMocks());

  const cases = [
    [TextToVideo, 'text_to_video', { aspect_ratio: '16:9' }],
    [ImageToVideo, 'image_to_video', { first_frame_image_url: 'https://cdn.runapi.ai/public/samples/first.png' }],
    [EditVideo, 'edit_video', { reference_image_urls: ['https://cdn.runapi.ai/public/samples/reference.png'], aspect_ratio: '16:9' }],
    [TransitionVideo, 'transition_video', { first_frame_image_url: 'https://cdn.runapi.ai/public/samples/first.png', last_frame_image_url: 'https://cdn.runapi.ai/public/samples/last.png' }],
    [ExtendVideo, 'extend_video', { source_task_id: 'source_123' }],
  ] as const;

  it.each(cases)('creates and gets %s tasks', async (Resource, endpoint, extra) => {
    vi.mocked(mockHttp.request)
      .mockResolvedValueOnce({ id: 'task_123', status: 'processing' })
      .mockResolvedValueOnce({ id: 'task_123', status: 'completed', videos: [{ url: 'https://cdn.runapi.ai/output.mp4' }] });
    const resource = new Resource(mockHttp);
    const params = { ...common, ...extra } as never;

    await resource.create(params);
    await resource.get('task_123');

    expect(mockHttp.request).toHaveBeenNthCalledWith(1, 'POST', `/api/v1/pixverse/${endpoint}`, { body: params });
    expect(mockHttp.request).toHaveBeenNthCalledWith(2, 'GET', `/api/v1/pixverse/${endpoint}/task_123`, {});
  });

  it.each([
    [TextToVideo, { aspect_ratio: '16:9' }],
    [ImageToVideo, { first_frame_image_url: 'https://cdn.runapi.ai/public/samples/first.png' }],
  ] as const)('validates enable_audio for %s', async (Resource, extra) => {
    vi.mocked(mockHttp.request).mockResolvedValue({ id: 'task_123', status: 'processing' });
    const resource = new Resource(mockHttp);

    await resource.create({ ...common, ...extra, enable_audio: true } as never);
    await resource.create({ ...common, ...extra, enable_audio: false } as never);

    expect(mockHttp.request).toHaveBeenNthCalledWith(1, 'POST', expect.any(String), {
      body: { ...common, ...extra, enable_audio: true },
    });
    expect(mockHttp.request).toHaveBeenNthCalledWith(2, 'POST', expect.any(String), {
      body: { ...common, ...extra, enable_audio: false },
    });
    await expect(resource.create({ ...common, ...extra, enable_audio: 'true' } as never)).rejects.toThrow(
      'enable_audio must be one of: true, false',
    );
  });
});
