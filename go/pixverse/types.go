package pixverse

import "github.com/runapi-ai/core-sdk/go/core"

type Model string
type OutputResolution string
type AspectRatio string
type TaskStatus string

const (
	ModelV6               Model            = "pixverse-v6"
	OutputResolution360P  OutputResolution = "360p"
	OutputResolution540P  OutputResolution = "540p"
	OutputResolution720P  OutputResolution = "720p"
	OutputResolution1080P OutputResolution = "1080p"
	AspectRatio169        AspectRatio      = "16:9"
	AspectRatio43         AspectRatio      = "4:3"
	AspectRatio11         AspectRatio      = "1:1"
	AspectRatio34         AspectRatio      = "3:4"
	AspectRatio916        AspectRatio      = "9:16"
	AspectRatio23         AspectRatio      = "2:3"
	AspectRatio32         AspectRatio      = "3:2"
	AspectRatio219        AspectRatio      = "21:9"
)

type AsyncTaskResponse struct {
	core.TaskBillingFacts
	ID     string     `json:"id"`
	Status TaskStatus `json:"status"`
	Error  string     `json:"error,omitempty"`
}

func (r AsyncTaskResponse) GetID() string     { return r.ID }
func (r AsyncTaskResponse) GetStatus() string { return string(r.Status) }
func (r AsyncTaskResponse) GetError() string  { return r.Error }

type Video struct {
	URL string `json:"url"`
}
type VideoTaskResponse struct {
	AsyncTaskResponse
	Videos       []Video `json:"videos,omitempty"`
	SourceTaskID string  `json:"source_task_id,omitempty"`
}
type TextToVideoResponse = VideoTaskResponse

type CommonVideoParams struct {
	Model            Model            `json:"model" help:"required; model slug"`
	Prompt           string           `json:"prompt" help:"required; video description"`
	OutputResolution OutputResolution `json:"output_resolution" help:"required; output resolution"`
	DurationSeconds  int              `json:"duration_seconds" help:"required; duration in seconds from 1 to 15"`
	EnableAudio      *bool            `json:"enable_audio,omitempty" help:"optional; enable synchronized audio generation"`
	Seed             *int             `json:"seed,omitempty" help:"optional; integer from 0 to 2147483647"`
	CallbackURL      string           `json:"callback_url,omitempty" help:"optional; webhook URL"`
}
type TextToVideoParams struct {
	CommonVideoParams
	AspectRatio     AspectRatio `json:"aspect_ratio" help:"required; output aspect ratio"`
	EnableMultiClip *bool       `json:"enable_multi_clip,omitempty" help:"optional; enable multi-clip generation"`
}
type ImageToVideoParams struct {
	CommonVideoParams
	FirstFrameImageURL string `json:"first_frame_image_url" help:"required; public first-frame image URL"`
	EnableMultiClip    *bool  `json:"enable_multi_clip,omitempty" help:"optional; enable multi-clip generation"`
}
type EditVideoParams struct {
	CommonVideoParams
	ReferenceImageURLs []string    `json:"reference_image_urls" help:"required; 1 to 7 public reference image URLs"`
	AspectRatio        AspectRatio `json:"aspect_ratio" help:"required; output aspect ratio"`
}
type TransitionVideoParams struct {
	CommonVideoParams
	FirstFrameImageURL string `json:"first_frame_image_url" help:"required; public first-frame image URL"`
	LastFrameImageURL  string `json:"last_frame_image_url" help:"required; public last-frame image URL"`
}
type ExtendVideoParams struct {
	CommonVideoParams
	SourceTaskID string `json:"source_task_id" help:"required; completed RunAPI PixVerse video task ID"`
}
