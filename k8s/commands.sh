# Создание Secret
kubectl apply -f postgresdb-secret.yml
kubectl get secrets

# Создание ConfigMap
kubectl apply -f postgesdb-configmap.yml
kubectl get configmaps

# Создание развертывания postgresdb-deployment
kubectl apply -f postgresdb-deployment.yml
kubectl logs -l app=postgresdb
kubectl describe pod

# Создание развертывания spring-boot-deployment
kubectl apply -f spring-boot-deployment.yml
kubectl logs -l app=spring-boot
kubectl describe pod