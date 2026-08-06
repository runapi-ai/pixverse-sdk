package pixverse

import (
	"context"
	"encoding/json"
	"testing"

	"github.com/runapi-ai/core-sdk/go/core"
)

type stubHTTPClient struct {
	method string
	path   string
	body   any
}

func (s *stubHTTPClient) Request(_ context.Context, method, path string, opts *core.HTTPRequestOptions) (json.RawMessage, error) {
	s.method = method
	s.path = path
	if opts != nil {
		s.body = opts.Body
	}
	return json.RawMessage(`{"id":"task_123","status":"processing"}`), nil
}

func commonParams() CommonVideoParams {
	return CommonVideoParams{
		Model: ModelV6, Prompt: "A lantern crosses the night sky",
		OutputResolution: OutputResolution720P, DurationSeconds: 5,
	}
}

func assertRequest(t *testing.T, stub *stubHTTPClient, method, path string) map[string]any {
	t.Helper()
	if stub.method != method || stub.path != path {
		t.Fatalf("unexpected request: %s %s", stub.method, stub.path)
	}
	if method == "GET" {
		return nil
	}
	return stub.body.(map[string]any)
}

func TestTextToVideoCreateAndGet(t *testing.T) {
	stub := &stubHTTPClient{}
	resource := NewClientWithHTTP(stub).TextToVideo
	_, err := resource.Create(context.Background(), TextToVideoParams{CommonVideoParams: commonParams(), AspectRatio: AspectRatio169})
	if err != nil {
		t.Fatal(err)
	}
	body := assertRequest(t, stub, "POST", "/api/v1/pixverse/text_to_video")
	if body["model"] != "pixverse-v6" || body["aspect_ratio"] != "16:9" {
		t.Fatalf("unexpected body: %#v", body)
	}
	_, err = resource.Get(context.Background(), "task_123")
	if err != nil {
		t.Fatal(err)
	}
	assertRequest(t, stub, "GET", "/api/v1/pixverse/text_to_video/task_123")
}

func TestImageToVideoCreateAndGet(t *testing.T) {
	stub := &stubHTTPClient{}
	resource := NewClientWithHTTP(stub).ImageToVideo
	_, err := resource.Create(context.Background(), ImageToVideoParams{CommonVideoParams: commonParams(), FirstFrameImageURL: "https://cdn.runapi.ai/public/samples/input.png"})
	if err != nil {
		t.Fatal(err)
	}
	body := assertRequest(t, stub, "POST", "/api/v1/pixverse/image_to_video")
	if body["first_frame_image_url"] != "https://cdn.runapi.ai/public/samples/input.png" {
		t.Fatalf("unexpected body: %#v", body)
	}
	_, err = resource.Get(context.Background(), "task_123")
	if err != nil {
		t.Fatal(err)
	}
	assertRequest(t, stub, "GET", "/api/v1/pixverse/image_to_video/task_123")
}

func TestVideoCreatesIncludeEnableAudio(t *testing.T) {
	for _, tc := range []struct {
		name   string
		create func(*Client, context.Context, *bool) error
	}{
		{
			name: "text-to-video",
			create: func(client *Client, ctx context.Context, enableAudio *bool) error {
				_, err := client.TextToVideo.Create(ctx, TextToVideoParams{
					CommonVideoParams: commonParamsWithAudio(enableAudio), AspectRatio: AspectRatio169,
				})
				return err
			},
		},
		{
			name: "image-to-video",
			create: func(client *Client, ctx context.Context, enableAudio *bool) error {
				_, err := client.ImageToVideo.Create(ctx, ImageToVideoParams{
					CommonVideoParams: commonParamsWithAudio(enableAudio), FirstFrameImageURL: "https://cdn.runapi.ai/public/samples/input.png",
				})
				return err
			},
		},
	} {
		t.Run(tc.name, func(t *testing.T) {
			for _, enabled := range []bool{true, false} {
				stub := &stubHTTPClient{}
				if err := tc.create(NewClientWithHTTP(stub), context.Background(), &enabled); err != nil {
					t.Fatal(err)
				}
				body := assertRequest(t, stub, "POST", stub.path)
				if body["enable_audio"] != enabled {
					t.Fatalf("enable_audio = %#v, want %t", body["enable_audio"], enabled)
				}
			}
		})
	}
}

func commonParamsWithAudio(enableAudio *bool) CommonVideoParams {
	params := commonParams()
	params.EnableAudio = enableAudio
	return params
}

func TestEditVideoCreateAndGet(t *testing.T) {
	stub := &stubHTTPClient{}
	resource := NewClientWithHTTP(stub).EditVideo
	_, err := resource.Create(context.Background(), EditVideoParams{CommonVideoParams: commonParams(), ReferenceImageURLs: []string{"https://cdn.runapi.ai/public/samples/input.png"}, AspectRatio: AspectRatio169})
	if err != nil {
		t.Fatal(err)
	}
	body := assertRequest(t, stub, "POST", "/api/v1/pixverse/edit_video")
	if len(body["reference_image_urls"].([]any)) != 1 {
		t.Fatalf("unexpected body: %#v", body)
	}
	_, err = resource.Get(context.Background(), "task_123")
	if err != nil {
		t.Fatal(err)
	}
	assertRequest(t, stub, "GET", "/api/v1/pixverse/edit_video/task_123")
}

func TestTransitionVideoCreateAndGet(t *testing.T) {
	stub := &stubHTTPClient{}
	resource := NewClientWithHTTP(stub).TransitionVideo
	_, err := resource.Create(context.Background(), TransitionVideoParams{CommonVideoParams: commonParams(), FirstFrameImageURL: "https://cdn.runapi.ai/public/samples/first.png", LastFrameImageURL: "https://cdn.runapi.ai/public/samples/last.png"})
	if err != nil {
		t.Fatal(err)
	}
	body := assertRequest(t, stub, "POST", "/api/v1/pixverse/transition_video")
	if body["last_frame_image_url"] != "https://cdn.runapi.ai/public/samples/last.png" {
		t.Fatalf("unexpected body: %#v", body)
	}
	_, err = resource.Get(context.Background(), "task_123")
	if err != nil {
		t.Fatal(err)
	}
	assertRequest(t, stub, "GET", "/api/v1/pixverse/transition_video/task_123")
}

func TestExtendVideoCreateAndGet(t *testing.T) {
	stub := &stubHTTPClient{}
	resource := NewClientWithHTTP(stub).ExtendVideo
	_, err := resource.Create(context.Background(), ExtendVideoParams{CommonVideoParams: commonParams(), SourceTaskID: "source_123"})
	if err != nil {
		t.Fatal(err)
	}
	body := assertRequest(t, stub, "POST", "/api/v1/pixverse/extend_video")
	if body["source_task_id"] != "source_123" {
		t.Fatalf("unexpected body: %#v", body)
	}
	_, err = resource.Get(context.Background(), "task_123")
	if err != nil {
		t.Fatal(err)
	}
	assertRequest(t, stub, "GET", "/api/v1/pixverse/extend_video/task_123")
}
