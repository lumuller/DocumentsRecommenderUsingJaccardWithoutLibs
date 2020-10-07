## by Luana Müller
DocumentsRecommender Project
================

## Goal

Thi project aim to recommend documents based on co-visitation of documents. The main idea is to discover and recommend similar documents using the style "people who saw that, also saw...".
The main challenge of this project was **not to use** libraries or frameworks to do the recommendation.

## Project details

This project was built using Java programming language and the open-source framework Spring.
It was also used Spring Boot.

### Rest API endpoints

#### POST: 

This endpoint must be used to feed the based about the documents read by the users. Every call to this endpoint requires the id of an `user` as parameter.
How to use: `$ curl -d"user=user1" http://localhost:8080/www.recommender.com/<documentID>/view`

#### GET:

This endpoint will retrieve (json format) the ordered list of 10 most similar documents.
How to use: `$ curl http://localhost:8080/www.recommender.com/<documentID>/similar/`

#### DELETE:

This endpoint removes all the saved data: 
How to use: `$ curl -X "DELETE" http://localhost:8080/www.recommender.com/`

### Architecture

To calculate similarity among documen, I opted to use the Jaccard coefficient calculation [https://en.wikipedia.org/wiki/Jaccard_index].  
Due to the nature of Jaccard index, it is necessary to compare each item (in this case, a document) with all the remaining itens from the database, in order to calculate the intersection value (by intersection I mean the amount of users who had read the same pair of documents).
By example, lets imagine two documents named DocumentA and DocumentB.
DocumentA was read by 251 users. Document B was read by 345 users. The number of user that read both documents is 168. Then:
J(DocumentA, DocumentB) = 168 / (251 + 345 - 168)
J(DocumentA, DocumentB) = 0.3925
This means, that the similarity among DocumentA and DocumentB is around 39%. 
To achieve the goal of the project, this calculation must be done to every pair of documents available on the dataset, that results on a O(n2) problem, making it impossible to calculate for large volumes of data.

Based on this fact, we built the projects architecture in order to *avoid* calculate the intersection.
The approach used was to keep register if the intersection among documents. Each register is updated when a document is visualized. 


#### About the data model (used in both versions, with and without database)

*About the class UsersDocuments:*
idUser: user ID.
documentsList: list containing all documents visualized by the user.

*About the class Intersection:*
idIntersection: Intersection ID generated using DocumentA and DocumentB ID concatenation.
idDocumentoA e idDocumentoB: Id of the document A and B.
value: amount of users who visualized both documents.

#### About how it works

##### How the Intersection is calculated 

1. When a document is visualized, the POST method will receive the document ID and the user ID;
2. Search on class UsersDocuments, for the user's register: If exists, retrieve data, if not, create a new user;
3. The document is added to the list of documents visualized by the user (if is not there already) on class UsersDocuments;
4. Check the list of documents visualized by this user, and for each document:
	4.1. Check if exists an intersection register representing the recently visualized document and the document from the user´s list. If exists, add 1 to the storage value, else, created a new intersection register with valuen equal to 1.
NOTE: During the intersections update, we update also the intersection value amont the document with itself. We cqall the register *self-intersection*, and it only represents the number of access a document had.
	
##### How recommendation is made

1. When the recommendation list is required, the GET method will receive the document ID (document base);
2. Retrieve all intersection registers in which the document base is referred as documentA or documentB;
3. Check the intersection list, and for each intersection:
	3.1. Retrieves the self-interction register from base document and the compared document;
	3.2. Calculate Jaccard index based on the retrieve values: intersectionAB / (selfIntersectionA + selfIntersectionB - intersectionAB);
	3.3. Save the results on a list;
4. Order the list;
5. Return the 10 main results from the list on a Json format.

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

