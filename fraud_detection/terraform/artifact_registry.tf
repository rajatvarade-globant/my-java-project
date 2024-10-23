resource "google_artifact_registry_repository" "fraud_detection" {
  location      = var.region
  repository_id = "fraud-detection"
  description   = "fraud_detection"
  format        = "DOCKER"

  docker_config {
    immutable_tags = true
  }
}