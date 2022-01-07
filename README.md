[![Build Status](https://app.travis-ci.com/AMEMELYANOV/job4j_url_shortcut.svg?branch=master)](https://app.travis-ci.com/AMEMELYANOV/job4j_url_shortcut)
[![codecov](https://codecov.io/gh/AMEMELYANOV/job4j_url_shortcut/branch/main/graph/badge.svg?token=o0DgrIlfhK)](https://codecov.io/gh/AMEMELYANOV/job4j_url_shortcut)
# job4j_url_shortcut

## Описание проекта
Сервис работает через REST API. 
Для обеспечения безопасности пользователей, все ссылки на сайте заменяются ссылками на сервис.
# Функционал:
- Регистрация сайта.
- Аутентификация и авторизация с использованием Spring Security.
- Регистрация URL с присвоением уникальных кодов.
- Выполнение переадресации по уникальному коду URL (выполняется без авторизации).
- Вывод статистики по запросам.
# Применяемые технологии:
* Java 11
* Spring Boot 2
* Spring Security & JWT authorization
* Spring Data JPA
* PostgreSQL
# Применяемые инструменты:
* Maven
* Checkstyle 

## Примеры работы:

### 1. Регистрация сайта.
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

### 2. Авторизация.

Пользователь отправляет POST запрос
с `login` и `password` для сайта и получает Authorization `key` в блоке `Headers` у параметра `Authorization`.

Вызов:

`POST http://localhost:8080/login`

C телом JSON объекта:

`{"login": "gImZrToibN", "password": "IQAgsfke"}`

Пример:

![alt text](images/shortcut_img_2.jpg)

### 3. Регистрация URL.

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

### 4. Переадресация. Выполняется без авторизации.

Когда сайт отправляет ссылку с кодом в ответ возвращается
ассоциированный адрес и статус 302.

Вызов:

`GET http://localhost:8080/redirect/УНИКАЛЬНЫЙ_КОД`

Ответ от сервера в заголовке:

`HTTP CODE - 302 REDIRECT URL`

Пример:

![alt text](images/shortcut_img_4.jpg)

### 5. Статистика.

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

##Запуск проекта с использованием Docker Compose
### 1. Клонирование проекта.
В CLI выполнить команду - `git clone https://github.com/AMEMELYANOV/job4j_url_shortcut.git`.
В текущей директории будет создана папка `job4j_url_shortcut`, содержащая проект.

### 2. Сборка проекта.
Перейти в директорию проекта.
В CLI выполнить команду - `mvn install`.

### 3. Сборка проекта в Docker image.
В CLI выполнить команду - `docker build -t shortcut .`.

### 4. Запуск проекта с использованием docker-compose.
В CLI выполнить команду - `docker-compose up`.

### 5. Работа с проектом.
Работа с проектом осуществляется через любое приложение поддерживающее REST запросы, например CURL или Postman.
