package com.littlefox.storybook.lib.enc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Collections;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.littlefox.logmonitor.Log;
import com.littlefox.storybook.lib.api.StorybookTempleteAPI;

import android.net.Uri;
import android.util.Base64;

public class Mp4Encrypt {

	private static volatile Mp4Encrypt INSTANCE;
	//mp4 body 시작 위치 624 ~ 624+108
	private final int  ENC_BODY_START = 624;
	//mp4 헤더 0 ~ 108
	private final int  ENC_LENGTH = 108;
	private final String ENC_STRING_KEY = "www.littlefox.co.kr";
	
	public static Mp4Encrypt getInstance() {
		if (INSTANCE == null) {
			synchronized (Mp4Encrypt.class) {
				if (INSTANCE == null)
					INSTANCE = new Mp4Encrypt();
			}
		}
		return INSTANCE;
	}
	

	public static String getMD5EncyptPath(String str)
	{
		return StorybookTempleteAPI.PATH_MP4+File.separator+getMD5Hash(Uri.parse(str).getLastPathSegment());
	}
	
	private byte[] getHash(byte[] str) {
		byte[] bytesOfMessage = str;
		MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("MD5");

	    } catch (NoSuchAlgorithmException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    
		return md.digest(bytesOfMessage);
	}
	
	private byte[] getHash(String str) {
		byte[] bytesOfMessage = str.getBytes();
		MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("MD5");

	    } catch (NoSuchAlgorithmException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    
		return md.digest(bytesOfMessage);
	}
	
	public static String getMD5Hash(String param) {

		StringBuffer md5 = new StringBuffer();

		try {

			byte[] digest = java.security.MessageDigest.getInstance("MD5")
					.digest(param.getBytes());

			for (int i = 0; i < digest.length; i++) {
				md5.append(Integer.toString((digest[i] & 0xf0) >> 4, 16));
				md5.append(Integer.toString(digest[i] & 0x0f, 16));
			}

		} catch (java.security.NoSuchAlgorithmException ne) {

			ne.printStackTrace();
		}

		return md5.toString();

	}
	
	//랜덤 IV값 
	private byte[] generateIv() throws NoSuchAlgorithmException {
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] iv = new byte[32];
		random.nextBytes(iv);
		return iv;
	}
	
	//IV 값 생성
	public byte[] makeIV() throws NoSuchAlgorithmException, InvalidParameterSpecException, NoSuchPaddingException, InvalidKeyException {
		byte[] iv = null;
		try {
			iv = generateIv();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getHash(iv);
	}
	

	public String headerEnc(byte[] str, String strPath)
		
		throws java.io.UnsupportedEncodingException,
		NoSuchAlgorithmException, NoSuchPaddingException,
		InvalidKeyException, InvalidAlgorithmParameterException,
		IllegalBlockSizeException, BadPaddingException {
	
		byte[] iv1byte = null;
		try {
			iv1byte = makeIV();//random
		} catch (InvalidParameterSpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	
		if(iv1byte == null)
			return null;
		
		String secretKey = getMD5Hash(ENC_STRING_KEY);		
		byte[] keyData = secretKey.getBytes();
	
		SecretKey secureKey = new SecretKeySpec(keyData, "AES");
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		
		IvParameterSpec ivSpec = new IvParameterSpec(iv1byte);
		c.init(Cipher.ENCRYPT_MODE, secureKey, ivSpec);
	
		byte[] encrypted = c.doFinal(str);
		byte[] data = new byte[encrypted.length + iv1byte.length];
		//IV+data+IV
		System.arraycopy(iv1byte, 0, data, 0, iv1byte.length/2);
		System.arraycopy(encrypted, 0, data, iv1byte.length/2, encrypted.length);
		System.arraycopy(iv1byte, iv1byte.length/2, data, encrypted.length+iv1byte.length/2, iv1byte.length/2);
		
		iv1byte = null;
		keyData = null;
		encrypted = null;
		
		//data base64
		return new String(Base64.encode(data, 0)).trim();
		
		//return Base64.encode(data, 0);
	}

	public byte[] headerDec(byte[] str, String strPath) throws java.io.UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {

		
		if(str == null )
			return null;

		byte[] data = Base64.decode(str, 0);
		byte[] iv1byte = new byte[16];
		byte[] dataReal = new byte[data.length-iv1byte.length];
		
		System.arraycopy(data, 0, iv1byte, 0, iv1byte.length/2);
		System.arraycopy(data, data.length-iv1byte.length/2, iv1byte, iv1byte.length/2, iv1byte.length/2);
		System.arraycopy(data, iv1byte.length/2, dataReal, 0, dataReal.length);
		
		String secretKey = getMD5Hash(ENC_STRING_KEY);		
		byte[] keyData = secretKey.getBytes();

		Key secureKey = new SecretKeySpec(keyData, "AES");

		Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
		c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv1byte));
		//String rtn = new String(c.doFinal(dataReal), "UTF-8").trim();
		
		data = null;
		iv1byte = null;
		keyData = null;
		secureKey = null;
		
		return c.doFinal(dataReal);
	}
	
	
	//암호화 데이타를 파일에 적어 둔다.
	public boolean save(String data, String str) {
		
		boolean btn = false;
		
		try {
			//파일이름 해쉬로 암호화 데이타 파일이름 지정.
			String path = getMD5EncyptPath(str);
			Log.i("path : "+path);
			
			File file=new File(path);	
	        FileOutputStream fos = new FileOutputStream(file);
			fos.write(data.getBytes());
			fos.close();
			file = null;
			btn = true;
		} catch (IOException e) {
			e.printStackTrace();
			btn = false;
		}
		
		return btn;
	}
	
	private byte[] read(String str) {
		String path = getMD5EncyptPath(str);
		
		Log.i("read :"+ path);
		
		File file = new File(path);
	    FileInputStream fis = null;
		
		byte[] b = new byte[(int) file.length()];
		try {
			fis = new FileInputStream(file);
			fis.read(b);
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			b = null;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			b = null;
			e.printStackTrace();
		}

		
		file = null;
		
		return b;
	} // end of main 
	
	
	//암호화 스트링에 들어갈 dummy 스트링 이걸 랜덤하게 섞는다.
	private byte[] randomString() {
		byte[] bytes = new byte[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q'
				,'r','s','t','u','v','w','x','y','g','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W'
				,'X','Y','Z','1','2','3','4','5','6','7','8','9','!','@','#','$','%','^','&','*','(',')','-','_','+','=','[',']','{','}','<','>'
				,'?','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','1','2','3','4','5','6','7','8','9'
				}; 
		
		ArrayList<Byte> ar = new ArrayList<Byte>();
		for(int n=0; n < bytes.length;n++) {
			ar.add(bytes[n]);
		}
		
		Collections.shuffle(ar);
		
		for(int i = 0; i < ar.size(); i++) {
			bytes[i] = ar.get(i);
		}
		
		//암호화 스트링 앞부분에 들어갈 문자 암호화 여부 판단용
		bytes[0] = 'l';
		bytes[1] = 'i';
		bytes[2] = 't';
		bytes[3] = 't';
		bytes[4] = 'l';
		bytes[5] = 'e';
		bytes[6] = 'f';
		bytes[7] = 'o';
		bytes[8] = 'x';
		
		return bytes;
	}

	public boolean encWrite(String strPath) throws IOException {
		
		
		boolean bRtn = true;
		
		long t1 = System.currentTimeMillis();  
		
		long filesize;
		byte[] bytes = randomString();
		byte[] littlefox = {'l','i','t','t','l','e','f','o','x'};  //암호화 스트링 앞부분에 암호화 여부 판단 하기 위한 스트링
		byte[] readbytes = new byte[ENC_LENGTH];
		byte[] headerbody = new byte[ENC_LENGTH*2];
		
		
		
        RandomAccessFile output = new RandomAccessFile(strPath, "rw"); 
        filesize = output.length(); 
        
        FileChannel fc = output.getChannel();
        
        MappedByteBuffer MappByteBuff = fc.map(FileChannel.MapMode.READ_WRITE, 0, filesize);
        fc.close();  
        output.close();  

        MappByteBuff.position(0);
        MappByteBuff.get(readbytes);
        
        
        //암호화 여부 판단
        int nCmp = 0;
        for(int n=0;n<littlefox.length;n++) {
        	if(readbytes[n] == littlefox[n]) {
        		nCmp ++;
        	}
        }
        
        //중복 암호화 피하기
        if(nCmp == littlefox.length)
        	return false;
        
//        if(Arrays.equals(readbytes, bytes))
//        	return false;
        
        
        System.arraycopy(readbytes, 0, headerbody, 0, ENC_LENGTH);
        
        MappByteBuff.position(ENC_BODY_START);
        MappByteBuff.get(readbytes);
            
        System.arraycopy(readbytes, 0, headerbody, headerbody.length/2, ENC_LENGTH);

        String strenc = "";
        try {
        	strenc = headerEnc(headerbody, strPath);
        	
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			if(strenc.length() <= 0)
				return false;
			else
				save(strenc, strPath);
		}
        
        MappByteBuff.position(0);
        MappByteBuff.put(bytes);
        
        MappByteBuff.position(ENC_BODY_START);
        MappByteBuff.put(bytes);  
        
        MappByteBuff.force();  
        MappByteBuff.flip();  
        
        t1 = System.currentTimeMillis();  
        
        MappByteBuff.clear();
        MappByteBuff = null;
        bytes = null;
        readbytes = null;
        headerbody = null;
        
		return bRtn;
	}
	
	public void decWrite(String strPath, RandomAccessFile output) throws IOException {
		
		
		long t1 = System.currentTimeMillis();  
		
		long filesize;
		
		byte[] buf = null;
		try {
			buf = headerDec(read(strPath), strPath);
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(buf == null)
				return;
		}
		
		output = new RandomAccessFile(strPath, "rw"); 
        filesize = output.length(); 
        
        FileChannel fc = output.getChannel();
        MappedByteBuffer MappByteBuff = fc.map(FileChannel.MapMode.READ_WRITE, 0, filesize);
        fc.close();  
        output.close();  
        MappByteBuff.position(0);
        MappByteBuff.put(buf, 0 , ENC_LENGTH);  
        
        MappByteBuff.position(ENC_BODY_START);
        MappByteBuff.put(buf, ENC_LENGTH , ENC_LENGTH);  
        
        MappByteBuff.force();  
        MappByteBuff.flip();  
        

        buf = null;
        MappByteBuff.clear();
        MappByteBuff = null;
        
        t1 = System.currentTimeMillis();  
        
	}
	
	
}
