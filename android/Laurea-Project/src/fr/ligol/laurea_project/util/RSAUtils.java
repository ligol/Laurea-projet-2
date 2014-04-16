package fr.ligol.laurea_project.util;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

public class RSAUtils {
    @SuppressLint("TrulyRandom")
    public static void generatePrivateKey(Context c) {
        Log.d("printkey", "generate");
        KeyPairGenerator kpg;
        SharedPreferences sp = c.getSharedPreferences("laurea_project",
                Context.MODE_PRIVATE);
        try {
            kpg = KeyPairGenerator.getInstance("RSA");

            kpg.initialize(1024);
            KeyPair kp = kpg.genKeyPair();
            String publicK = Base64.encodeToString(kp.getPublic().getEncoded(),
                    Base64.DEFAULT);
            String privateK = Base64.encodeToString(kp.getPrivate()
                    .getEncoded(), Base64.DEFAULT);
            sp.edit().putString("priv", privateK).commit();
            sp.edit().putString("pub", publicK).commit();
            sp.edit().putBoolean("private", true).commit();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static PrivateKey getPrivateKey(Context c)
            throws UnsupportedEncodingException {
        try {
            SharedPreferences sp = c.getSharedPreferences("laurea_project",
                    Context.MODE_PRIVATE);
            String privateK = sp.getString("priv", null);
            byte[] keyBytes = Base64.decode(privateK, Base64.DEFAULT);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey priv = fact.generatePrivate(keySpec);
            return priv;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey getPublicKey(Context c)
            throws UnsupportedEncodingException {
        try {
            SharedPreferences sp = c.getSharedPreferences("laurea_project",
                    Context.MODE_PRIVATE);
            String publicK = sp.getString("pub", null);
            // Log.d("printkey", publicK.replace("\n", "+"));
            byte[] keyBytes = Base64.decode(publicK, Base64.DEFAULT);
            // Log.d("printkey2", new String(keyBytes).replace("\n", "+"));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(spec);
            // Log.d("printkey", keyFactory
            // .getKeySpec(key, RSAPublicKeySpec.class).getModulus()
            // .toString());
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey getPublicKey(String publicK)
            throws UnsupportedEncodingException {
        try {
            // Log.d("printkey", publicK.replace("\n", "+"));
            byte[] keyBytes = Base64.decode(publicK, Base64.DEFAULT);
            // Log.d("printkey2", new String(keyBytes).replace("\n", "+"));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey key = keyFactory.generatePublic(spec);
            // Log.d("printkey", keyFactory
            // .getKeySpec(key, RSAPublicKeySpec.class).getModulus()
            // .toString());
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPrivateKeyHash(Context c) {
        try {
            PrivateKey priv = getPrivateKey(c);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(priv.getEncoded());
            return Base64.encodeToString(md.digest(), Base64.DEFAULT);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPublicKeyHash(Context c) {
        try {
            PublicKey priv = getPublicKey(c);
            // Log.d("test key2",
            // Base64.encodeToString(priv.getEncoded(), Base64.DEFAULT)
            // .replace("\n", "+"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(priv.getEncoded());
            return Base64.encodeToString(md.digest(), Base64.DEFAULT);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPublicKeyHash(String c) {
        try {
            PublicKey priv = getPublicKey(c);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(priv.getEncoded());
            return Base64.encodeToString(md.digest(), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encrypt(String key, String message) {
        try {
            PublicKey pubKey = getPublicKey(key);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] cipherText = cipher.doFinal(Base64.decode(message,
                    Base64.DEFAULT));
            return Base64.encodeToString(cipherText, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(Context c, String message) {
        try {
            PrivateKey pubKey = getPrivateKey(c);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            byte[] cipherText = cipher.doFinal(Base64.decode(message,
                    Base64.DEFAULT));
            return Base64.encodeToString(cipherText, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
