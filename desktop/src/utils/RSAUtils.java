package utils;

import java.io.IOException;
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
import java.sql.SQLException;

import objects.Check;

import org.java_websocket.util.Base64;

import com.j256.ormlite.dao.Dao;


public class RSAUtils {
	public static void generatePrivateKey(Dao<Check, String> check) {
		KeyPairGenerator kpg;

		try {
			kpg = KeyPairGenerator.getInstance("RSA");

			kpg.initialize(1024);
			KeyPair kp = kpg.genKeyPair();
			String publicK = Base64.encodeBytes(kp.getPublic().getEncoded());
			String privateK = Base64.encodeBytes(kp.getPrivate().getEncoded());
			Check update = new Check(privateK, publicK);
			check.create(update);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static PrivateKey getPrivateKey(Dao<Check, String> check)
			throws UnsupportedEncodingException {
		try {
			Check select = new Check();
			select = check.queryForId("Keys");
			String privateK = select.getPriv();
			byte[] keyBytes = Base64.decode(privateK);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey priv = fact.generatePrivate(keySpec);
			return priv;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static PublicKey getPublicKey(Dao<Check, String> check)
			throws UnsupportedEncodingException {
		try {
			Check select = new Check();
			select = check.queryForId("Keys");
			String publicK = select.getPub();
			byte[] keyBytes = Base64.decode(publicK);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey key = keyFactory.generatePublic(spec);
			return key;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getPrivateKeyHash(Dao<Check, String> check) {
		try {
			PrivateKey priv = getPrivateKey(check);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(priv.getEncoded());
			return Base64.encodeBytes(md.digest());
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getPublicKeyHash(Dao<Check, String> check) {
		try {
			PublicKey priv = getPublicKey(check);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(priv.getEncoded());
			return Base64.encodeBytes(md.digest());
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
