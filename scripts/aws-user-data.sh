#!/usr/bin/env bash
set -euo pipefail

dnf update -y
dnf install -y docker git
systemctl enable --now docker

mkdir -p /opt/reservas-api
cd /opt/reservas-api

if [ ! -d .git ]; then
  git clone https://github.com/almartinez-svalero/reservas-ad-aa2.git .
fi

git fetch --all
git checkout develop
git pull origin develop

cp .env.example .env

docker compose -f docker-compose.aws.yml up -d --build
