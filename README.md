<p align="center">
  <a href="https://runapi.ai"><img src="https://runapi.ai/icon.svg" height="56" alt="RunAPI"></a>
</p>

<h3 align="center">
  <a href="https://github.com/runapi-ai/pixverse-sdk">PixVerse API SDK for RunAPI</a>
</h3>

<p align="center">
  PixVerse API SDKs for JavaScript, Python, Ruby, Go, Java, and PHP on RunAPI.
</p>

<div align="center">

[![npm](https://img.shields.io/npm/v/@runapi.ai/pixverse)](https://www.npmjs.com/package/@runapi.ai/pixverse)
[![PyPI](https://img.shields.io/pypi/v/runapi-pixverse)](https://pypi.org/project/runapi-pixverse/)
[![RubyGems](https://img.shields.io/gem/v/runapi-pixverse)](https://rubygems.org/gems/runapi-pixverse)
[![Go Reference](https://pkg.go.dev/badge/github.com/runapi-ai/pixverse-sdk/go.svg)](https://pkg.go.dev/github.com/runapi-ai/pixverse-sdk/go)
[![Maven Central](https://img.shields.io/maven-central/v/ai.runapi/runapi-pixverse)](https://central.sonatype.com/artifact/ai.runapi/runapi-pixverse)
[![License](https://img.shields.io/github/license/runapi-ai/pixverse-sdk)](https://github.com/runapi-ai/pixverse-sdk/blob/main/LICENSE)

</div>
<br/>

Use PixVerse V6 through RunAPI from JavaScript, Python, Ruby, Go, Java, or PHP. The SDK exposes five asynchronous resources: `textToVideo`, `imageToVideo`, `editVideo`, `transitionVideo`, and `extendVideo`.

Model details and pricing: https://runapi.ai/models/pixverse/pixverse-v6

## Install

```bash
npm install @runapi.ai/pixverse
pip install runapi-pixverse
gem install runapi-pixverse
go get github.com/runapi-ai/pixverse-sdk/go@latest
```

Java artifact: `ai.runapi:runapi-pixverse`. PHP package: `runapi-ai/pixverse`.

## JavaScript

```ts
import { PixVerseClient } from '@runapi.ai/pixverse';

const client = new PixVerseClient({ apiKey: process.env.RUNAPI_API_KEY });
const result = await client.textToVideo.run({
  model: 'pixverse-v6',
  prompt: 'A lantern festival above a calm river at night',
  output_resolution: '720p',
  duration_seconds: 5,
  aspect_ratio: '16:9',
});

console.log(result.videos[0].url);
```

Use `create()` to submit without waiting, `get(id)` to query status, and `run()` to poll until completion. `extendVideo` accepts an account-owned completed RunAPI `source_task_id`; it does not accept a caller-supplied video URL.

API reference: https://runapi.ai/docs/api/pixverse/text-to-video
