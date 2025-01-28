#!/bin/bash
mkdir -p certs
openssl req -x509 -newkey rsa:4096 -keyout certs/selfsigned.key -out certs/selfsigned.crt -days 365 -nodes -subj "/CN=localhost"
