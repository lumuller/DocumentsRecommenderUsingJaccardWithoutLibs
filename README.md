## by Luana Müller
DocumentsRecommender Project
================

## Goal

Thi project aim to recommend documents based on co-visitation of documents. The main idea is to discover and recommend similar documents using the style "people who saw that, also saw...".
The main challenge of this project was * not to use * libraries or frameworks to do the recommendation.

## Project details

This project was built using Java programming language and the open-source framework Spring.
It was also used Spring Boot.

### Rest API endpoints

#### POST: 

This endpoint must be used to feed the based about the documents read by the users. Every call to this endpoint requires the id of an `user` as parameter.
How to use: `$ curl -d"user=user1" http://localhost:8080/www.globoplay.globo.com/<idDocumento>/view`

#### GET:

Essa interface irá retornar no formato json a lista dos dez documentos mais similares em ordem decrescente.
Forma de uso: `$ curl http://localhost:8080/www.globoplay.globo.com/<idDocumento>/similar/`

#### DELETE:

Remove todos os dados da base.
Forma de uso: `$ curl -X "DELETE" http://localhost:8080/www.globoplay.globo.com/`

### Arquitetura

Dada a natureza da função para cálculo de coeficiênte de Jaccard, ela exige uma comparação de cada item (neste caso os documentos) com todos os demais itens, de modo a calcular a intersecção. Tal cálculo, irá comportar-se com tempo exponencial durante a execução do programa, tornando inviável o cálculo para grandes volumes de dados.

A partir deste fato, a arquitetura do API, baseou-se em evitar o cálculo da intersecção.
Deste modo, optou-se por manter registros de intersecções entre os documentos, registros estes a serem recalculados após cada nova visualização de um documento.

#### Sobre as estruturas de armazenamento (em ambas versões, com e sem banco de dados)

Sobre a classe UsersDocuments:
idUser: id do usuário
documentsList: lista contendo todos os documentos visualizados pelo usuário

Sobre a classe Intersection:
idIntersection: id da intersecção, gerado à partir da concatenação dos ids dos documentos A e B
idDocumentoA e idDocumentoB: id dos documentos envolvidos na intersecção

#### Sobre a lógica de funcionamento

##### Lógica de inserção de views
1. Recebe id do documento visualizado e id do usuário que visualizou o documento.
2. Procura o registro do usuários: se existir, recupera os dados, senão, cria um novo usuário.
3. Adiciona o documento à lista de documentos visualizados pelo usuário (somente se já não estiver lá).
4. Percorre a lista de documentos visualizados pelo usuário e, para cada um:
	4.1. Verifica se existe o registro de intersecção entre o documento recentemente visualizado e o documento contido na lista. Se existir, soma 1 ao valor armazenado no registro, senão, cria um novo registro de intersecção, com valor inicial igual a 1.
OBS: Durante a atualização das intersecções, será atualizado o valor da intersecção do documento com ele mesmo. Nos referimos a este registro como valor de auto-intersecção, o qual representa o número de acessos que o documento teve.
	
##### Lógica de recomendação
1. Recebe o id do documento base para a recomendação.
2. Recupera todos os registros de intersecções nos quais o documento base seja referenciado como documentoA ou documentoB.
3. Percorre a lista de intersecções e, para cada item:
	3.1. Recupera os registros de auto-intersecção do documento base e do documento com o qual ele está sendo comparado.
	3.2 Calcula o coeficiente de Jaccard: valorIntersecçãoAB / (valorAutoIntersecçãoA + valorAutoIntersecçãoB - valorIntersecçãoAB).
	3.3 Armazena os resultados em uma lista em memória.
4. Ordena a lista de resultados em ordem decrescente.
5. Particiona a lista, de modo a manter somente os 10 primeiros elementos (se a lista for originalmente menor que 10, será mantida em seu tamanho original).
6. Retorna a lista, para a exibição da mesma.

### Requisitos

Java Runtime Environment 8

Java Development Kit 8

Apache Maven 3.3.x ou superior

### Como rodar

Na pasta do projeto, execute os comandos abaixo para iniciar o serviço:

```
make
make run
```

### Testes executados

#### Testes Unitários

Foram incluídos no projeto, testes unitários para validar:
-Cenário onde 3 documentos foram co-visualizados pelos mesmo usuário, no mesmo número de vezes. 
-Cenário onde 5 documentos recebem visualizações por diversos usuários (10 diferentes usuários).
Ambos cenários validam que a recomendação recebida pelos documentos esteja de acordo com o resultado esperado.

#### Testes Funcionais

Os testes funcionais executados seguiram os mesmos cenários dos testes unitários, porém, utilizando números maiores de registros, e análise dos resultados obtidos.

#### Testes de carga

Testes de carga foram executados utilizando a ferramenta Postman.
A ferramenta perde a estabilidade (e trava) em testes acima de 10 mil iterações, portanto, os testes de POST foram executados usando estes valores.

Em testes de POST, para a inserção de 10 mil registros, foram obtidos os seguintes resultados:
	"delay": 0
	"count": 10000
	"totalPass": 10000
	"totalFail": 0
	"totalTime": 32182 ms
	
Em testes de GET, para a requisição de recomendações para 3 mil documentos, foram obtidos os seguintes resultados:
	"delay": 0,
	"count": 3000,
	"totalPass": 3000,
	"totalFail": 0,
	"totalTime": 18131 ms

