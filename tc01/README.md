# TC01

## Montando ambiente

### Variaveis de ambiente
Para poder rodar esse projeto, deve-se criar um arquivo com nome de `.env`. Nele estaram todas as variaveis referente ao desenvolvimendo do projeto. As variaveis necessárias são:
```
DATABASE_PORT
DATABASE_USER
DATABASE_PASSW
DATABASE_NAME
PG_PORT
PG_EMAIL
PG_PASSW
SPRING_KEY_JWT_SECRET
```
Como esse trabalho tem cunho educacional, irei colocar dentro do repositório, um arquivo já configurado para facilitar a utilização.

### Usando Dockerfile e Dockercompose
Foram adicionados dois arquivos para podermos rodar a aplicação de forma "conterizada", que possibilita desacoplar do ambiente do usuário, garantindo que seja possível executar a aplicação. Para isso é necessário ter a ferramenta docker instalada na maquina. Para executar, esteja dentro na pasta do projeto e execute o comando:
```
docker-compose up -d
```


