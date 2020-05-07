package by.todes.cert;

import sun.misc.BASE64Decoder;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;

import javax.security.auth.x500.X500Principal;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author l_miron
 */
public class CertOpen {

  static String signature = "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";

  public static void main(String[] args) throws Exception {
    Cert cert = new Cert();
    cert.openCertB64(signature);
    X509Certificate x509Cert = cert.getCert();
    System.out.println(x509Cert.getSubjectDN().getName());
    System.out.println(x509Cert.getSubjectX500Principal().getName(X500Principal.RFC1779));
    String[] attributes = x509Cert.getSubjectX500Principal().getName(X500Principal.RFC1779).split(",");
    for (String attr : attributes) {
      System.out.println(attr);
    }
    byte[] n = x509Cert.getExtensionValue("2.5.4.41");
    byte[] post = x509Cert.getExtensionValue("1.2.112.1.2.1.1.5.1");
    // Map <String, String> aa= new HashMap<>() ;
    String bb = cert.getExtensionValue("OID.2.5.4.4") + cert.getExtensionValue("OID.2.5.4.41");
    System.out.println(bb);
    // String aaa=cert.getExtensionValue("1.2.112.1.2.1.1.5.1");
    System.out.println(new String(post));

  }
}