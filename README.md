# ETL Dynamic - Sistema de Extração, Transformação e Carregamento de Dados

Uma aplicação robusta construída com **Spring Boot 4.0.4** e **Java 21** para processar, transformar e carregar dados em tempo real usando Apache Kafka e AWS S3.

## 🎯 Sobre o Projeto

O **ETL Dynamic** é uma plataforma de processamento de dados que combina:
- **Autenticação e Autorização** segura com JWT
- **Processamento em Lote** (Spring Batch)
- **Streaming de Dados** com Apache Kafka
- **Armazenamento** em AWS S3
- **Gerenciamento de Usuários** com controle de papéis e permissões
- **Persistência** em banco de dados PostgreSQL

## 📋 Tecnologias Utilizadas

### Core
- **Java 21** - Linguagem de programação
- **Spring Boot 4.0.4** - Framework web
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **Spring Batch** - Processamento em lote
- **Lombok** - Redução de código boilerplate

### Banco de Dados
- **PostgreSQL** - Banco de dados relacional
- **HikariCP** - Pool de conexões

### Mensageria e Streaming
- **Apache Kafka** - Processamento de eventos em tempo real
- **Spring Cloud Stream** - Integração com Kafka

### Cloud Storage
- **AWS S3** - Armazenamento de objetos (com suporte a MinIO local)

### Emails
- **Spring Mail** - Envio de emails (configurado com Mailpit localmente)

### Ferramentas
- **Maven** - Gerenciador de dependências e build
- **Docker Compose** - Orquestração de containers

## 🏗️ Arquitetura

```
src/main/java/dev/hamasakis/etl/
├── controllers/          # Endpoints REST (Auth, Users)
├── services/            # Lógica de negócio (Auth, JWT, Mail, Storage)
├── models/              # Entidades JPA (User)
├── repositories/        # Acesso a dados (UserRepository)
├── dtos/                # Objetos de transferência de dados
├── enums/               # Tipos enumerados (Role, Permission)
├── configs/             # Configurações (Security)
├── messaging/           # Integração Kafka (Producer, Consumer)
└── seeder/              # Inicialização de dados
```

## 🚀 Como Começar

### Pré-requisitos

- **Java 21** ou superior
- **Maven 3.6+**
- **Docker e Docker Compose** (para executar os serviços localmente)
- **Git**

### Instalação e Setup

1. **Clone o repositório**
   ```bash
   git clone <repository-url>
   cd etl
   ```

2. **Inicie os serviços com Docker Compose**
   ```bash
   docker-compose up -d
   ```

   Isso iniciará:
   - **PostgreSQL** (porta 5432)
   - **PgAdmin** (porta 5050) - Interface web para gerenciar o banco
   - **Kafka** (porta 9092)
   - **MinIO** (S3 local)
   - **Mailpit** (Interface de emails)

3. **Compile e execute a aplicação**
   ```bash
   ./mvnw spring-boot:run
   ```

   Ou com Maven instalado:
   ```bash
   mvn spring-boot:run
   ```

4. **Acesse a aplicação**
   - Servidor: `http://localhost:8001/api`
   - PgAdmin: `http://localhost:5050`

## 📝 Configuração

### Arquivo de Configuração Principal

O arquivo `src/main/resources/application.yaml` contém todas as configurações:

```yaml
server:
  port: 8001
  servlet:
    context-path: /api

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/etl_dynamic_db
    username: admin
    password: adminpassword
  
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: etl-processor-group
  
  cloud:
    aws:
      s3:
        bucket: etl-raw-data
        endpoint: http://localhost:9000
```

### Variáveis de Ambiente

Você pode sobrescrever as configurações padrão usando variáveis de ambiente:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://seu-host:5432/seu-banco
export SPRING_DATASOURCE_USERNAME=seu-usuario
export SPRING_DATASOURCE_PASSWORD=sua-senha
export SPRING_KAFKA_BOOTSTRAP_SERVERS=seu-kafka:9092
```

## 🔐 Autenticação

### Endpoints de Autenticação

#### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "senha123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "expiresIn": 3600
}
```

### Controle de Acesso

O sistema utiliza **JWT (JSON Web Tokens)** para autenticação e autorização:

- **Roles**: ADMIN, USER, GUEST
- **Permissions**: Definidas por papel

### Como Usar o Token

Adicione o token no header `Authorization`:
```bash
Authorization: Bearer <seu-token-jwt>
```

## 👥 Gerenciamento de Usuários

### Endpoints de Usuários

#### Listar Usuários
```bash
GET /api/users
Authorization: Bearer <token>
```

#### Atualizar Usuário
```bash
PUT /api/users/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Novo Nome",
  "email": "novo@email.com"
}
```

## 📨 Envio de Emails

A aplicação é configurada para enviar emails usando **Mailpit** (ferramenta de teste de emails).

Quando um email é enviado, ele fica disponível em:
```
http://localhost:1025 (SMTP)
```

## 📊 Apache Kafka

### Publicar Mensagens

A aplicação possui um `KafkaProducerService` para enviar mensagens:

```java
kafkaProducerService.sendMessage("topic-name", "mensagem");
```

### Consumir Mensagens

O `KafkaConsumerService` processa mensagens automaticamente:

```java
@KafkaListener(topics = "topic-name", groupId = "etl-processor-group")
public void consume(String message) {
    // Processa a mensagem
}
```

## 💾 Armazenamento em S3

Utilize o `StorageService` para operações com S3:

```java
// Upload
storageService.uploadFile(file, "seu-bucket");

// Download
storageService.downloadFile("seu-bucket", "chave");

// Deletar
storageService.deleteFile("seu-bucket", "chave");
```

## 🧪 Testes

Execute os testes com:

```bash
./mvnw test
```

Arquivo de testes principal: `src/test/java/dev/hamasakis/etl/EtlApplicationTests.java`

## 📦 Build e Deployment

### Compilar para Produção

```bash
./mvnw clean package -DskipTests
```

Isso gera um arquivo JAR em `target/etl-0.0.1-SNAPSHOT.jar`

### Build com Docker

```bash
./mvnw spring-boot:build-image
```

## 📋 Estrutura de Papéis e Permissões

### Papéis (Roles)
- **ADMIN** - Acesso total ao sistema
- **USER** - Usuário padrão com permissões limitadas
- **GUEST** - Acesso somente leitura

### Arquivo de Enumerações
- `enums/Role.java` - Definição de papéis
- `enums/Permission.java` - Definição de permissões específicas

## 🛠️ Desenvolvimento

### Estrutura do Projeto

- **DTOs** (`dtos/`) - Objetos de transferência para requisições e respostas
- **Models** (`models/`) - Entidades de banco de dados
- **Services** (`services/`) - Lógica de negócio
- **Controllers** (`controllers/`) - Endpoints REST
- **Repositories** (`repositories/`) - Acesso a dados

### Adicionar Nova Entidade

1. Crie o model em `models/`
2. Crie o repository em `repositories/`
3. Crie a service em `services/`
4. Crie o controller em `controllers/`
5. Crie os DTOs em `dtos/`

## 📚 Referências

- [Documentação Spring Boot 4.0.4](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Apache Kafka](https://kafka.apache.org/)
- [AWS S3](https://aws.amazon.com/s3/)
- [JWT.io](https://jwt.io/)

## 🐛 Troubleshooting

### Erro de Conexão com PostgreSQL
```bash
# Verifique se o container está rodando
docker ps | grep postgres

# Ou reinicie os serviços
docker-compose restart postgres
```

### Erro de Conexão com Kafka
```bash
# Verifique o status do Kafka
docker-compose logs kafka

# Ou reinicie
docker-compose restart kafka
```

### Porta Já em Uso
Se a porta 8001 já está em uso, modifique em `application.yaml`:
```yaml
server:
  port: 8002  # ou outra porta disponível
```

## 📄 Licença

Este projeto é fornecido como está, sem garantias expressas ou implícitas.

## 👨‍💻 Contribuidores

- **Desenvolvedor Principal**: dev.hamasakis

---

**Última atualização**: Março 2026

Para mais informações, consulte a documentação técnica ou entre em contato com a equipe de desenvolvimento.

