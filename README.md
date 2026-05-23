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
```

---

# Estrutura Docker

## Containers Utilizados

### API Java

- Container: `supernova-api`
- Porta: `8080`

### Banco H2

- Banco H2 em memória
- Console H2 habilitado na aplicação

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
      - ./data:/opt/h2-data

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
spring.datasource.url=jdbc:h2:mem:testdb

spring.datasource.username=sa
spring.datasource.password=

spring.datasource.driver-class-name=org.h2.Driver

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

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

### POST - Criar Pet

```http
POST /pets
```

### PUT - Atualizar Pet

```http
PUT /pets/{id}
```

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

## SSH na VM

```bash
ssh azureuser@IP_DA_VM
```

---

## Clonar repositório dentro da VM

```bash
git clone https://github.com/FelipeAugusto99/SUPERNOVAVET-CLOUD.git
```

```bash
cd SUPERNOVAVET-CLOUD
```

---

## Rodar Docker Compose

```bash
sudo docker compose up -d --build
```

---

## Verificar containers rodando

```bash
sudo docker ps
```

---

## API

```text
http://SEU-IP:8080/pets
```

---

## Console H2

```text
http://SEU-IP:8080/h2-console
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

# Evidência de Remoção da Infraestrutura

Para remover toda a infraestrutura criada:

Abra outro terminal/bash fora da VM, volte para a pasta do projeto e execute:

```bash
./azure/deletar.sh
```

Após executar, utilize o comando abaixo para comprovar que o Resource Group foi removido:

```bash
az group list -o table
```

