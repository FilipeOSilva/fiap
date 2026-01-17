# Links uteis
https://start.spring.io/
# Falar sobre JPA(postgres)
# DB
---

### Usuários

* Dois tipos obrigatórios:

  * Dono de restaurante
  * Cliente
* Possibilidade de adicionar novos tipos futuramente

---

### Usuário (entidade principal)

* `id` (chave primária)
* `nome` (String)
* `email` (String, **único**)
* `login` (String)
* `senha` (String)
* `data_ultima_alteracao` (Date / DateTime)
* `tipo_usuario` (ex: CLIENTE, DONO_RESTAURANTE)
* `endereco` (relacionamento)

---

### Endereço

* Entidade separada
* Campos:

  * `rua`
  * `numero`
  * `cidade`
  * `cep`
  * `complemento` (opcional)
* Relacionamento:

  * Usuário 1 — 1 Endereço
    *(ou 1 — N, se quiser enriquecer o modelo)*

---

### Cliente

* Especialização de Usuário
* `id` (FK para Usuário)
* Sem campos adicionais obrigatórios (por enquanto)

---

### Dono de Restaurante

* Especialização de Usuário
* `id` (FK para Usuário)
* Relacionamento:

  * Dono de Restaurante 1 — N Restaurantes

---

### Modelo Relacional (resumo)

* `usuario`
* `endereco`
* `cliente`
* `dono_restaurante`

---

Se quiser, posso simplificar ainda mais ou adaptar exatamente ao **padrão que seu professor costuma cobrar**.

