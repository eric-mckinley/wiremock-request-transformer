package com.github.wiremock;

import com.github.tomakehurst.wiremock.standalone.WireMockServerRunner;
import com.github.wiremock.transformer.RequestToBodyTransformer;

public class WiremockLauncher {

    public static void main(String[] args) {

        String[] transformerArgs = new String[args.length + 2];
        System.arraycopy(args, 0, transformerArgs, 0, args.length);

        transformerArgs[args.length] = "--extensions";
        transformerArgs[args.length + 1] = RequestToBodyTransformer.class.getName();
        WireMockServerRunner.main(transformerArgs);
    }
}
