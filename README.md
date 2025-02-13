# Aplicación que estamos usando

Estamos usando la aplicación la cual fue el trabajo de fin de trimster pasado, esta es una api rest la cual se estructura principalmente en 3 entidades 

### **Usuario**: Almacena información sobre los usuarios del sistema.
### **Campeón**: Contiene información sobre los campeones disponibles en el sistema.
### **Partida**: Registra las partidas jugadas y sus estadisticas, vinculando a un usuario con un campeón utilizado en una partida específica.

# EndPoints

Cada entidad tiene un par de enpoints pero voy a poner solo los más relevantes al proyecto y a lo que vamos a usar:

## Usuario:

### Registro y autenticación
- `POST /usuarios/registro`: Crea un nuevo usuario.

## Campeones:

### Gestión de campeones
- `POST /campeones`: Agrega un nuevo campeón (solo admins)


## Partidas:

### Gestión de partidas
- `POST /partidas`: Registra una nueva partida asociada a un usuario y un campeón.


<br>
<br>
<br>
<br>

# Preparación del entorno de Kubernete:

## 1. Instalación de herramientas necesarias

### Docker: (Usando su versión [*desktop*](https://www.docker.com/products/docker-desktop/) ) Para crear y administrar imágenes de contenedores.

### Kubectl: Para interactuar con Kubernetes.


```bash
# Instalación de Kubectl Windows
# Desde el directorio raíz del proyecto

# Descargar la última versión de kubectl
curl -LO https://dl.k8s.io/release/v1.28.0/bin/windows/amd64/kubectl.exe
# Crea una carpeta para kubectl (si no existe)
mkdir C:\kubectl
# Movemos el ejecutable a ese directorio
move kubectl.exe C:\kubectl\

# Para Linux/macOS
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
chmod +x kubectl
sudo mv kubectl /usr/local/bin/

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

### [XAMPP](https://www.apachefriends.org/es/index.html) [Opcional]

Para la creación y manipulación manual de nuestra base de datos. 

No es estrictamente necesario, ya que la base de datos la tenemos en un contenedor, pero así tendremos una interfaz gráfica más fácil y cómoda para manejar datos, en lugar de por terminal.

Si se instala *XAMPP*, al iniciar la aplicacion iniciaremos MySQL con el botón **start** que tiene adjaccente y accederemos desde nuestro navegador con *http://localhost/phpmyadmin* a **phpMyAdmin** donde podremos manejar la base de datos de la API.


La base de datos MySQL (para XAMPP) está preconfigurada con los siguientes detalles:

* Host: localhost

* Puerto: 3306

* Nombre de la base de datos: apilol_bd

* Usuario: root

* Contraseña: 


## 2. Implementación de los contenedores

### Obtén la imagen de los contenedores:


#### Contenedor para la API

```bash
docker pull manuelmgarmorn/mi-apli-lol:latest
```

Una vez tenemos la imagen, despliegala: 

```bash
docker run -d --name api-container -p 8080:8080 --network mi-red \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-container:3306/apilol_bd \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  manuelmgarmorn/mi-apli-lol:latest
```

#### Contenedor para la BBDD

Crea otro contenedor con la imagen de MySQL. Asegurate de tener la imagen oficial con:

```bash
docker pull mysql:5.7
```

Crea y ejecuta el contenedor con:

```bash
docker run -d --name mysql-container -e MYSQL_ROOT_PASSWORD=password -p 3306:3306 mysql:5.7
```

* **--name** Define el nombre del contenedor, puedes modificarlo.
* **-e MYSQL_ROOT_PASSWORD=my-secret-pw** Define la contraseña de la base de datos, recomendamos que se use una personal.



#### Crea una red Docker para conectar los contenedores entre ellos:

Creamos la red docker

```bash
docker network create mi-red
```
Conectamos ambos contenedores con:

```bash 
# BBDD
docker network connect mi-red mysql-container
# API
docker network connect mi-red api-container
```

## 3. Despliegue en Kubernetes


```bash
# **PENDIENTE** Creacion de los archivos YAML de los servicios y configuración e indicar directorio dónde se alojan.
```

Elige un directorio desde donde alojar los archivos .yaml 

1. Aplicamos los archivos de configuración YAML en Kubernetes
   
```bash
cd kubectl/
kubectl apply -f api-deployment.yaml
kubectl apply -f api-service.yaml
kubectl apply -f mysql-deployment.yaml
kubectl apply -f mysql-service.yaml

# Si están todos en el mismo directorio puedes simplemente ejecutar 
kubectl apply -f kubernetes/
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