# PetJoy

## Apresentação
Aplicação Java que realize operações CRUD em um banco de dados MySQL utilizando o Docker Compose para o gerenciamento e a execução dos serviços.

## A Equipe
Ian Barros Nunes
Letícia Lopes Moreira
Maria Augusta Barreto de Gois

## Requisitos

### Contexto:           
PetJoy - Uma plataforma de gerenciamento de pets hospedados no hotel PetJoy. Realiza operações de cadastrar os pets, lista-los, editar suas atividades e excluir hospedagens finalizadas.
    
### Tabelas do Banco de Dados:
- Cliente, Pets, Materiais, Atividade, Material_Utilizado

## Apresentação (durante a aula)
Cenário de execução da aplicação com o docker-compose com as operações do CRUD    
`docker-compose build`
`docker-compose run --rm app`
`docker stop petjoy-app`

Cenário de execução isolado para o container do banco de dados                
- Indicar os comandos para a execução do container
`docker compose up -d mysql`
`docker exec -it petjoy-db mysql -u root -p`
`docker stop petjoy-db`

- Realizar ao menos duas operações do CRUD no container
`USE petjoy;`
`SELECT * FROM cliente`
`INSERT INTO materiais VALUES (10, 'Sabonete');`

## Folder Structure

The workspace contains two folders by default, where:
- `lib`: the folder to maintain dependencies

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
