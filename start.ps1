param (
 [string]$HOST_IP
)

docker-compose up --build -d

./gradlew clean build -x test

docker run -d -p 5000:5000 --name registry registry:2

minikube start --insecure-registry="${HOST_IP}:5000"

$env:PROCESS_SHELL = "powershell.exe"
$env:PROCESS_CONFIG = "-Command"
$env:REPO_CLONE_PATH = ""
$env:DB_URL = "jdbc:postgresql://localhost:5432/db"
$env:DB_USERNAME = "user"
$env:DB_PASSWORD = "password"
$env:DOCKER_HOST_IP_ADDRESS = "${HOST_IP}"
$env:DOCKER_REGISTRY_PORT = "5000"
$env:GITHUB_CLIENT_ID=""
$env:GITHUB_CLIENT_SECRET=""
$env:GITHUB_APP_ID=""

java -jar build/libs/CiTiFier-0.0.1.jar --spring.profiles.active=prod