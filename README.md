Docker Compose with Spring Boot, MySQL, NGINX


What you'll build

A Spring Boot application with MySQL and NGINX running inside Docker containers


What you'll need

Docker CE


Stack

Docker,
Java,
Spring Boot,
MySQL,
NGINX,
Maven.

To Run the docker-compose you need to change directory to dockerhubpull then you can able find required docker-compose.yml

then follow below procedure.....

Run command docker-compose up --build

Access to notes by id curl --header "Content-Type:application/json" --request GET   http://localhost/easynotes/api/notes/{id}

Access to create notes,but you need to pass the body,and as well so the request is like ....

curl --header "Content-Type:application/json" --request POST --data '{"content":"this is first note","title":"testV1"}'  http://localhost/easynotes/api/notes

Access to delete the note by id ,and you need to pass id as parameter.Then the request is ...

curl  --request DELETE   http://localhost/easynotes/api/notes/{id}

Access to update notes,but you need to pass the body,and id as a path variable.So the request is like ....

curl --header "Content-Type:application/json" --request PUT --data '{"id":2,"content":"this is first note","title":"testV1"}'  http://localhost/easynotes/api/notes/{id}
