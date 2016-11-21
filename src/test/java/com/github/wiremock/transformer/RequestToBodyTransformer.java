package com.github.wiremock.transformer;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.BinaryFile;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.wiremock.transformer.data.RequestData;
import com.github.wiremock.transformer.data.RequestDataBuilder;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class RequestToBodyTransformer extends ResponseDefinitionTransformer {

    public RequestToBodyTransformer() {

        Velocity.init();
    }

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource, Parameters parameters) {

        String body = getBody(responseDefinition, fileSource);

        return ResponseDefinitionBuilder
                .like(responseDefinition).but()
                .withBodyFile(null)
                .withBody(transformResponse(request, body))
                .build();
    }

    private String getBody(ResponseDefinition responseDefinition, FileSource fileSource) {
        String body = null;
        if (responseDefinition.getBody() != null) {
            body = responseDefinition.getBody();
        } else if (responseDefinition.getBodyFileName() != null){
            BinaryFile binaryFile = fileSource.getBinaryFileNamed(responseDefinition.getBodyFileName());
            body = new String(binaryFile.readContents(), StandardCharsets.UTF_8);
        }
        return body;
    }

    private String transformResponse(Request request, String response) {
        if (response == null) {
            return null;
        }

        RequestData requestData = RequestDataBuilder.build(request);
        VelocityContext context = new VelocityContext();
        context.put("request", requestData);

        StringWriter w = new StringWriter();
        Velocity.evaluate(context, w, "anything", response);
        return w.toString();
    }

    public String getName() {
        return "request-to-body-transformer";
    }
}
