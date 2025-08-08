# SecureFlow – Mini-laboratório de RIF (Resposta a Incidentes e Forense)

> Ambiente **100% em nuvem (GitHub Codespaces)** para treinar cenários de incidentes: força-bruta, SQLi controlado e upload EICAR; com coleta de evidências via logs de app/DB/Nginx e selagem por SHA-256.

## Subir no Codespaces
1. Abra no Codespaces.
2. Aguarde o `postCreateCommand` (sobe `docker compose`).
3. Acesse a interface web via **porta 8081** do Codespace.

## Serviços
- **web**: UI simples (porta 5173) – acessada via Nginx.
- **nginx**: proxy reverso (porta 8081) – gera `access.log` e `error.log` em `evidencias/nginx_logs`.
- **app**: Spring Boot (porta 8080) – logs em `evidencias/app_logs/secureflow-app.log`.
- **db**: PostgreSQL (porta 5432) – logs em `evidencias/db_logs`.

## Cenários
- **Login**: POST `/api/auth/login` – sempre falha, incrementa contador e loga IP/UA.
- **Search (SQLi controlado)**: GET `/api/search?q=<payload>` – concatena string (para treino).
- **Upload**: POST `/api/upload` – salva arquivo em `/tmp/uploads` (no container do app).

## Evidências
Geradas automaticamente em `./evidencias/*`. Para selar:
```bash
bash scripts/evidencias/selar.sh
```

## Ataques simulados
```bash
bash scripts/ataques/forca_bruta.sh
bash scripts/ataques/sqli.sh
```

> **Atenção:** Vulnerabilidades **intencionais** e isoladas para estudo. **Não** reutilize este código em produção.
