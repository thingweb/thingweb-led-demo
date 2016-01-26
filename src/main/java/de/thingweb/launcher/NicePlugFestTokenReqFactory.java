package de.thingweb.launcher;

import de.thingweb.security.TokenRequirements;

/**
 * Created by mchn1210 on 26.01.2016.
 */
public class NicePlugFestTokenReqFactory {

    private static final String ISSUER = "NicePlugfestAS";
    private static final String AUDIENCE = "NicePlugfestRS";
    private static final String PUBKEY_ES256 = "{\"keys\":[{\"kty\": \"EC\",\"d\": \"_hysUUk5sRGAHhl7RJN7x5UhBMiy6pl6kHR5-ZaWzpU\",\"use\": \"sig\",\"crv\": \"P-256\",\"kid\": \"PlugFestNice\",\"x\": \"CQsJZUvJWx5yB5EwuipDXRDye4Ybg0wwqxpGgZtcl3w\",\"y\": \"qzYskD2N7GrGDSgo6N9pPLXMIwr6jowFGyqsTJGmpz4\",\"alg\": \"ES256\"},{\"kty\": \"oct\",\"kid\": \"018c0ae5-4d9b-471b-bfd6-eef314bc7037\",\"use\": \"sig\",\"alg\": \"HS256\",\"k\": \"aEp0WElaMnVTTjVrYlFmYnRUTldicGRtaGtWOEZKRy1PbmJjNm14Q2NZZw==\"}]}";
    private static final String SUBJECT = "0c5f83a7-cf08-4f48-8337-bfc65ea149ff";
    private static final String TYPE = "org:w3:wot:jwt:as:min";

    public static TokenRequirements createTokenRequirements() {
        return TokenRequirements.build()
                .setIssuer(ISSUER)
                .setAudience(AUDIENCE)
                .setVerificationKeys(PUBKEY_ES256)
                .setTokenType(TYPE)
                .createTokenRequirements();
    }
}
