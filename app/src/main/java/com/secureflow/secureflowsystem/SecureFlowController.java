package com.secureflow.secureflowsystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/")
public class SecureFlowController {

    @Autowired
    JdbcTemplate jdbc;

    private final ConcurrentHashMap<String, Integer> falhasLogin = new ConcurrentHashMap<>();

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String username, @RequestParam String password, @RequestHeader(value="X-Forwarded-For", required=false) String xff, @RequestHeader(value="User-Agent", required=false) String ua) {
        String ip = xff != null ? xff : "unknown";
        int fails = falhasLogin.merge(username, 1, Integer::sum);
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("username", username);
        body.put("result", "FAIL");
        body.put("fail_count", fails);
        body.put("ip", ip);
        body.put("ua", ua);
        System.out.println("[LOGIN] user=" + username + " ip=" + ip + " ua="" + ua + "" fails=" + fails);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    // VULNERÁVEL CONTROLADO: SQLi para treino (NÃO usar em produção!)
    @GetMapping("/search")
    public Map<String, Object> search(@RequestParam String q) {
        String sql = "SELECT '" + q + "' as echo"; // intencional concatenação
        System.out.println("[SEARCH] q=" + q + " sql="" + sql + """);
        Map<String, Object> resp = new HashMap<>();
        try {
            String echoed = jdbc.queryForObject(sql, String.class);
            resp.put("echo", echoed);
        } catch (Exception e) {
            resp.put("error", e.getMessage());
        }
        resp.put("timestamp", Instant.now().toString());
        return resp;
    }

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String dir = "/tmp/uploads";
        new File(dir).mkdirs();
        String out = dir + "/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        file.transferTo(new File(out));
        System.out.println("[UPLOAD] saved=" + out + " size=" + file.getSize());
        Map<String, Object> resp = new HashMap<>();
        resp.put("saved", out);
        resp.put("size", file.getSize());
        resp.put("timestamp", Instant.now().toString());
        return resp;
    }
}
