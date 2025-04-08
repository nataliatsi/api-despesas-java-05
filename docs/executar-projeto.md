# 🚀 Como rodar o projeto

### 1️⃣ Clonar o repositório

```bash
git clone https://github.com/Programmer-Girls/api-despesas-java-05.git
```

### 2️⃣ Entrar na pasta do projeto

```bash
cd despesas-api
```

### 3️⃣ Gerar as chaves RSA

Para garantir a segurança das autenticações, gere as chaves RSA da seguinte maneira:

#### 🔑 Gerar a Chave Privada

```bash
openssl genrsa -out src/main/resources/app.key
```

#### 🔓 Derivar a Chave Pública

```bash
openssl rsa -in src/main/resources/app.key -pubout -out src/main/resources/app.pub
```

**Importante**: As chaves devem ser chamadas `app.key` (privada) e `app.pub` (pública), e devem ser armazenadas no diretório `src/main/resources`.

### 4️⃣ Subir o banco de dados com Docker

```bash
docker-compose up --build -d
```

Isso iniciará um container com PostgreSQL.

### 5️⃣ Executar a aplicação

```bash
./mvnw spring-boot:run
```

Ou, se estiver usando Maven instalado:

```bash
mvn spring-boot:run
```
