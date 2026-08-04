import pytest

from runapi.core import config
from runapi.core.errors import AuthenticationError, ValidationError
from runapi.pixverse import PixVerseClient
from runapi.pixverse.resources import EditVideo, ExtendVideo, ImageToVideo, TextToVideo, TransitionVideo


class FakeHttp:
    def __init__(self, *responses):
        self._responses = list(responses)
        self.calls = []

    def request(self, method, path, body=None, options=None):
        self.calls.append((method, path, body))
        if self._responses:
            return self._responses.pop(0)
        return {"id": "task_1", "status": "processing"}


@pytest.fixture(autouse=True)
def reset_config(monkeypatch):
    monkeypatch.delenv("RUNAPI_API_KEY", raising=False)
    monkeypatch.setattr(config, "api_key", None)
    yield


def common_params():
    return {
        "model": "pixverse-v6",
        "prompt": "A lantern crosses the night sky",
        "output_resolution": "720p",
        "duration_seconds": 5,
    }


def test_auth_and_resource_accessors():
    with pytest.raises(AuthenticationError, match="API key is required"):
        PixVerseClient()
    client = PixVerseClient(api_key="k", http_client=FakeHttp())
    assert isinstance(client.text_to_video, TextToVideo)
    assert isinstance(client.image_to_video, ImageToVideo)
    assert isinstance(client.edit_video, EditVideo)
    assert isinstance(client.transition_video, TransitionVideo)
    assert isinstance(client.extend_video, ExtendVideo)


@pytest.mark.parametrize(
    ("resource_name", "endpoint", "extra", "expected_key"),
    [
        ("text_to_video", "text_to_video", {"aspect_ratio": "16:9"}, "aspect_ratio"),
        ("image_to_video", "image_to_video", {"first_frame_image_url": "https://cdn.runapi.ai/public/samples/first.png"}, "first_frame_image_url"),
        ("edit_video", "edit_video", {"reference_image_urls": ["https://cdn.runapi.ai/public/samples/reference.png"], "aspect_ratio": "16:9"}, "reference_image_urls"),
        ("transition_video", "transition_video", {"first_frame_image_url": "https://cdn.runapi.ai/public/samples/first.png", "last_frame_image_url": "https://cdn.runapi.ai/public/samples/last.png"}, "last_frame_image_url"),
        ("extend_video", "extend_video", {"source_task_id": "source_123"}, "source_task_id"),
    ],
)
def test_create_and_get_request_shape(resource_name, endpoint, extra, expected_key):
    fake = FakeHttp({"id": "task_123", "status": "processing"}, {"id": "task_123", "status": "completed", "videos": [{"url": "https://cdn.runapi.ai/output.mp4"}]})
    resource = getattr(PixVerseClient(api_key="k", http_client=fake), resource_name)
    params = {**common_params(), **extra}

    resource.create(**params)
    resource.get("task_123")

    assert fake.calls[0] == ("post", f"/api/v1/pixverse/{endpoint}", params)
    assert expected_key in fake.calls[0][2]
    assert fake.calls[1] == ("get", f"/api/v1/pixverse/{endpoint}/task_123", None)


def test_contract_validation_rejects_missing_required_field():
    client = PixVerseClient(api_key="k", http_client=FakeHttp())
    with pytest.raises(ValidationError, match="aspect_ratio is required"):
        client.text_to_video.create(**common_params())
