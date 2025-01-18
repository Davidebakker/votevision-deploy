# Frontend Stage
FROM node:20 AS frontend-build
WORKDIR /frontend-vue

# Install dependencies and build the frontend
COPY frontend-vue/package*.json ./
RUN npm ci  # Ensure clean install for each architecture
COPY frontend-vue/ .
RUN npm run build

# Backend Stage
FROM eclipse-temurin:21-jdk AS backend-build
WORKDIR /backend-java

# Copy source code and Maven wrapper
COPY backend-java/ .

# Fix permissions for Maven wrapper
RUN chmod +x mvnw \
# Build the backend JAR
&& ./mvnw clean package -DskipTests

# Final Stage
FROM nginx:stable-alpine AS final
WORKDIR /app

# Install Java in the final image
RUN apk add --no-cache openjdk21

# Copy backend JAR
COPY --from=backend-build /backend-java/target/*.jar /app/backend.jar

# Copy frontend build files
COPY --from=frontend-build /frontend-vue/dist /usr/share/nginx/html

# Copy Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose the port
EXPOSE 8080

# Start both backend and Nginx
CMD ["sh", "-c", "java -jar /app/backend.jar & nginx -g 'daemon off;'"]
