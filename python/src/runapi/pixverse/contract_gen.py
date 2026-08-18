CONTRACT = {
    "edit-video": {
        "models": ["pixverse-v6"],
        "fields_by_model": {
            "pixverse-v6": {
                "aspect_ratio": {
                    "enum": ["16:9", "4:3", "1:1", "3:4", "9:16", "2:3", "3:2", "21:9"],
                    "required": True
                },
                "duration_seconds": {
                    "required": True,
                    "min": 1,
                    "max": 15,
                    "type": "integer"
                },
                "enable_audio": {
                    "enum": [True, False]
                },
                "model": {
                    "required": True
                },
                "output_resolution": {
                    "enum": ["360p", "540p", "720p", "1080p"],
                    "required": True
                },
                "prompt": {
                    "required": True,
                    "min": 3,
                    "max": 5000,
                    "length": True
                },
                "reference_image_urls": {
                    "required": True,
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
        "models": ["pixverse-v6"],
        "fields_by_model": {
            "pixverse-v6": {
                "duration_seconds": {
                    "required": True,
                    "min": 1,
                    "max": 15,
                    "type": "integer"
                },
                "enable_audio": {
                    "enum": [True, False]
                },
                "model": {
                    "required": True
                },
                "output_resolution": {
                    "enum": ["360p", "540p", "720p", "1080p"],
                    "required": True
                },
                "prompt": {
                    "required": True,
                    "min": 3,
                    "max": 5000,
                    "length": True
                },
                "seed": {
                    "min": 0,
                    "max": 2147483647,
                    "type": "integer"
                },
                "source_task_id": {
                    "required": True
                }
            }
        }
    },
    "image-to-video": {
        "models": ["pixverse-v6"],
        "fields_by_model": {
            "pixverse-v6": {
                "duration_seconds": {
                    "required": True,
                    "min": 1,
                    "max": 15,
                    "type": "integer"
                },
                "enable_audio": {
                    "enum": [True, False]
                },
                "first_frame_image_url": {
                    "required": True
                },
                "model": {
                    "required": True
                },
                "output_resolution": {
                    "enum": ["360p", "540p", "720p", "1080p"],
                    "required": True
                },
                "prompt": {
                    "required": True,
                    "min": 3,
                    "max": 5000,
                    "length": True
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
        "models": ["pixverse-v6"],
        "fields_by_model": {
            "pixverse-v6": {
                "aspect_ratio": {
                    "enum": ["16:9", "4:3", "1:1", "3:4", "9:16", "2:3", "3:2", "21:9"],
                    "required": True
                },
                "duration_seconds": {
                    "required": True,
                    "min": 1,
                    "max": 15,
                    "type": "integer"
                },
                "enable_audio": {
                    "enum": [True, False]
                },
                "model": {
                    "required": True
                },
                "output_resolution": {
                    "enum": ["360p", "540p", "720p", "1080p"],
                    "required": True
                },
                "prompt": {
                    "required": True,
                    "min": 3,
                    "max": 5000,
                    "length": True
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
        "models": ["pixverse-v6"],
        "fields_by_model": {
            "pixverse-v6": {
                "duration_seconds": {
                    "required": True,
                    "min": 1,
                    "max": 15,
                    "type": "integer"
                },
                "enable_audio": {
                    "enum": [True, False]
                },
                "first_frame_image_url": {
                    "required": True
                },
                "last_frame_image_url": {
                    "required": True
                },
                "model": {
                    "required": True
                },
                "output_resolution": {
                    "enum": ["360p", "540p", "720p", "1080p"],
                    "required": True
                },
                "prompt": {
                    "required": True,
                    "min": 3,
                    "max": 5000,
                    "length": True
                },
                "seed": {
                    "min": 0,
                    "max": 2147483647,
                    "type": "integer"
                }
            }
        }
    }
}
