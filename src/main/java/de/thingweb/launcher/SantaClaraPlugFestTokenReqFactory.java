package de.thingweb.launcher;

import de.thingweb.security.TokenRequirements;

/**
 * Created by mchn1210 on 26.01.2016.
 */
public class SantaClaraPlugFestTokenReqFactory {

    private static final String ISSUER = "NicePlugfestAS"; //left at Nice just to confuse
    private static final String AUDIENCE = "NicePlugfestRS";
    private static final String PUBKEY_Thingweb_RS256 = "{keys: [{kty: \"RSA\",e: \"AQAB\",use: \"sig\",kid: \"273993369\",alg: \"RS256\",n: \"3lh9zHIe3Zi4mfYpl7AwU25dP4Axvt4WTpM2_l86YX8DBFeBzvuTE5U2yIpH_Cs9vUZnMjkwok3ez8SKzvFb6mTlPY9Lfu6Gk7_dfsolFKJoq97aNbHU_i47YOQG_Ecni4i6DhWcDKQkyA6KFAmjZZ_gxZJekisSkewuKSmDo8M\"}]}";
    private static final String SUBJECT = "0c5f83a7-cf08-4f48-8337-bfc65ea149ff";
    private static final String TYPE = "org:w3:wot:jwt:as:min";

    public static TokenRequirements createTokenRequirements() {
        return TokenRequirements.build()
                .setIssuer(ISSUER)
                .setAudience(AUDIENCE)
                .setVerificationKeys(PUBKEY_Thingweb_RS256)
                .setTokenType(TYPE)
                .createTokenRequirements();
    }
}
