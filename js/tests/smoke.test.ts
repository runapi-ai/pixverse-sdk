import { describe, expect, it } from 'vitest';
import { PixVerseClient } from '../src';

describe('PixVerseClient', () => {
  it('exposes all five video resources', () => {
    const client = new PixVerseClient({ apiKey: 'test-key' });
    expect(client.textToVideo).toBeDefined();
    expect(client.imageToVideo).toBeDefined();
    expect(client.editVideo).toBeDefined();
    expect(client.transitionVideo).toBeDefined();
    expect(client.extendVideo).toBeDefined();
  });
});
