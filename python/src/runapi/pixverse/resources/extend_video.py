"""PixVerse extend-video resource."""

from typing import Any, Optional
from runapi.core import Resource, RequestOptions
from ..types import CompletedExtendVideoResponse, ExtendVideoResponse

class ExtendVideo(Resource):
    ENDPOINT = "/api/v1/pixverse/extend_video"
    RESPONSE_CLASS = ExtendVideoResponse
    COMPLETED_RESPONSE_CLASS = CompletedExtendVideoResponse
    def run(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        task = self.create(options=options, **params)
        return self._poll_until_complete(lambda: self.get(task.id, options=options))
    def create(self, options: Optional[RequestOptions] = None, **params: Any) -> Any:
        body = self._compact_params(params)
        return self._request("post", self.ENDPOINT, body=body, options=options)
    def get(self, id: str, options: Optional[RequestOptions] = None) -> Any:
        return self._request("get", f"{self.ENDPOINT}/{id}", options=options)
