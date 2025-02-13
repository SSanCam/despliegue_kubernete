
# Preparación del entorno de Kubernete:

## Instalación de herramientas necesarias

* Docker: (Usando su versión [*desktop*](https://www.docker.com/products/docker-desktop/) ) Para crear y administrar imágenes de contenedores.

* Kubectl: Para interactuar con Kubernetes.


```bash
# Instalación de Kubectl
# Desde el directorio raíz del proyecto

# Descargar la última versión de kubectl
curl -LO https://dl.k8s.io/release/v1.28.0/bin/windows/amd64/kubectl.exe
# Crea una carpeta para kubectl (si no existe)
mkdir C:\kubectl
# Movemos el ejecutable a ese directorio
move kubectl.exe C:\kubectl\
```

Agrega la carpeta *C:\kubectl* a tu variable de entorno PATH:

- Abre el menú de inicio y busca "Editar las variables de entorno del sistema".

- Haz clic en "Variables de entorno".

- En la sección "Variables del sistema", busca la variable Path y haz clic en "Editar".

- Agrega una nueva entrada con la ruta C:\kubectl.

- Guarda los cambios.

Verifica la instalación desde la terminal de tu IDE con:

```bash
kubectl version --client
```

Deberás ver algo como ésto:


```bash
Client Version: v1.28.0
```

* ## Creación de la base de datos 

Al usar DockerDesktop, creamos nuestra base de daots en un contenedor:

1. Desgarga la imagen de MySQL

```bash
docker pull mysql:5.7
```

2. Crea y ejecuta el conenedor

```bash 
docker run -d --name mysql-container -e MYSQL_ROOT_PASSWORD=password -p 3306:3306 mysql:5.7
``` 

Explicación de los parámetros:

    -d: Ejecuta el contenedor en segundo plano (modo "detached").

    --name mysql-container: Asigna un nombre al contenedor (mysql-container en este caso).

    -e MYSQL_ROOT_PASSWORD=my-secret-pw: Define la contraseña del usuario root de MySQL.

    -p 3306:3306: Mapea el puerto 3306 del contenedor al puerto 3306 de tu máquina local.

    mysql:5.7: Especifica la imagen de MySQL que se usará.

Podemos verificar el contenedor activo por terminal con:

```bash
docker ps
```

En nuestro archivo *application.properties* situada en nuestro directorio *root/resources/* agregamos las líneas encesarias para conectarnos a la BBDD a través de nuestra API

```properties
# Configuración de la base de datos MySQL
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# URL de conexión a la base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/nombre_bd

# Credenciales de acceso
spring.datasource.username=nombre_usuario
spring.datasource.password=clave_usuario
```

* ## Despliegue de la API


* ## Despliegue en Kubernetes

1. Aplicamos los archivos de configuración YAML en Kubernetes
   
```bash
kubectl apply -f api-deployment.yaml
kubectl apply -f api-service.yaml
kubectl apply -f mysql-deployment.yaml
kubectl apply -f mysql-service.yaml
```

2. Confirmamos el despliegue con 
```bash
kubectl get pods
```

3. Accedemos a la API
```bash
# Redirigimos el puerto
kubectl port-forward service/api-rest-service 8080:8080
```
Luego accedemos desde nuestro navegador con *http://localhost:8080*