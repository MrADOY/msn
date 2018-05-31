### Application de discution instantanée basée sur les microservices

Application Disponible à cette URL : http://serveurnicoant.ddns.net/msn/

> Hebergé sur le framework Heroku, Angular sur serveur local

> Application ne disposant pas des dernières fonctionnalités comme le panneau d'administration mais vous aurez l'occasion de 
> les tester avec des méthodes décrites en dessous, tous les fonctionnalités sont implementées en Java, il nous manque un 
> petit timing pour les intégrer au front...

#### Contributeurs

- Jourdain Nicolas Sadgasmification

  > Nicolas s'est occupé de la partie IHM de l'application, il a utilisé le framework Angular 5

- Thomas Buchard tombuch

  > Thomas a developpé le bus RabbitMQ permettant la transmission des données entre les microservices

- Lefevre Romain R0oma1n

  > Romain a permis le deploiement de tous les microservices sur Docker et s'est occupé de la gestion de la BDD avec notamment AWS

- Pietrzak Aurélien MrADOY

  > Aurélien s'est occupé des API et du developpemnt des microservices à l'aide de SpringBoot, Maven, Java, Swagger, JWT

#### Présentation du Projet

Notre projet devait respecter une certaine architecture afin de pouvoir remarquer les défauts et avantages de cette architecture 

![Capture d’écran 2018-05-30 à 20.20.45](https://github.com/MrADOY/msn/blob/master/images/Capture%20d’écran%202018-05-30%20à%2020.20.45.png)

#### Presentation des technologies 

- SpringBoot

  > Framework permettant de developper une application autonome qui embarque un serveur d'application

- Angular

  > Frameword permettant de developper une application client web en typescript

- Maven

  > Plugin qui permet la gestion des dépendances d'une application java

- Swagger

  > Permet d'avoir un catalogue de l'API d'un microservice, après le déploiement d'une application
  >
  > celle ci est joignable via l'URL http://ipappli:portappli/swagger-ui.html

- Docker

  > Permet de deployer une application dans un container 

- RabbitMQ

  > Librairie de gestion de BUS AMQP

- Heroku

  > Service cloud permettant d'heberger des applications Java

- JWT

  > Java web token nous permet de garantir la sécurité de l'application lors de la connexion il est fourni au client, il est a transmettre dans le header lors de l'appel au web service 

#### Comment deployer un micro service

*Requis*

1. Java 8
2. Maven
3. Un Bus RabbitMQ installé 

```
git clone https://github.com/MrADOY/msn.git
cd back
## A faire pour chaque micro service
mvn spring-boot:run
```

- registrar : fonctionne sur le port 5000
- chat  : fonctionne sur le port 5001
- Log : fonctionne sur le port 5002

#### Tester votre un micro service

```
Pour tous les appels, un jwt est a fournir dans le header de l'application, inscrivez vous puis connectez vous,le jwt se trouve dans la réponse à l'appel rest 
```

*Requis*

1. Postman (pour plus de facilité)

```
importer dans poster les collections postman qui sont présentes dans votre dossier microservices 
ex : /back/chat/Chat.postman_collection.json
Les appels sont déjà implementés 
```

Sinon effectuer des appels 

```
http://localhost:5000/api/auth/connexion 
en réunissant les variables nécessaires 
```

Via Swagger

1. Registrar
https://msn-registrar-istv.herokuapp.com/swagger-ui.html
2. Chat
https://msn-chat-istv.herokuapp.com/swagger-ui.html
3. Log/Admin
https://msn-log-istv.herokuapp.com/swagger-ui.html

```
ou si deployer en locan ...
http://localhost:5000/swagger-ui.html
puis naviger pour tester les API
```

![Capture d’écran 2018-05-30 à 20.41.44](https://github.com/MrADOY/msn/blob/master/images/Capture%20d’écran%202018-05-30%20à%2020.41.44.png)

#### Questions 

1. Quels sont les points faibles de cette architecture ?

   > RabbitMQ est la partie qui permet le fonctionnement de TOUTE l'application, une telle dépendance sur un composant est donc un point faible, deplus il est impossible d'envoyer des messages si registrar est down, puisque c'est lui qui indique si le destinataire du message est en ligne
   >
   > Il nous manque également un service discovery tel eureka à notre application pour éviter les dépendances niveau host lors d'appel au service.
   >
   > Le manque d'utilisation du load balancer est également un point faible de notre application

2. Proposez une architecture qui permet d’assurer un service continue sur la partie messagerie

   cliente, ici on ne prendra pas en compte la continuité de service de la partie administrative.

3. RabbitMQ est un point névralgique de notre architecture, que peut-on faire pour que cela ne

   le soit plus, ou tout au moins permettre une solution de secours.

   > Le bus est le centre de l'application, c'est par celui ci que toutes les informations transites, pour éviter cela on pourrait utiliser les websockets avec sockJS par exemple pour eviter de surcharger les appels API, notamment appeler registrar pour chaque message, les websockets permettent de developer des webhooks qui sont l'inverse de l'API dans le sens où c'est le serveur qui contacte le client, le service qui indique si l'utilisateur est connecté viendrait donc à disparaitre, cette technologie permettrait même de faire disparaitre le bus AMQP 

![Capture d’écran 2018-05-30 à 21.25.01](https://github.com/MrADOY/msn/blob/master/images/Capture%20d’écran%202018-05-30%20à%2021.25.01.png)
