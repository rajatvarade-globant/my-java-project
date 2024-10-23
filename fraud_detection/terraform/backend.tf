terraform {
  backend "gcs" {
    bucket = "biiling_test"
    prefix = "terraform/state"
  }
}