# cartservice implemantation

this project developed on java 11 version

you can create jar with 'mvn clean install'

you can run jar with 'java -jar target/cartservice-0.0.1-SNAPSHOT.jar'

# Project Architecture

# Api Documentation
    http://localhost:8080/swagger-ui.html

# in-memory database access
    http://localhost:8080//h2-console
    JDBC Url : jdbc:h2:mem:cart-db
    user : sa

# issues
    - delivery cost calculation can be extended with numberOfProducts and numberOfDelivery,
        now its based on static values
    - entities (Product, Campaign etc.) can be updated with different values via PUT or PATCH methods