# SupernovaVet - Cloud Application

## Integrantes

- Felipe Augusto Lopes Ferreira - RM563982
- Kaique Mascarenhas dos Santos - RM565802

---

# Descrição do Projeto

O SupernovaVet é uma API REST desenvolvida em Java com Spring Boot para gerenciamento veterinário.

A aplicação permite realizar operações CRUD completas de Pets e Tutores, possibilitando:

- Cadastro de pets
- Cadastro de tutores
- Consulta de registros
- Atualização de informações
- Remoção de dados

Toda a solução foi conteinerizada utilizando Docker e executada em uma Máquina Virtual Linux na Microsoft Azure.

---

# Benefícios para o Negócio

A solução oferece:

- Centralização do gerenciamento veterinário
- Facilidade no cadastro e consulta de pets
- Persistência de dados em banco H2
- Facilidade de deploy utilizando Docker
- Escalabilidade em nuvem utilizando Azure
- Ambiente reproduzível através de scripts automatizados

---

# Tecnologias Utilizadas

- Java 21
- Spring Boot
- Docker
- Docker Compose
- Banco H2
- Microsoft Azure
- Azure CLI

---

# Arquitetura da Solução

## Fluxo Macro

```text
Usuário
   ↓
Internet
   ↓
Azure VM Linux
   ↓
Docker Compose
   ↓
Container Spring Boot API
   ↓
Container Banco H2
```

---

# Estrutura Docker

## Containers Utilizados

### API Java

- Container: `supernova-api`
- Porta: `8080`

### Banco H2

- Container: `supernova-h2`
- Porta TCP: `1521`
- Console Web: `81`

---

# Persistência de Dados

O banco H2 utiliza volume nomeado Docker:

```yaml
volumes:
  h2-data:
```

Garantindo persistência dos dados mesmo após reinicialização dos containers.

---

# Docker Compose

```yaml
services:

  h2db:
    image: oscarfonts/h2
    container_name: supernova-h2

    ports:
      - "1521:1521"
      - "81:81"

    volumes:
      - h2-data:/opt/h2-data

    networks:
      - supernova-net

  app:
    build: .
    container_name: supernova-api

    ports:
      - "8080:8080"

    depends_on:
      - h2db

    networks:
      - supernova-net

networks:
  supernova-net:

volumes:
  h2-data:
```

---

# Dockerfile

```dockerfile
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

RUN useradd -m appuser

USER appuser

EXPOSE 8080

CMD java -jar target/Novamonitor-0.0.1-SNAPSHOT.jar
```

---

# Configuração do Banco H2

## application.properties

```properties
spring.datasource.url=jdbc:h2:tcp://h2db:1521/opt/h2-data/meubanco
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

spring.h2.console.enabled=true
spring.h2.console.settings.web-allow-others=true
```

---

# Rotas da API

## Pets

### GET - Listar Pets

```http
GET /pets
```

---

### POST - Criar Pet

```http
POST /pets
```

---

### PUT - Atualizar Pet

```http
PUT /pets/{id}
```

---

### DELETE - Remover Pet

```http
DELETE /pets/{id}
```

---

## Tutores

### GET - Listar Tutores

```http
GET /tutores
```

---

### POST - Criar Tutor

```http
POST /tutores
```

---

# How To - Instalação da Solução

## 1) Clonar o repositório

```bash
git clone https://github.com/FelipeAugusto99/SUPERNOVAVET-CLOUD.git
```

---

## 2) Entrar na pasta do projeto

```bash
cd SUPERNOVAVET-CLOUD
```

---

## 3) Realizar login na Azure

```bash
az login
```

---

## 4) Executar script de criação da infraestrutura

```bash
./azure/criacao.sh
```

O script realiza automaticamente:

- Criação da VM Linux
- Abertura das portas necessárias
- Instalação do Docker
- Instalação do Git
- Instalação do Nano
- Execução da aplicação Dockerizada

---

# Execução da Aplicação

Após o script finalizar:

## API

```bash
http://SEU-IP:8080/pets
```

## Console H2

```bash
http://SEU-IP:81
```

---

# Operações CRUD

## GET - Listar Pets

```bash
curl http://SEU-IP:8080/pets
```

---

## POST - Criar Pet

```bash
curl -X POST http://SEU-IP:8080/pets \
-H "Content-Type: application/json" \
-d '{
  "nome":"Rex",
  "especie":"Cachorro",
  "idade":5,
  "nivelRisco":"BAIXO",
  "tutor":{
    "nome":"Felipe",
    "email":"felipe@email.com"
  }
}'
```

---

## POST - Criar Segundo Pet

```bash
curl -X POST http://SEU-IP:8080/pets \
-H "Content-Type: application/json" \
-d '{
  "nome":"Mimi",
  "especie":"Gato",
  "idade":2,
  "nivelRisco":"MEDIO",
  "tutor":{
    "nome":"Kaique",
    "email":"kaique@email.com"
  }
}'
```

---

## PUT - Atualizar Pet

```bash
curl -X PUT http://SEU-IP:8080/pets/1 \
-H "Content-Type: application/json" \
-d '{
  "nome":"Rex Atualizado",
  "especie":"Cachorro",
  "idade":6,
  "nivelRisco":"ALTO"
}'
```

---

## DELETE - Remover Pet

```bash
curl -X DELETE http://SEU-IP:8080/pets/2
```

---

# Scripts Azure CLI

## Script de Criação

Arquivo:

```bash
azure/criacao.sh
```

Responsável por:

- Criar Resource Group
- Criar VM Linux
- Abrir portas
- Instalar Docker
- Instalar Git
- Clonar repositório
- Executar Docker Compose

---

## Script de Remoção

Arquivo:

```bash
azure/deletar.sh
```

Responsável por:

- Remover Resource Group
- Excluir VM
- Excluir recursos associados

---

# Execução em Background

A aplicação executa em background utilizando:

```bash
docker compose up -d
```

---

# Usuário Sem Privilégios Administrativos

A aplicação utiliza usuário não-root no container:

```dockerfile
RUN useradd -m appuser

USER appuser
```

---

# Evidências Necessárias para Entrega

## Demonstrar no vídeo

- Execução do script Azure CLI
- Criação automática da VM
- Execução do Docker Compose
- Funcionamento da API
- Persistência dos dados
- Operações CRUD completas
- Console H2 funcionando

---

# Remoção da Infraestrutura

Para remover toda a infraestrutura criada:

```bash
./azure/deletar.sh
```
