package com.github.progirls.despesas.api.despesas_api.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyValidator {

    public static void main(String[] args) throws Exception {
        // Caminhos dos arquivos
        String privateKeyPath = "src/main/resources/keys/app.key";
        String publicKeyPath = "src/main/resources/keys/app.pub";

        // Ler chave privada
        String privateKeyContent = new String(Files.readAllBytes(Paths.get(privateKeyPath)))
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] privateKeyDecoded = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyDecoded);
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(privateKeySpec);
        System.out.println("✅ Chave privada carregada com sucesso!");

        // Le chave pública
        String publicKeyContent = new String(Files.readAllBytes(Paths.get(publicKeyPath)))
                .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] publicKeyDecoded = Base64.getDecoder().decode(publicKeyContent);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyDecoded);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
        System.out.println("✅ Chave pública carregada com sucesso!");
    }
}