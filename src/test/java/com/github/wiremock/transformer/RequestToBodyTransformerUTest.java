package com.github.wiremock.transformer;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.jayway.restassured.response.Cookie;
import com.jayway.restassured.response.Header;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RequestToBodyTransformerUTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(8080).extensions(new RequestToBodyTransformer()));

    @Test
    public void testRequestUrl() {

        wireMockRule.stubFor(post(urlEqualTo("/test/example/stuff"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("You visited ${request.url}")
                        .withTransformers("request-to-body-transformer")));
        given()
                .contentType("application/json")

                .when()
                .post("/test/example/stuff")
                .then()
                .statusCode(200)
                .body(equalTo("You visited /test/example/stuff"));

        wireMockRule.verify(postRequestedFor(urlEqualTo("/test/example/stuff")));
    }

    @Test
    public void testRequestAbsoluteUrl() {
        wireMockRule.stubFor(post(urlEqualTo("/test/example/stuff"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("You visited ${request.absoluteUrl}")
                        .withTransformers("request-to-body-transformer")));
        given()
                .contentType("application/json")

                .when()
                .post("/test/example/stuff")
                .then()
                .statusCode(200)
                .body(equalTo("You visited http://localhost:8080/test/example/stuff"));

        wireMockRule.verify(postRequestedFor(urlEqualTo("/test/example/stuff")));
    }

    @Test
    public void testRequestMethod() {
        wireMockRule.stubFor(post(urlEqualTo("/test/example/stuff"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("You visited ${request.method}")
                        .withTransformers("request-to-body-transformer")));
        given()
                .contentType("application/json")

                .when()
                .post("/test/example/stuff")
                .then()
                .statusCode(200)
                .body(equalTo("You visited POST"));

        wireMockRule.verify(postRequestedFor(urlEqualTo("/test/example/stuff")));
    }

    @Test
    public void testRequestHeaders() {

        String standardHeaders = ", Accept, Connection, User-Agent, Host, Accept-Encoding, Content-Length, Content-Type]";

        wireMockRule.stubFor(post(urlEqualTo("/test/example/stuff"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("You visited ${request.headerNames}")
                        .withTransformers("request-to-body-transformer")));
        given()
                .contentType("application/json")
                .header(new Header("some-header-a", "A"))
                .header(new Header("some-header-b", "B"))
                .when()
                .post("/test/example/stuff")

                .then()
                .statusCode(200)
                .body(equalTo("You visited [some-header-a, some-header-b" + standardHeaders));

        wireMockRule.verify(
                postRequestedFor(urlEqualTo("/test/example/stuff"))
        );
    }

    @Test
    public void testRequestHeader() {


        wireMockRule.stubFor(post(urlEqualTo("/test/example/stuff"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("You visited ${request.header.some-header-a}")
                        .withTransformers("request-to-body-transformer")));
        given()
                .contentType("application/json")
                .header(new Header("some-header-a", "A"))
                .header(new Header("some-header-b", "B"))
                .when()
                .post("/test/example/stuff")

                .then()
                .statusCode(200)
                .body(equalTo("You visited A"));

        wireMockRule.verify(
                postRequestedFor(urlEqualTo("/test/example/stuff"))
        );
    }

    @Test
    public void testRequestCookie() {


        wireMockRule.stubFor(post(urlEqualTo("/test/example/stuff"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("You visited ${request.cookie.cookieB}")
                        .withTransformers("request-to-body-transformer")));
        given()
                .contentType("application/json")
                .cookie(new Cookie.Builder("cookieB", "BBB").build())
                .when()
                .post("/test/example/stuff")

                .then()
                .statusCode(200)
                .body(equalTo("You visited BBB"));

        wireMockRule.verify(
                postRequestedFor(urlEqualTo("/test/example/stuff"))
        );
    }

    @Test
    public void testRequestPort() {


        wireMockRule.stubFor(post(urlEqualTo("/test/example/stuff"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("You visited ${request.port}")
                        .withTransformers("request-to-body-transformer")));
        given()
                .contentType("application/json")
                .when()
                .post("/test/example/stuff")

                .then()
                .statusCode(200)
                .body(equalTo("You visited 8080"));

        wireMockRule.verify(
                postRequestedFor(urlEqualTo("/test/example/stuff"))
        );
    }

    @Test
    public void testRequestParams() {

        wireMockRule.stubFor(get(urlEqualTo("/test/example/stuff?abc=123&def=456&ghi=789"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("You visited ${request.paramNames}")
                        .withTransformers("request-to-body-transformer")));
        given()
                .contentType("application/json")
                .queryParam("abc", "123")
                .queryParam("def", "456")
                .queryParam("ghi", "789")
                .when()
                .get("/test/example/stuff")

                .then()
                .statusCode(200)
                .body(equalTo("You visited [abc, def, ghi]"));

        wireMockRule.verify(
                getRequestedFor(urlEqualTo("/test/example/stuff?abc=123&def=456&ghi=789"))
        );
    }

    @Test
    public void testRequestParam() {

        wireMockRule.stubFor(get(urlEqualTo("/test/example/stuff?abc=123&def=456&ghi=789"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("You visited ${request.param.def}")
                        .withTransformers("request-to-body-transformer")));
        given()
                .contentType("application/json")
                .queryParam("abc", "123")
                .queryParam("def", "456")
                .queryParam("ghi", "789")
                .when()
                .get("/test/example/stuff")

                .then()
                .statusCode(200)
                .body(equalTo("You visited 456"));

        wireMockRule.verify(
                getRequestedFor(urlEqualTo("/test/example/stuff?abc=123&def=456&ghi=789"))
        );
    }

    @Test
    public void testRequestPath() {

        wireMockRule.stubFor(get(urlEqualTo("/test/example/stuff?abc=123&def=456&ghi=789"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("content-type", "application/json")
                        .withBody("You visited ${request.path}")
                        .withTransformers("request-to-body-transformer")));
        given()
                .contentType("application/json")
                .queryParam("abc", "123")
                .queryParam("def", "456")
                .queryParam("ghi", "789")
                .when()
                .get("/test/example/stuff")

                .then()
                .statusCode(200)
                .body(equalTo("You visited /test/example/stuff"));

        wireMockRule.verify(
                getRequestedFor(urlEqualTo("/test/example/stuff?abc=123&def=456&ghi=789"))
        );
    }
}
