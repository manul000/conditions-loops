# Условия и циклы — три варианта

Это самостоятельный проект: для передачи достаточно этой папки целиком.

Установите JDK 22. В переменных среды Windows задайте `JAVA_HOME` — папку JDK, а в `Path` добавьте `%JAVA_HOME%\bin`.
Откройте PowerShell в папке проекта. Maven Wrapper загрузит Maven автоматически; первый запуск требует интернета.

Сборка:

```powershell
.\mvnw.cmd package
```

Запуск:

```powershell
java -jar .\target\02-conditions-loops-1.0-SNAPSHOT.jar
```

В меню выберите вариант: 1 — анализ строки, 2 — калькулятор товаров, 3 — проверка пароля. Можно передать номер сразу:

```powershell
java -jar .\target\02-conditions-loops-1.0-SNAPSHOT.jar 1
```
