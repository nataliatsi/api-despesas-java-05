# Controle de Despesas – API Backend

---

A **API Controle de Despesas** é responsável pelo gerenciamento de transações financeiras dos usuários, permitindo o controle eficiente de receitas e despesas.

---

## Arquitetura do Sistema

O projeto segue uma arquitetura baseada em camadas:

- **Controller**: Responsável por receber requisições HTTP e retornar respostas.
- **Service**: Contém a lógica de negócio da aplicação.
- **Repository**: Interage diretamente com o banco de dados usando o Spring Data JPA.

A API é desenvolvida em **Spring Boot** e utiliza **PostgreSQL** como banco de dados. Para simplificar a execução, os serviços são disponibilizados via **Docker Compose**.

- Veja os [Diagramas do Projeto](./docs/diagramas.md)

- Créditos aos autores:
  - Casos de Uso — [Pamela Brito](https://github.com/pamelabrito)
  - Diagrama de Classes — [Natália Gomes](https://github.com/nataliatsi)
  - Modelo de Dados — [Milena Mensor](https://github.com/MilenaMensor)

---

## Estrutura de Banco de Dados

---

## Enpoints da API

### 📌 Endpoint de Cadastro de Usuário (POST /api/v1/users)
Foi criado o endpoint responsável por cadastrar novos usuários na aplicação. A funcionalidade inclui:

- Validação dos dados de entrada (nome, email, senha);

- Verificação de duplicidade de email;

- Geração de hash seguro para a senha (BCrypt);

- Persistência do usuário no banco de dados;

- Retorno com mensagens claras em casos de erro (ex.: email já cadastrado).

Esse endpoint é público e será utilizado no fluxo inicial de criação de conta pelos usuários.



---

## Autenticação

---

## Validação dos Dados

---

## Executando o Projeto

Siga o passo a passo completo para rodar o projeto localmente no arquivo abaixo:

👉 [Como rodar o projeto](./docs/executar-projeto.md)

#### Pré-requisitos

Antes de começar, certifique-se de ter os seguintes requisitos instalados:

- **Java 17**
- **Docker** (para subir o banco de dados PostgreSQL)
- **OpenSSL** (para geração das chaves RSA)

---

## Testes

---

## Documentação da API

Após iniciar a aplicação, você pode acessar a documentação no **Swagger**:

🔗 **[Swagger UI](http://localhost:8080/swagger-ui/index.html)**

Aqui, você pode visualizar e testar os endpoints disponíveis.
