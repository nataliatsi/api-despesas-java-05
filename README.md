# Controle de Despesas – API Backend

---

A **API Controle de Despesas** é responsável pelo gerenciamento de transações financeiras dos usuários, permitindo o controle eficiente de receitas e despesas.

O projeto segue uma arquitetura baseada em camadas:

- **Controller**: Responsável por receber requisições HTTP e retornar respostas.
- **Service**: Contém a lógica de negócio da aplicação.
- **Repository**: Interage diretamente com o banco de dados usando o Spring Data JPA.

A API é desenvolvida em **Spring Boot** e utiliza **PostgreSQL** como banco de dados. Para simplificar a execução, os serviços são disponibilizados via **Docker Compose**.

---

## 📌 Pré-requisitos

Antes de começar, certifique-se de ter os seguintes requisitos instalados:

- **Java 17**
- **Docker** (para subir o banco de dados PostgreSQL)
- **OpenSSL** (para geração das chaves RSA)

## 🚀 Como executar o projeto

### 1️⃣ Clonar o repositório

```bash
git clone https://github.com/nataliatsi/api-despesas-java-05.git
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

## 📖 Documentação da API

Após iniciar a aplicação, você pode acessar a documentação no **Swagger**:

🔗 **[Swagger UI](http://localhost:8080/swagger-ui/index.html)**

Aqui, você pode visualizar e testar os endpoints disponíveis.


