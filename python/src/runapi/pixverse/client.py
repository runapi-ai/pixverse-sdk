"""PixVerse client."""

from __future__ import annotations

from typing import Any, Optional

from runapi.core import ProviderClient

from .resources.edit_video import EditVideo
from .resources.image_to_video import ImageToVideo
from .resources.text_to_video import TextToVideo
from .resources.transition_video import TransitionVideo
from .resources.extend_video import ExtendVideo


class PixVerseClient(ProviderClient):
    """PixVerse V6 video generation, editing, transition, and extension client.

    Example::

        client = PixVerseClient(api_key="sk-...")
        result = client.text_to_video.run(
            model="pixverse-v6", prompt="A lighthouse above a stormy sea"
        )
    """

    def __init__(self, api_key: Optional[str] = None, **options: Any) -> None:
        super().__init__(api_key, **options)
        http = self._http
        self.text_to_video = TextToVideo(http)
        self.image_to_video = ImageToVideo(http)
        self.edit_video = EditVideo(http)
        self.transition_video = TransitionVideo(http)
        self.extend_video = ExtendVideo(http)
