# Prerequisites

- Акаунт на azure
- azure storage explorer

# Для початку потрібно створити необхідні ресурси

1. Створюємо нову ресурс групу або використовуємо існуючу.
2. Створюємо storage account. При створенні в опціях заходимо в вкладку advanced і поле Data Lake storage Gen2 обираємо enabled.
![lab-8](../img/lab_8_1.jpg)
Після створення сторедж аккаунту створюємо контейнер і папку в ньому.
![lab-8](../img/lab_8_2.jpg)
![lab-8](../img/lab_8_3.jpg)
3. Також нам буде потрібен EventHub instance. Можна викоритати з попередніх лабораторних.
4. Створюємо app registrations для надання доступів
![lab-8](../img/lab_8_8.jpg)
В ньому в вкладці certificates & secrets створюємо новий секрет і одразу копіюємо його значення.
![lab-8](../img/lab_8_9.jpg)
В resource group в вкладці IAM додаємо створеному app registration права contributor 
![lab-8](../img/lab_8_11.jpg)

5. Створюємо databricks service. Після створення натискаємо Launch worckspace.
![lab-8](../img/lab_8_4.jpg)
Після переходу створюємо cluster
![lab-8](../img/lab_8_5.jpg)
В кластері встановлюємо бібліотеку maven `com.microsoft.azure:azure-eventhubs-spark_2.11:2.3.18`
![lab-8](../img/lab_8_6.jpg)

Після цього створюємо notebook для коду пайтон та для scala.
![lab-8](../img/lab_8_7.jpg)

В notebook пайтон вставляємо код з файла mounty.py 
В notebook скала вставляємо код з файла eventHub.scala

В відповідні поля mounty.py потрібно вставити свої конфігураційні дані з app registrations
 `client.id` -> `application_id`
 `client.secret` -> secret value який ми створили
 `client.endpoint` потрібно змінити значення перед `/oauth2/token` на `Directory (tenant) ID`

 В `storage` підставляємо імена свого стореджу та контейнеру 
![lab-8](../img/lab_8_10.jpg)

заходимо в azure storage explorer та надаємо до контейнера та папки доступ нашому app registration.
![lab-8](../img/lab_8_12.jpg)

Запускаємо кнопкою Run all в notebook(достатньо один раз)

6. Відкриваємо notebook scala і вставляємо в нього код з eventHub.scala. Тут потрібно замінити відповідні ключі, які ми вставляли в notebook пайтона. 
`client id` - `appid`
`secret` - `password`
`tenantID` - `tenantID`
`fileSystemName` - `container name`, `storageAccountName` - ім'я аккаунт сторедж.
connection string це connection string eventhub space, setEventHubName - ім'я eventHub у який ми записуємо дані.

Далі нам потрібно замінити поля які ми будемо парсити у файлики csv це потрібно змінити у функції filtered в відповідності до даних з датасету, а також у файлі ```EventHubWriter.py``` 

Далі ми вписуємо директорію у яку ми маунтели таку і нам вивело path. Вставляємо у рядок option та start

Після цього потрібно це заранити і отримати повідомлення без помилок 
![lab-8](../img/lab_8_13.jpg)

Пізніше потрібно запустити код на пайтоні. Отримуємо вивід:
![lab-8](../img/lab_8_14.jpg)


Тепер за допомогою Postman потрібно відправити POST запит, як на скріні. Не забуваємо в Connection-Type додати application/json.
В запиті в поле url вставляємо посилання на наш датасет
![lab-8](../img/lab_8_15.jpg)

Після успішного виконання в azure storage explorer в нашій директорії з'являються csv файлики заповлені даними з датасету
![lab-8](../img/lab_8_16.jpg)
![lab-8](../img/lab_8_17.jpg)