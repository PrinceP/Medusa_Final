package medusa;
import com.chilkatsoft.*;

public class ChilkatExample {
 
    public ChilkatExample()
    {
        
    }
  static {
    try {
        System.load("C:\\chilkat.dll");
    } catch (UnsatisfiedLinkError e) {
      System.err.println("Native code library failed to load.\n" + e);
      System.exit(1);
    }
  }

    /**
     *
     * @param text
     * @param password
     * @param encdec
     * @return
     */
    public String doencryption(String text, String password, int encdec)
  {
    CkCrypt2 crypt = new CkCrypt2();

    boolean success;
    success = crypt.UnlockComponent("Anything for 30-day trial");
    if (success != true) {
        System.out.println("Crypt component unlock failed");
        return "fail";
    }

   

    crypt.put_CryptAlgorithm("aes");
    crypt.put_CipherMode("cbc");
    crypt.put_KeyLength(128);

    //  Generate a binary secret key from a password string
    //  of any length.  For 128-bit encryption, GenEncodedSecretKey
    //  generates the MD5 hash of the password and returns it
    //  in the encoded form requested.  The 2nd param can be
    //  "hex", "base64", "url", "quoted-printable", etc.
    String hexKey;
    hexKey = crypt.genEncodedSecretKey(password,"hex");
    crypt.SetEncodedKey(hexKey,"hex");

    crypt.put_EncodingMode("base64");
   
   

    //  Encrypt a string and return the binary encrypted data
    //  in a base-64 encoded string.
    String encdectext = null;
    if(encdec == 1)
    {
    
    encdectext = crypt.encryptStringENC(text);
   
    System.out.println(encdectext);
    }
    else if(encdec == 2)
    {
    
    encdectext = crypt.decryptStringENC(text);

    System.out.println(encdectext);
    }
      return encdectext;
  }
  
}