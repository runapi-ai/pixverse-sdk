export const contract = {
  "edit-video": {
    "models": [
      "pixverse-v6"
    ],
    "fields_by_model": {
      "pixverse-v6": {
        "aspect_ratio": {
          "enum": [
            "16:9",
            "4:3",
            "1:1",
            "3:4",
            "9:16",
            "2:3",
            "3:2",
            "21:9"
          ],
          "required": true
        },
        "duration_seconds": {
          "required": true,
          "min": 1,
          "max": 15,
          "type": "integer"
        },
        "enable_audio": {
          "enum": [
            true,
            false
          ]
        },
        "model": {
          "required": true
        },
        "output_resolution": {
          "enum": [
            "360p",
            "540p",
            "720p",
            "1080p"
          ],
          "required": true
        },
        "prompt": {
          "required": true,
          "min": 3,
          "max": 5000,
          "length": true
        },
        "reference_image_urls": {
          "required": true,
          "min_items": 1,
          "max_items": 7
        },
        "seed": {
          "min": 0,
          "max": 2147483647,
          "type": "integer"
        }
      }
    }
  },
  "extend-video": {
    "models": [
      "pixverse-v6"
    ],
    "fields_by_model": {
      "pixverse-v6": {
        "duration_seconds": {
          "required": true,
          "min": 1,
          "max": 15,
          "type": "integer"
        },
        "enable_audio": {
          "enum": [
            true,
            false
          ]
        },
        "model": {
          "required": true
        },
        "output_resolution": {
          "enum": [
            "360p",
            "540p",
            "720p",
            "1080p"
          ],
          "required": true
        },
        "prompt": {
          "required": true,
          "min": 3,
          "max": 5000,
          "length": true
        },
        "seed": {
          "min": 0,
          "max": 2147483647,
          "type": "integer"
        },
        "source_task_id": {
          "required": true
        }
      }
    }
  },
  "image-to-video": {
    "models": [
      "pixverse-v6"
    ],
    "fields_by_model": {
      "pixverse-v6": {
        "duration_seconds": {
          "required": true,
          "min": 1,
          "max": 15,
          "type": "integer"
        },
        "enable_audio": {
          "enum": [
            true,
            false
          ]
        },
        "first_frame_image_url": {
          "required": true
        },
        "model": {
          "required": true
        },
        "output_resolution": {
          "enum": [
            "360p",
            "540p",
            "720p",
            "1080p"
          ],
          "required": true
        },
        "prompt": {
          "required": true,
          "min": 3,
          "max": 5000,
          "length": true
        },
        "seed": {
          "min": 0,
          "max": 2147483647,
          "type": "integer"
        }
      }
    }
  },
  "text-to-video": {
    "models": [
      "pixverse-v6"
    ],
    "fields_by_model": {
      "pixverse-v6": {
        "aspect_ratio": {
          "enum": [
            "16:9",
            "4:3",
            "1:1",
            "3:4",
            "9:16",
            "2:3",
            "3:2",
            "21:9"
          ],
          "required": true
        },
        "duration_seconds": {
          "required": true,
          "min": 1,
          "max": 15,
          "type": "integer"
        },
        "enable_audio": {
          "enum": [
            true,
            false
          ]
        },
        "model": {
          "required": true
        },
        "output_resolution": {
          "enum": [
            "360p",
            "540p",
            "720p",
            "1080p"
          ],
          "required": true
        },
        "prompt": {
          "required": true,
          "min": 3,
          "max": 5000,
          "length": true
        },
        "seed": {
          "min": 0,
          "max": 2147483647,
          "type": "integer"
        }
      }
    }
  },
  "transition-video": {
    "models": [
      "pixverse-v6"
    ],
    "fields_by_model": {
      "pixverse-v6": {
        "duration_seconds": {
          "required": true,
          "min": 1,
          "max": 15,
          "type": "integer"
        },
        "enable_audio": {
          "enum": [
            true,
            false
          ]
        },
        "first_frame_image_url": {
          "required": true
        },
        "last_frame_image_url": {
          "required": true
        },
        "model": {
          "required": true
        },
        "output_resolution": {
          "enum": [
            "360p",
            "540p",
            "720p",
            "1080p"
          ],
          "required": true
        },
        "prompt": {
          "required": true,
          "min": 3,
          "max": 5000,
          "length": true
        },
        "seed": {
          "min": 0,
          "max": 2147483647,
          "type": "integer"
        }
      }
    }
  }
} as const;
