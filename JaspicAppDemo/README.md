# Build
mvn clean package && docker build -t lab.jefrajames/JaspicDemo .

# RUN

docker rm -f JaspicDemo || true && docker run -d -p 8080:8080 -p 4848:4848 --name JaspicDemo lab.jefrajames/JaspicDemo 