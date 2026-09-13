# SuperNova VET - DevOps & Cloud Computing

Projeto desenvolvido para a disciplina de **DevOps Tools & Cloud Computing**.

A solução utiliza uma arquitetura totalmente containerizada na Microsoft Azure, seguindo a opção **ACR + ACI**.

A aplicação foi desenvolvida em **Java com Spring Boot** e utiliza **PostgreSQL** como banco de dados.

---

## Integrantes

- Felipe Augusto Lopes Ferreira - RM563982
- Kaique Mascarenhas dos Santos - 565802

---

## Sobre o projeto

O **SuperNova VET** é uma API para gerenciamento de tutores e pets de uma clínica veterinária.

A aplicação permite realizar operações de cadastro, consulta, atualização e exclusão de dados relacionados a:

- Tutores
- Pets

Cada pet possui um tutor responsável, formando um relacionamento entre as duas principais entidades do sistema.

A API utiliza Spring Boot, Spring Data JPA, Flyway e PostgreSQL.

---

## Benefícios para o negócio

A solução permite centralizar as informações dos tutores e seus respectivos pets em um único sistema.

Entre os principais benefícios estão:

- Centralização dos dados dos clientes e animais.
- Facilidade na consulta das informações cadastradas.
- Atualização rápida dos dados dos pets e tutores.
- Associação entre cada pet e seu tutor responsável.
- Classificação do nível de risco dos animais.
- Redução de cadastros manuais e informações descentralizadas.
- Disponibilidade da aplicação através da infraestrutura em nuvem.
- Facilidade de implantação utilizando containers.

---

# Arquitetura da solução

A infraestrutura utiliza os seguintes recursos:

- **Azure Container Registry (ACR)** para armazenar a imagem Docker da API.
- **Azure Container Instances (ACI)** para executar a API Spring Boot.
- **Azure Container Instances (ACI)** para executar o PostgreSQL.
- **Docker** para criação da imagem da aplicação.
- **Azure CLI** para criação de todos os recursos da infraestrutura.

Fluxo principal:

```text
Usuário / Swagger
       |
       v
Azure Container Instance
     API Java
       |
       v
Azure Container Instance
   PostgreSQL

Azure Container Registry
       |
       v
Imagem Docker da API
       |
       v
Azure Container Instance
```

Todos os recursos utilizados na solução são criados através da Azure CLI.

> O diagrama oficial da arquitetura será adicionado à documentação do projeto.

---

# Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Flyway
- PostgreSQL 16
- Docker
- Azure CLI
- Azure Container Registry
- Azure Container Instances
- Swagger / OpenAPI
- Maven

---

# Banco de dados

A aplicação utiliza **PostgreSQL 16**.

O banco também é executado dentro de um container no Azure Container Instances.

As tabelas principais da aplicação são:

### `ch_tutor`

Armazena os tutores responsáveis pelos pets.

Principais campos:

- `id_tutor`
- `nm_tutor`
- `ds_email`
- `nr_telefone`
- `ds_senha`
- `ds_perfil`

### `ch_pet`

Armazena os pets cadastrados.

Principais campos:

- `id_pet`
- `nm_pet`
- `nr_idade`
- `ds_especie`
- `ds_nivel_risco`
- `id_tutor`

O campo `id_tutor` é uma chave estrangeira que relaciona o pet ao seu tutor.

O script DDL completo pode ser encontrado no arquivo:

```text
script_bd.sql
```

As migrations utilizadas pela aplicação estão em:

```text
src/main/resources/db/migration
```

---

# Estrutura principal do projeto

```text
SUPERNOVAVET-DEVOPS
│
├── azure
│   ├── criacao.sh
│   └── deletar.sh
│
├── src
│   └── main
│       └── resources
│           └── db
│               └── migration
│
├── Dockerfile
├── script_bd.sql
├── pom.xml
└── README.md
```

---

# Pré-requisitos

Antes de executar o projeto, é necessário possuir:

- Git
- Docker Desktop
- Azure CLI
- Conta Microsoft Azure
- Acesso a uma assinatura Azure
- Git Bash, caso esteja executando os scripts `.sh` no Windows

Também é necessário estar com o Docker Desktop em execução.

---

# Clonando o projeto

O primeiro passo é clonar este repositório:

```bash
git clone https://github.com/FelipeAugusto99/SUPERNOVAVET-DEVOPS.git
```

Entre na pasta:

```bash
cd SUPERNOVAVET-DEVOPS
```

---

# Login na Azure

Realize o login utilizando a Azure CLI:

```bash
az login
```

Caso existam várias assinaturas disponíveis, é possível verificar com:

```bash
az account list --output table
```

E selecionar uma assinatura utilizando:

```bash
az account set --subscription "<NOME-OU-ID-DA-SUBSCRIPTION>"
```

---

# Criação da infraestrutura

Toda a infraestrutura da aplicação pode ser criada através do script:

```text
azure/criacao.sh
```

No Git Bash:

```bash
bash azure/criacao.sh
```

Durante a execução, será solicitada uma senha para o PostgreSQL.

A senha é informada durante a execução e não fica armazenada no código-fonte.

O script realiza automaticamente:

1. Criação do Resource Group.
2. Registro do provider do Azure Container Instances.
3. Criação do Azure Container Registry.
4. Login no ACR.
5. Build da imagem Docker da API.
6. Criação da tag da imagem.
7. Push da imagem para o ACR.
8. Criação do container PostgreSQL no ACI.
9. Configuração das credenciais do ACR.
10. Criação do container da API no ACI.
11. Configuração da comunicação entre API e banco de dados.
12. Exibição do endereço público da aplicação.

---

# Recursos criados

A infraestrutura utiliza os seguintes recursos:

```text
Resource Group:
rg-supernovavet-devops

Azure Container Registry:
acrsupernovavet563982

Container da API:
aci-supernovavet-api

Container do PostgreSQL:
aci-supernovavet-db
```

Região utilizada:

```text
Brazil South
```

---

# Build da aplicação

O Dockerfile utiliza duas etapas.

Na primeira etapa é realizado o build da aplicação utilizando Java 17.

Na segunda etapa é criada a imagem final somente com o Java Runtime Environment.

Build manual:

```bash
docker build -t supernovavet-api .
```

---

# Envio da imagem para o ACR

Login:

```bash
az acr login --name acrsupernovavet563982
```

Criação da tag:

```bash
docker tag supernovavet-api \
acrsupernovavet563982.azurecr.io/supernovavet-api:v1
```

Push:

```bash
docker push \
acrsupernovavet563982.azurecr.io/supernovavet-api:v1
```

Para consultar os repositórios existentes no ACR:

```bash
az acr repository list \
  --name acrsupernovavet563982 \
  --output table
```

---

# Execução sem usuário root

Por questões de segurança, o container da aplicação não é executado com o usuário `root`.

O Dockerfile cria um usuário específico:

```dockerfile
RUN useradd -m appuser
```

E posteriormente define:

```dockerfile
USER appuser
```

Dessa forma, o processo Java é executado com permissões reduzidas dentro do container.

---

# Variáveis de ambiente

A API recebe as configurações do banco através de variáveis de ambiente.

Principais variáveis:

```text
DB_HOST
DB_PORT
DB_NAME
DB_USERNAME
DB_PASSWORD
```

Exemplo da configuração utilizada pela aplicação:

```properties
spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:supernova}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}
```

Nenhuma senha do ambiente Azure é armazenada diretamente no código-fonte.

---

# Acessando o Swagger

Após a criação da infraestrutura, o Swagger pode ser acessado através de:

```text
http://supernovavet-api-563982.brazilsouth.azurecontainer.io:8080/swagger-ui/index.html
```

O endereço é disponibilizado pelo Azure Container Instance da API.

---

# CRUD de Tutores

A aplicação possui CRUD completo para a entidade Tutor.

### Consultar tutores

```http
GET /tutores
```

### Criar tutor

```http
POST /tutores
```

Exemplo:

```json
{
  "nome": "Tutor Teste",
  "email": "tutor.teste@supernovavet.com",
  "telefone": "11977776666",
  "senha": "senha123",
  "perfil": "TUTOR"
}
```

### Atualizar tutor

```http
PUT /tutores/{id}
```

Exemplo:

```json
{
  "nome": "Tutor Atualizado",
  "email": "tutor.atualizado@supernovavet.com",
  "telefone": "11966665555",
  "senha": "senha123",
  "perfil": "TUTOR"
}
```

### Excluir tutor

```http
DELETE /tutores/{id}
```

---

# CRUD de Pets

A aplicação também possui CRUD completo para a entidade Pet.

### Consultar pets

```http
GET /pets
```

### Criar pet

```http
POST /pets
```

Primeiro exemplo:

```json
{
  "nome": "Luna",
  "idade": 6,
  "especie": "CACHORRO",
  "nivelRisco": "BAIXO",
  "tutor": {
    "id": 1,
    "nome": "Administrador",
    "email": "admin@supernovavet.com",
    "telefone": "11999999999"
  }
}
```

Segundo exemplo:

```json
{
  "nome": "Thor",
  "idade": 4,
  "especie": "CACHORRO",
  "nivelRisco": "MEDIO",
  "tutor": {
    "id": 2,
    "nome": "Veterinario",
    "email": "vet@supernovavet.com",
    "telefone": "11888888888"
  }
}
```

### Atualizar pet

```http
PUT /pets/{id}
```

Exemplo:

```json
{
  "nome": "Luna Atualizada",
  "idade": 7,
  "especie": "CACHORRO",
  "nivelRisco": "ALTO",
  "tutor": {
    "id": 1
  }
}
```

### Excluir pet

```http
DELETE /pets/{id}
```

---

# Verificação direta no PostgreSQL

Para comprovar que os dados enviados pela API realmente foram persistidos no PostgreSQL executado na Azure, é possível acessar diretamente o container do banco.

Execute:

```bash
az container exec \
  --resource-group rg-supernovavet-devops \
  --name aci-supernovavet-db \
  --exec-command "psql -U postgres -d supernova"
```

Depois será exibido:

```text
supernova=#
```

---

## Consultar tutores diretamente no banco

```sql
SELECT
    id_tutor,
    nm_tutor,
    ds_email,
    nr_telefone,
    ds_perfil
FROM ch_tutor
ORDER BY id_tutor;
```

---

## Consultar pets diretamente no banco

```sql
SELECT
    id_pet,
    nm_pet,
    nr_idade,
    ds_especie,
    ds_nivel_risco,
    id_tutor
FROM ch_pet
ORDER BY id_pet;
```

---

## Consultar relacionamento entre Pet e Tutor

```sql
SELECT
    p.id_pet,
    p.nm_pet AS pet,
    p.ds_nivel_risco,
    t.id_tutor,
    t.nm_tutor AS tutor
FROM ch_pet p
INNER JOIN ch_tutor t
    ON p.id_tutor = t.id_tutor
ORDER BY p.id_pet;
```

Para sair do PostgreSQL:

```text
\q
```

---

# Verificando logs da API

Os logs podem ser consultados através da Azure CLI:

```bash
az container logs \
  --resource-group rg-supernovavet-devops \
  --name aci-supernovavet-api
```

Nos logs é possível verificar:

- Inicialização do Spring Boot.
- Execução da aplicação utilizando `appuser`.
- Conexão com PostgreSQL.
- Execução das migrations Flyway.
- Inicialização do Tomcat na porta 8080.

---

# Verificando os containers

API:

```bash
az container show \
  --resource-group rg-supernovavet-devops \
  --name aci-supernovavet-api \
  --query "{estado:instanceView.state,ip:ipAddress.ip,fqdn:ipAddress.fqdn}" \
  --output table
```

Banco:

```bash
az container show \
  --resource-group rg-supernovavet-devops \
  --name aci-supernovavet-db \
  --query "{estado:instanceView.state,ip:ipAddress.ip,fqdn:ipAddress.fqdn}" \
  --output table
```

O estado esperado é:

```text
Running
```

---

# Exclusão da infraestrutura

Para evitar consumo desnecessário de créditos da Azure, toda a infraestrutura pode ser removida através do script:

```bash
bash azure/deletar.sh
```

O script remove o Resource Group:

```text
rg-supernovavet-devops
```

Como todos os recursos estão dentro desse grupo, também são removidos:

- Azure Container Registry
- Container da API
- Container PostgreSQL

Também é possível verificar se o Resource Group ainda existe:

```bash
az group exists --name rg-supernovavet-devops
```

Após a remoção completa, o retorno será:

```text
false
```

---

# Segurança

A solução adota algumas práticas de segurança:

- A aplicação não é executada como root dentro do container.
- Senhas não ficam armazenadas no código-fonte.
- A senha do PostgreSQL é fornecida durante a criação da infraestrutura.
- A senha do banco é enviada ao ACI utilizando uma variável segura.
- A imagem da aplicação é armazenada no Azure Container Registry.
- Configurações de conexão são realizadas através de variáveis de ambiente.

---

# Persistência e integração

O fluxo de persistência utilizado pela solução é:

```text
Swagger / Cliente
       |
       v
API Spring Boot - ACI
       |
       v
Spring Data JPA
       |
       v
PostgreSQL - ACI
```

As operações realizadas pela API podem ser comprovadas diretamente através de comandos `SELECT` executados no PostgreSQL.

Isso permite demonstrar a comunicação entre a aplicação e o banco executados na nuvem.

---

# Limpeza dos recursos

Os recursos utilizados neste projeto são destinados ao ambiente acadêmico.

Após os testes ou demonstrações, recomenda-se executar:

```bash
bash azure/deletar.sh
```

para evitar consumo desnecessário de créditos da assinatura Azure.
