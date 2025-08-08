const http = require('http');
const fs = require('fs');
const path = require('path');

const html = `<!doctype html>
<html>
<head><meta charset="utf-8"><title>SecureFlow Lab</title></head>
<body>
  <h1>SecureFlow Mini-Lab</h1>
  <section>
    <h2>Login (força-bruta)</h2>
    <form id="loginForm">
      <input name="username" placeholder="user" />
      <input name="password" type="password" placeholder="pass" />
      <button>Enviar</button>
    </form>
    <pre id="loginOut"></pre>
  </section>
  <section>
    <h2>Search (SQLi controlado)</h2>
    <input id="q" placeholder="q" />
    <button id="btnSearch">Buscar</button>
    <pre id="searchOut"></pre>
  </section>
  <section>
    <h2>Upload (EICAR)</h2>
    <input id="file" type="file"/>
    <button id="btnUpload">Enviar</button>
    <pre id="uploadOut"></pre>
  </section>
  <script>
    const api = (path) => '/api' + path;

    document.getElementById('loginForm').addEventListener('submit', async (e)=>{
      e.preventDefault();
      const f = e.target;
      const p = new URLSearchParams();
      p.append('username', f.username.value);
      p.append('password', f.password.value);
      const r = await fetch(api('/auth/login'), { method:'POST', headers:{'Content-Type':'application/x-www-form-urlencoded'}, body:p });
      document.getElementById('loginOut').textContent = await r.text();
    });

    document.getElementById('btnSearch').addEventListener('click', async ()=>{
      const q = document.getElementById('q').value;
      const r = await fetch(api('/search?q=') + encodeURIComponent(q));
      document.getElementById('searchOut').textContent = await r.text();
    });

    document.getElementById('btnUpload').addEventListener('click', async ()=>{
      const f = document.getElementById('file').files[0];
      const fd = new FormData();
      fd.append('file', f);
      const r = await fetch(api('/upload'), { method:'POST', body: fd });
      document.getElementById('uploadOut').textContent = await r.text();
    });
  </script>
</body>
</html>`;

const server = http.createServer((req,res)=>{
  res.setHeader('Content-Type','text/html; charset=utf-8');
  res.end(html);
});

server.listen(5173, ()=> console.log('Web up on 5173'));
