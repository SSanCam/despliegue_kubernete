
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

* ## Acceder a la base de datos

La base de datos MySQL está preconfigurada con los siguientes detalles:

* Host: localhost

* Puerto: 3306

* Nombre de la base de datos: apilol_bd

* Usuario: root

* Contraseña: 


* ## Despliegue de la API

Desde el directorio raiz del proyecto:

Tenemos un Dockerfile con las características necesarias para su despliegue en DockerDesktop

Tras compilar el proyecto en un .jar construimos su imagen en un contenedor 

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