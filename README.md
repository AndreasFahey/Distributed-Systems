# Distributed-Systems
DS Project Repo Part 1 & Part 2.

Author: Andreas Fahey G00346830

To clone this Project enter the following:   git clone https://github.com/AndreasFahey/Distributed-Systems.git

Part1: gRPC Password Service
- Once cloned, in eclipse go into the pom file and generate-sources.
- Then Run the Password Server class before Running the Password Client class.
- The Client will give you an error after input if the server isn't running.

References for Part1:
- https://github.com/john-french
- https://www.youtube.com/watch?v=OZ_Qmklc4zE
- https://www.youtube.com/watch?v=JFzAe9SvNaU
- https://www.youtube.com/watch?v=eUu29SrGYTA&t=34s
- https://www.baeldung.com/grpc-introduction

Part2: RESTful User Account Service
- Once cloned, in eclipse go into the pom file and generate-sources.
- Run the following commands in Cmder.
- Run the server.jar - "java -jar server.jar"
- Run the service DS-PasswordService-0.0.1-SNAPSHOT.jar "java -jar DS-PasswordService/target/DS-PasswordService-0.0.1-SNAPSHOT.jar server   DS-PasswordService/accountAPIConfig.yaml". You must clone to use this jar in the target directory to be able also use accountAPI.yaml
- Open Postman and start trying GET, POST, DELETE, PUT. JSON works fine but had trouble trying to get XML to work, throws a Error [500].

References for Part2:
- https://howtodoinjava.com/dropwizard/tutorial-and-hello-world-example/
- https://www.eclipse.org/jetty/documentation/9.4.x/start-jar.html
- https://searchapparchitecture.techtarget.com/definition/RESTful-API
- https://www.youtube.com/watch?v=5jQSat1cKMo
- https://www.youtube.com/watch?v=501dpx2IjGY
- https://cmder.net/
