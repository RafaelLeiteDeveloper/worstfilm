# 🎬 Teste Técnico: Leitura da Lista de Indicados e Vencedores da Categoria "Pior Filme" do Golden Raspberry Awards 🍿

Este projeto tem como objetivo realizar a leitura da lista de indicados e vencedores da categoria "Pior Filme" do **Golden Raspberry Awards**

**Requisitos para executar o projeto:**

- **Java versão 8** (por exemplo: 8.0.402-amzn)
- **IDE de sua preferência**
- **Maven** com versão compatível ao Java (por exemplo: 3.9.4)


# Arquitetura

Este projeto utiliza a **arquitetura hexagonal**, que tem como objetivo separar claramente as preocupações de negócios, aplicação e infraestrutura. No modelo hexagonal ao qual utiliza-se, as classes são organizadas em diferentes camadas, cada uma com um propósito :

1. **Camada de Domínio (Domain):**
 - Contém as **entidades de negócio**, como a classe `Films`, que representa os filmes do sistema.

2. **Camada de Portas (Port):**
 - **Portas de Aplicação (Application Ports):** Representadas como as interfaces `MovieService` e `MovieProcessor`, definem os casos de uso do sistema.
 - **Portas de UI (User Interface Ports):** Representadas pela interface `MovieUiPort`, são responsáveis pela implementação dos metodos controladores.
 - **Portas de Infraestrutura (Infrastructure Ports):** Representadas pela interface `MovieRepository`, abstraem o acesso aos dados do sistema.

3. **Camada de Adaptadores (Adapters):**
 - **Adaptadores de Infraestrutura (Infrastructure Adapters):** Representados pela implementação de `MovieRepository`, `FileLoadCsv` e `FileLoad`, são responsáveis por adaptar a interface da infraestrutura para a interface definida nas portas de infraestrutura.
 - **Adaptadores de UI (User Interface Adapters):** Representados por `MoviesControllerAdapter`, são responsáveis por adaptar a interface do usuário para a interface definida nas portas de UI.

4. **Camada de Aplicação (Application):**
 - Contém as implementações dos casos de uso do sistema, como `MovieServiceImpl`, que implementa `MovieService`.

5. **Camada de Configuração (Config):**
 - Contém as configurações da aplicação, como `SwaggerConfig`, que configura o Swagger para documentação da API.

Essa organização permite que as regras de negócio sejam isoladas das implementações específicas da infraestrutura e da interface do usuário, facilitando a manutenção e testabilidade do sistema.


# Executando o Projeto

Para executar o projeto, siga o passo a passo abaixo:

1. Abra o terminal e utilize o seguinte comando:

```shell
mvn spring-boot:run
```

Aguarde alguns segundos e o sistema iniciará automaticamente. Alternativamente, você pode executá-lo clicando no botão "play" encontrado na sua IDE.

2. Após o sistema estar em execução, você pode acessar a rota especificada utilizando o `curl`:

```shell
curl -X GET http://localhost:8080/movie/search/winners/interval/min/max
```

o resultado será algo como:

```json
{
  "min":
     [
       { 
         "producer":"Joel Silver",
         "interval":1,
         "previousWin":1990,
         "followingWin":1991
       }
     ],
  "max":
  [
    {"producer":"Matthew Vaughn",
      "interval":13,
      "previousWin":2002,
      "followingWin":2015
    }
  ]
}
```

# Acessando Swagger

para acessar o swagger:

http://localhost:8080/swagger-ui/index.html

![img.png](img.png)

# Executando os testes

para executar os testes, o passo abaixo:

1. Abra o terminal e utilize o seguinte comando:

```shell
mvn test
```


Além dos logs de execução, podemos verificar os testes executados com sucesso analisando as últimas linhas:
```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```