## wiremock-request-to-body-transformer

Repackages wiremock version 2.3.1 to run with a response transformer.

The running wiremock runs with a default catch all response that returns a custom 404 page to better identify problems especially if running hidden in a docker container.
The 404 page is defined in the 404.vm  for docker, or in $WORK_DIR/mappings/somefile.json when running locally. 

Using velocity syntax users can out put the following information.
The example 404.vm file returns html for displaying in a browser.

| Velocity Name  | Outputs |
| ------------- | ------------- |
| ${request.absolute}  | Full req url |
| ${request.url}  | Url of the request  |
| ${request.schema}  | Request schema, e.g. http |
| ${request.server}  | Request server, e.g. localhost |
| ${request.port}  | Request server port, e.g. 8080 |
| ${request.path}  | Request path |
| ${request.paramNames}  | String list of param  names |
| ${request.params}  | Name value pair of all params |
| ${request.param.xxx}  | Value of the param xxx |
| ${request.headerNames}  | String list of header names |
| ${request.headers}  | Name value pair of all headers |
| ${request.header.xxx}  | Value of the header xxx |
| ${request.cookieNames}  | String list of cookies  names |
| ${request.cookies}  | Name value pair of all cookies |
| ${request.cookie.xxx}  | Value of the cookie xxx |

### Run locally
From the projects directory

step1 build:
```
mvn clean install
```

step2:
Customize the flattened file in the workdir mappings directory. 
e.g. the runlocal-example.json

step3: run on port 9005
```
java -jar target/wiremock-request-to-body-transformer-1.0.jar --port 9005
```
 
step4:
Use wiremock as normal, and a detailed 404 page should be visible for non mocked urls.

Note: If resetting please use WireMock.resetToDefault(); to maintain the custom 404


### Run as docker image
From the projects directory

step1 build:
```
mvn clean install
```

step2:
Customize the 404.vm file in the project directory

step3: build the docker image
```
docker build -t wiremock404 .
```

step4: Run the docker image exposing wiremock on docker hosts port 9006
```
docker run -it -p 9006:80 --name wiremock404-inst1 wiremock404
```

step5:
Use wiremock as normal, and a detailed 404 page should be visible for non mocked urls.

Note: If resetting please use WireMock.resetToDefault(); to maintain the custom 404

