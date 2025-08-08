#!/usr/bin/env bash
# Gera SHA-256 para tudo em ./evidencias e salva manifestos por pasta
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/../.."; pwd)"
cd "$ROOT/evidencias"
for d in app_logs db_logs nginx_logs pcap relatorios; do
  [ -d "$d" ] || continue
  find "$d" -type f -print0 | xargs -0 -I{} sh -c "sha256sum '{}' >> '${d}.sha256'"
done
echo "Manifestos SHA-256 gerados em evidencias/*.sha256"
