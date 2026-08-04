import { BaseClient, type ClientOptions } from '@runapi.ai/core';
import { EditVideo } from './resources/edit-video';
import { ImageToVideo } from './resources/image-to-video';
import { TextToVideo } from './resources/text-to-video';
import { TransitionVideo } from './resources/transition-video';
import { ExtendVideo } from './resources/extend-video';

/**
 * PixVerse V6 video generation, editing, transition, and extension client.
 *
 * @example
 * ```typescript
 * const client = new PixVerseClient({ apiKey: 'your-api-key' });
 *
 * const result = await client.textToVideo.run({
 *   model: 'pixverse-v6',
 *   prompt: 'A horse galloping across a sunset beach',
 *   output_resolution: '720p',
 *   duration_seconds: 5,
 *   aspect_ratio: '16:9',
 * });
 * ```
 */
export class PixVerseClient extends BaseClient {
  public readonly textToVideo: TextToVideo;
  public readonly imageToVideo: ImageToVideo;
  public readonly editVideo: EditVideo;
  public readonly transitionVideo: TransitionVideo;
  public readonly extendVideo: ExtendVideo;

  constructor(options: ClientOptions = {}) {
    super(options);
    this.textToVideo = new TextToVideo(this.http);
    this.imageToVideo = new ImageToVideo(this.http);
    this.editVideo = new EditVideo(this.http);
    this.transitionVideo = new TransitionVideo(this.http);
    this.extendVideo = new ExtendVideo(this.http);
  }
}
