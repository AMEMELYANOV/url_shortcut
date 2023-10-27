# URL_shortcut

## <p id="contents">Оглавление</p>

<ul>
<li><a href="#01">Описание проекта</a></li>
<li><a href="#02">Стек технологий</a></li>
<li><a href="#03">Требования к окружению</a></li>
<li><a href="#04">Сборка и запуск проекта</a>
    <ol type="1">
        <li><a href="#0401">Сборка проекта</a></li>
        <li><a href="#0402">Запуск проекта</a></li>
    </ol>
</li>
<li><a href="#05">Взаимодействие с приложением</a>
    <ol  type="1">
        <li><a href="#0501">Регистрация сайта</a></li>
        <li><a href="#0502">Авторизация</a></li>
        <li><a href="#0503">Регистрация URL</a></li>
        <li><a href="#0504">Переадресация</a></li>
        <li><a href="#0505">Статистика</a></li>
    </ol>
</li>
<li><a href="#06">Запуск проекта с использованием Docker</a>
<ol  type="1">
        <li><a href="#0601">Клонирование проекта</a></li>
        <li><a href="#0602">Сборка проекта в Docker image</a></li>
        <li><a href="#0603">Сборка и запуск проекта с использованием docker-compose</a></li>
        <li><a href="#0604">Работа с проектом</a></li>
</ol>
<li><a href="#07">Работа проекта через K8s</a>
<ol  type="1">
        <li><a href="#0701">Создание Secret</a></li>
        <li><a href="#0702">Создание ConfigMap</a></li>
        <li><a href="#0703">Создание deployment для Postgres</a></li>
        <li><a href="#0704">Создание deployment для Spring Boot</a></li>
        <li><a href="#0705">Проверка работоспособности</a></li>
        <li><a href="#0706">Работа с проектом</a></li>
</ol>
</li>
<li><a href="#contacts">Контакты</a></li>
</ul>

## <p id="01">Описание проекта</p>

Сервис REST API для кодирования html ссылок во внутренние коды сервиса.

Функционал:

- Регистрация сайта.
- Аутентификация и авторизация с использованием Spring Security.
- Регистрация URL с присвоением уникальных кодов.
- Выполнение переадресации по уникальному коду URL (выполняется без авторизации).
- Вывод статистики по запросам.

<p><a href="#contents">К оглавлению</a></p>

## <p id="02">Стек технологий</p>

- Java 17
- Spring Boot 2.7, Spring Security, Spring Data
- PostgreSQL 14, Liquibase 4
- JUnit 5, Mockito
- Maven 3.8
- Lombok 1.18
- Docker

  Инструменты:

- Javadoc, JaCoCo, Checkstyle

<p><a href="#contents">К оглавлению</a></p>

## <p id="03">Требования к окружению</p>

Java 17, Maven 3.8, PostgreSQL 14, Docker

<p><a href="#contents">К оглавлению</a></p>

## <p id="04">Сборка и запуск проекта</p>

### <p id="0401">1. Сборка проекта</p>

Команда для сборки в jar:
`mvn clean package -DskipTests`

<p><a href="#contents">К оглавлению</a></p>

### <p id="0402">2. Запуск проекта</p>

Перед запуском проекта необходимо создать базу данных shortcut
в PostgreSQL, команда для создания базы данных:
`create database shortcut;`
Средство миграции Liquibase автоматически создаст структуру
базы данных и наполнит ее предустановленными данными.
Команда для запуска приложения:
`mvn spring-boot:run`

<p><a href="#contents">К оглавлению</a></p>

## <p id="05">Взаимодействие с приложением</p>

### <p id="0501">1. Регистрация сайта</p>

Сервисом могут пользоваться разные сайты. Каждому сайту выдается пара логин и пароль.
Чтобы зарегистрировать сайт в системе, необходимо отправить запрос:

Вызов:
`POST http://localhost:8080/registration`

C телом JSON объекта:

`{"site" : "job4j.ru"}`

Ответ от сервера:

`{
"registration": true/false,
"login": УНИКАЛЬНЫЙ_КОД,
"password": УНИКАЛЬНЫЙ_КОД
}`

Пример:

![alt text](images/shortcut_img_1.jpg)

Флаг `registration` указывает, что регистрация выполнена или нет,
`false` - сайт уже есть в системе.

<p><a href="#contents">К оглавлению</a></p>

### <p id="0502">2. Авторизация</p>

Пользователь отправляет POST запрос
с `login` и `password` для сайта и получает Authorization `key` в блоке `Headers` у параметра `Authorization`.

Вызов:

`POST http://localhost:8080/login`

C телом JSON объекта:

`{"login": "gImZrToibN", "password": "IQAgsfke"}`

Пример:

![alt text](images/shortcut_img_2.jpg)

<p><a href="#contents">К оглавлению</a></p>

### <p id="0503">3. Регистрация URL</p>

Поле того, как пользователь зарегистрировал свой сайт он может
отправлять на сайт ссылки и получать преобразованные ссылки в коды.

Вызов:

`POST http://localhost:8080/convert`

C телом JSON объекта:

`{"url": "https://job4j.ru/profile/exercise/106/task-view/532"}`

Ответ от сервера:

`{"code": УНИКАЛЬНЫЙ_КОД}`

Пример:

![alt text](images/shortcut_img_3.jpg)

<p><a href="#contents">К оглавлению</a></p>

### <p id="0504">4. Переадресация</p>

Выполняется без авторизации.
Когда сайт отправляет ссылку с кодом в ответ возвращается
ассоциированный адрес и статус 302.

Вызов:

`GET http://localhost:8080/redirect/УНИКАЛЬНЫЙ_КОД`

Ответ от сервера в заголовке:

`HTTP CODE - 302 REDIRECT URL`

Пример:

![alt text](images/shortcut_img_4.jpg)

<p><a href="#contents">К оглавлению</a></p>

### <p id="0505">5. Статистика</p>

В сервисе считается количество вызовов каждого адреса.
По сайту можно получить статистку всех адресов и количество вызовов этого адреса - параметр `total`.

Вызов:

`GET http://localhost:8080/statistic`

Ответ от сервера JSON:

`{`
{"url": URL, "total": 0},
{"url": "https://job4j.ru/profile/exercise/106/task-view/532", "total": 103}
`}`
![alt text](images/shortcut_img_5.jpg)

<p><a href="#contents">К оглавлению</a></p>

## <p id="06">Запуск проекта с использованием Docker</p>

### <p id="0601">1. Клонирование проекта</p>

В CLI выполнить команду - `git clone https://github.com/AMEMELYANOV/job4j_url_shortcut.git`.
В текущей директории будет создана папка `job4j_url_shortcut`, содержащая проект.

<p><a href="#contents">К оглавлению</a></p>

### <p id="0602">2. Сборка проекта в Docker image</p>

В CLI выполнить команду - `docker build -t shortcut .`.

<p><a href="#contents">К оглавлению</a></p>

### <p id="0603">3. Сборка и запуск проекта с использованием docker-compose</p>

Для сборки Docker образов сервисов приложения необходимо перейти в корневую папку приложения (должна содержать
файл docker-compose.yml) и в CLI выполнить команду - `docker-compose build`.
Для создания контейров из образов и запуска приложения необходимо выполнить команду - `docker-compose up`.
Если образы приложения отсутствуют в локальном репозитории, выполнение команды `docker-compose up`
сначала соберет образы сервисов, далее на их основе создаст котейнеры и выполнит их запуск.

<p><a href="#contents">К оглавлению</a></p>

### <p id="0604">4. Работа с проектом</p>

Работа с проектом осуществляется через любое приложение поддерживающее REST запросы, например CURL или Postman.

<p><a href="#contents">К оглавлению</a></p>

## <p id="07">Работа проекта через K8s</p>
Перед выполнением развертывания необходимо перейти в подкаталог `/k8s`, далее все команды выполняются через CLI

### <p id="0701">1. Создание Secret</p>

Выполнить команду - `kubectl apply -f postgresdb-secret.yml`.
Выполнить проверку, что `secret` создан - `kubectl get secret`.

<p><a href="#contents">К оглавлению</a></p>

### <p id="0702">2. Создание ConfigMap</p>

Выполнить команду - `kubectl apply -f postgesdb-configmap.yml`.
Выполнить проверку, что `configmap` создан - `kubectl get configmaps`.

<p><a href="#contents">К оглавлению</a></p>

### <p id="0703">3. Создание deployment для Postgres</p>

Выполнить команду - `kubectl apply -f postgresdb-deployment.yml`.
Выполнить проверку, что pod - `postgresdb` создан - `kubectl get pods`.

<p><a href="#contents">К оглавлению</a></p>

### <p id="0704">4. Создание deployment для Spring Boot</p>

Выполнить команду - `kubectl apply -f spring-boot-deployment.yml`.
Выполнить проверку, что pod - `spring-boot` создан - `kubectl get pods`.

<p><a href="#contents">К оглавлению</a></p>

### <p id="0705">5. Проверка работоспособности</p>

Выполнить команду - `kubectl get service`.
Убеждаемся, что сервисы `postgresdb` и `spring-boot` запущены.
Выполнить команду - `minikube service spring-boot-service` для получения ссылки, ip-адреса и порта для дальнейшей
работы.

<p><a href="#contents">К оглавлению</a></p>

### <p id="0706">6. Работа с проектом</p>

Работа с проектом осуществляется через любое приложение поддерживающее REST запросы, например CURL или Postman.

<p><a href="#contents">К оглавлению</a></p>

## <p id="contacts">Контакты</p>

[![alt-text](https://img.shields.io/badge/-telegram-grey?style=flat&logo=telegram&logoColor=white)](https://t.me/T_AlexME)
&nbsp;&nbsp;
[![alt-text](https://img.shields.io/badge/@%20email-005FED?style=flat&logo=mail&logoColor=white)](mailto:amemelyanov@yandex.ru)
&nbsp;&nbsp;

<p><a href="#contents">К оглавлению</a></p>