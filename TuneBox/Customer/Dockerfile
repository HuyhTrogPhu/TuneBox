# Sử dụng môi trường Java 21
FROM openjdk:21-jdk-slim

# Đặt thư mục làm việc
WORKDIR /app

# Copy file JAR từ thư mục target vào container
COPY target/*.jar app.jar

# Khai báo lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
