
# Solução - Desafio Produtos de Seguros 🚀

## Executando o projeto
Apos clonado o projeto, abra o terminal e entre na pasta target:

Execute o comando abaixo e aguarde o Spring inicializar.:
```
java -jar seguros-0.0.1-SNAPSHOT.jar
```

## Descrição da solução
Criei um microservico expondo api REST que recebe um payload em json, calcula o preco tarifado de produtos de seguro, salva no 
banco de dados em memoria H2 e retorna como resultado outro payload em json com o preco tarifado.

### Arquitetura
Optei pela arquitetura Port & Adapters, essa arquitetura permite desacoplar as camadas de regras de negocio, 
saida dos dados (persistencia) e recepcao dos dados(rest controller). 
Também facilita muito na hora de criar testes de unidade e integrado.

### Calculando valor tarifado
Usei um enum como forma de mapear cada categoria com seus respectivos valores, pois seu uso proporciona uma maneira 
elegante e robusta de representar conjuntos de constantes, proporcionando clareza e legibilidade do codigo.
Usei uma classe concreta para calcular o preco tarifado retornando um big decimal. 

Abordei essa soluçao com enum porque imagino que não ira passar de trinta produtos. Caso seja necessario incluir novos 
produtos ou atualizar a taxa de imposto, basta adicionar/alterar uma unica linha.

A aplicação foi construida apenas com as funcionalidades de cadastrar e atualizar produtos. (Conforme o escopo da 
regra de negocio). 
Cada produto calculado/atualizado é unico na base de dados, pois seu objetivo é apenas calcular o preco tarifado de 
produtos. Utilizei as boas praticas de mercado e resolvi de forma simples.

Para cada interação entre as camadas utilizei a biblioteca mapstruct que permite converter um tipo de objeto em outro objeto.

### Logs
TODO...


### Calculando e gravando produto
TODO...

### Calculando e atualizando produto
TODO...


    







