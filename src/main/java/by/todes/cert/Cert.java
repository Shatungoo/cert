package by.todes.cert;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;

public class Cert {
  X509Certificate cert;

  public void openCert(File file) throws FileNotFoundException, IOException, CertificateException {
    InputStream fis = new FileInputStream(file);
    BufferedInputStream bis = new BufferedInputStream(fis);
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    cert = (X509Certificate) cf.generateCertificate(bis);
  }

  public void openCertB64(String certStr) throws FileNotFoundException, IOException, CertificateException {

    String encoding;
    if (certStr.contains("BEGIN CERTIFICATE")) {
      ByteArrayInputStream ex = new ByteArrayInputStream(certStr.getBytes());
      CertificateFactory cf = CertificateFactory.getInstance("X.509");
      cert = (X509Certificate) cf.generateCertificate(ex);
      // encoding="PkiPath";
    } else {
      encoding = "PKCS7";
      byte[] contentInBytes = Base64.getDecoder().decode(certStr);
      ByteArrayInputStream ex = new ByteArrayInputStream(contentInBytes);
      CertificateFactory cf = CertificateFactory.getInstance("X.509");
      cert = (X509Certificate) cf.generateCertPath(ex, encoding).getCertificates().get(0);
    }

  }

  public void setCert(X509Certificate cert) {
    this.cert = cert;
  }

  public X509Certificate getCert() {
    return cert;
  }

  // public Map<String, String> getOIDs() throws Exception {
  // Map<String, String> oidMap = new TreeMap<>();
  // oidMap.put("Сертификат", cert.getSubjectDN().toString());
  // oidMap.put("алгоритм", cert.getSigAlgName());
  // oidMap.put("срок действия с ", cert.getNotBefore().toString());
  // oidMap.put("срок действия по ", cert.getNotAfter().toString());
  // Set<String> crOids = cert.getCriticalExtensionOIDs();
  // for (String oid : crOids) {
  // oidMap.put(oid, getOID(cert.getExtensionValue(oid)));
  // }
  // Set<String> nonCrOids = cert.getNonCriticalExtensionOIDs();
  // for (String oid : nonCrOids) {
  // oidMap.put(oid, getOID(cert.getExtensionValue(oid)));
  // }
  // return oidMap;
  // }
  //
  // String getOID(byte[] oid) throws IOException {
  // DerValue val = new DerValue(oid);
  // byte[] derEncoding = val.toByteArray();
  //// ObjectIdentifier q = val.getOID();
  // try{
  // System.out.println(val.getGeneralString());
  //// System.out.println(val.getBitString());
  // return val.getGeneralString();
  // }catch (Exception e){
  //
  // }
  // String s = val.getAsString();
  // System.out.println(s);
  // return s;
  // }

  DERObject toDERObject(byte[] data) throws IOException {
    ByteArrayInputStream inStream = new ByteArrayInputStream(data);
    ASN1InputStream asnInputStream = new ASN1InputStream(inStream);

    return asnInputStream.readObject();
  }

  public String getExtensionValue(String oid) throws IOException {
    String decoded = null;
    byte[] extensionValue = cert.getExtensionValue(oid);

    if (extensionValue != null) {
      DERObject derObject = toDERObject(extensionValue);
      if (derObject instanceof DEROctetString) {
        DEROctetString derOctetString = (DEROctetString) derObject;
        derObject = toDERObject(derOctetString.getOctets());
        decoded = derObject.toString();
        // if (derObject instanceof DERUTF8String) {
        // DERUTF8String s = DERUTF8String.getInstance(derObject);
        // decoded = s.getString();
        // }

      }
    }
    return decoded;
  }

  public void toFile(String path) throws CertificateEncodingException, IOException {

    File file = new File(path);
    if (!file.exists()) {
      file.createNewFile();
    }
    byte[] buf = cert.getEncoded();

    FileOutputStream os = new FileOutputStream(file);
    os.write(buf);
    os.close();
  }
}
