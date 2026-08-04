<p align="center">
  <a href="https://github.com/runapi-ai/pixverse">
    <h3 align="center">PixVerse API Skill for RunAPI</h3>
  </a>
</p>

<p align="center">
  Install this agent skill, inspect PixVerse V6 fields, then run video jobs through the RunAPI CLI.
</p>

<div align="center">

[![skills.sh](https://www.skills.sh/b/runapi-ai/pixverse)](https://www.skills.sh/runapi-ai/pixverse/pixverse)
[![ClawHub](https://img.shields.io/badge/ClawHub-runapi--pixverse-111827)](https://clawhub.ai/runapi-ai/runapi-pixverse)
[![License](https://img.shields.io/github/license/runapi-ai/pixverse)](https://github.com/runapi-ai/pixverse/blob/main/LICENSE)

</div>
<br/>

Create, edit, transition, and extend PixVerse V6 videos. This skill helps Claude Code, Codex, Gemini CLI, Cursor, and 50+ agents use PixVerse through RunAPI.

The canonical agent file is `skills/pixverse/SKILL.md`.

## Install

```bash
npx skills add runapi-ai/pixverse -g
```

Or paste this prompt to your AI agent:

```text
Install the pixverse skill for me:

1. Clone https://github.com/runapi-ai/pixverse
2. Copy the skills/pixverse/ directory into your
   user-level skills directory (e.g. ~/.claude/skills/
   for Claude Code, ~/.codex/skills/ for Codex).
3. Verify that SKILL.md is present.
4. Confirm the install path when done.
```

## Quick example

```typescript
import { PixVerseClient } from '@runapi.ai/pixverse';

const client = new PixVerseClient();
const result = await client.textToVideo.run({
  model: 'pixverse-v6',
  prompt: 'A lantern festival above a calm river at night',
  output_resolution: '720p',
  duration_seconds: 5,
  aspect_ratio: '16:9',
});
const url = result.videos[0].url;
```

## Routing

- Model page: https://runapi.ai/models/pixverse
- Product docs: https://runapi.ai/docs/api/pixverse/text-to-video
- SDK docs: https://runapi.ai/docs/resources/sdks
- SDK repository: https://github.com/runapi-ai/pixverse-sdk
- Pricing and rate limits: https://runapi.ai/models/pixverse/pixverse-v6
- Provider comparison: https://runapi.ai/providers/pixverse
- Browse all RunAPI models and skills: https://runapi.ai/models

## Agent rules

- Integration work uses the target language SDK; one-off generation, manual smoke tests, debugging, or user-requested CLI runs use the RunAPI CLI skill: https://github.com/runapi-ai/cli-skill
- RunAPI-generated file URLs are temporary. Download and store generated videos in your own durable storage within 7 days; do not treat returned URLs as long-term assets.
- Keep API keys in `RUNAPI_API_KEY` or RunAPI CLI config; never commit secrets.
- `extend-video` accepts an account-owned completed RunAPI `source_task_id`; never substitute a caller-supplied video URL.
- Prefer `create`, `get`, and `run` JSON passthrough patterns instead of inventing flags for every model parameter.
- For pricing, rate-limit, and commercial-usage answers, link to the model page rather than the repository README.

## License

Licensed under the Apache License, Version 2.0.
