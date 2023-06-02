Простое CRUD приложение с архитектурой MVC.

Есть три сущности. Writer, Post, Label.

База данных - текстовые файлы, содержащие json: db/labels.json, db/posts.json, db/writers.json.

# Компиляция
`mvn clean package`
# Запуск
`java -cp target/CRUD-1.0-SNAPSHOT.jar:$(cat deps.txt) com.danil.crud.App`

# Использование:
```label create <content>
label update <label_id> <new_content>
label delete <label_id>
label list
label get <label_id>

post create <writer_id> <content>
post update <post_id> <new_content>
post addlabel <post_id> <label_id>
post delete <post_id>
post dellabel <post_id> <label_id>
post list
post get <post_id>

writer create <first_name> <last_name>
writer update <writer_id> <first_name> <last_name>
writer delete <writer_id>
writer list
writer get <writer_id>
```
