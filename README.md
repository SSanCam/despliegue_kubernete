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
<br>
<br>
<br>

* Desde la terminal de DockerDesktop, ejecuta los siguientes comandos: 

*Windows (powershell)*

```bash
# Descargar kubectl.exe
Invoke-WebRequest -Uri "https://dl.k8s.io/release/v1.28.0/bin/windows/amd64/kubectl.exe" -OutFile "kubectl.exe"
# Crear la carpeta para kubectl (si no existe)
New-Item -ItemType Directory -Path "C:\kubectl" -Force
# Mover el ejecutable a la carpeta
Move-Item -Path ".\kubectl.exe" -Destination "C:\kubectl\"
# Verificar la instalación (después de reiniciar PowerShell)
kubectl version --client
```

*Linux*

```bash
# Descargar la última versión de kubectl
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
# Dar permisos de ejecución
chmod +x kubectl
# Mover el ejecutable a una ubicación en el PATH
sudo mv kubectl /usr/local/bin/
# Verificar la instalación
kubectl version --client
Agrega la carpeta *C:\kubectl* a tu variable de entorno PATH:
- Abre el menú de inicio y busca "Editar las variables de entorno del sistema".
- Haz clic en "Variables de entorno".
- En la sección "Variables del sistema", busca la variable Path y haz clic en "Editar".
- Agrega una nueva entrada con la ruta C:\kubectl.
- Guarda los cambios.
Verifica la instalación desde la terminal de tu IDE con:
kubectl version --client
```

*macOS*

```bash
# Descargar la última versión de kubectl
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/darwin/amd64/kubectl"
# Dar permisos de ejecución
chmod +x kubectl
# Mover el ejecutable a una ubicación en el PATH
sudo mv kubectl /usr/local/bin/
# Verificar la instalación
kubectl version --client

```
Habilita Kubernetes en Docker Desktop siguiendo estos pasos:

Si estás usando la terminal de Docker Desktop, reinicia la aplicación y después: 

1. Abre Docker Desktop.
2. Dirigete a *settings* (configuración).
3. Busca la opción *Kubernetes* en el menú lateral izquierdo.
4. Marca la casilla *Enable Kubernetes*.
5. Finalmente haz click en *Apply & Restart*.

Espera a que finalice la instalación, *puede tardar varios minutos.*

---

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

---

## 2. Despliegue en Kubernetes

Vamos a desplegar los archivos `.yaml` que definen el **Deployment y el Service** tanto de **MySQL como de la API** en Kubernetes.

Primero, comprueba en *Docker Desktop > Settings > Kubernetes* que el servicio está activo.

Puedes revisar su progreso desde la terminal ejecutando:

```bash
kubectl cluster-info
```

* Ejecuta los siguientes comandos desde el directorio donde se encuentran los archivos `.yaml` (en este caso, en la carpeta `kubernetes/` dentro del directorio raíz):

```bash
# Aplicamos todos los archivos .yaml necesarios 
kubectl apply -f .
```

Debería devolverte:

```bash
deployment.apps/mysql-db created
service/mysql-db created
deployment.apps/spring-boot-app created
service/spring-boot-app created
```

* Verifica que los pods se estén ejecutando:

```bash
kubectl get pods
```

Podrás ver que se están descargando las imágenes y creando los contenedores:

```bash
NAME                               READY   STATUS              RESTARTS   AGE
mysql-db-55c95dcdfb-26hr4          0/1     ContainerCreating   0          39s
spring-boot-app-66447db89f-fgsxx   0/1     ContainerCreating   0          39s
```

Finalmente, cuando su estatus cambie a *running* significa que el despliegue ha sido exitoso y los contenedores están funcionando dentro del clúster en Kubernetes

```bash
NAME                               READY   STATUS    RESTARTS   AGE
mysql-db-55c95dcdfb-26hr4          1/1     Running   0          2m26s
spring-boot-app-66447db89f-fgsxx   1/1     Running   0          2m26s
```
* Para acceder a la API desde el navegador o Postman, debemos redirigir el puerto:

```bash
# Redirigimos el puerto
kubectl port-forward service/spring-boot-app 8080:8080
```
Luego accedemos desde nuestro navegador con *http://localhost:8080*

O podemos acceder a algún endpoint específico como *http://localhost:8080/usuarios*