package controllers;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.format.datetime.DateFormatter;
import models.data.Link;
import models.user.AuthenticationManager;
import models.user.Gender;
import models.user.UserID;
import play.data.Form;
import play.mvc.Content;
import play.mvc.Result;
import play.mvc.Results.Redirect;
import scala.collection.mutable.HashMap;
import models.dbentities.UserModel;
import views.html.index;
import views.html.landingPages.AdminLandingPage;
import views.html.landingPages.IndependentPupilLandingPage;
import views.html.landingPages.OrganizerLandingPage;
import views.html.landingPages.PupilLandingPage;
import views.html.register;
import views.html.emptyPage;
import controllers.user.Type;

/**
 * This class receives all GET requests and based on there session identifier (cookie)
 * and current role in the system they will be served a different view.
 * @author Sander Demeester
 */
public class UserController extends EController{

	/**
	 * This hashmap embodies the mapping from a Type to a view.
	 * Each view is responsible for getting all information from the DataModel and make a
	 * beautiful view for the user :)
	 */
	private HashMap<Type, Class<?>> landingPageHashmap = new HashMap<Type, Class<?>>();
	private AuthenticationManager authenticatieManger = new AuthenticationManager();

	public UserController(){
		landingPageHashmap.put(Type.ADMINISTRATOR, AdminLandingPage.class);
		landingPageHashmap.put(Type.INDEPENDENT, IndependentPupilLandingPage.class);
		landingPageHashmap.put(Type.ORGANIZER, OrganizerLandingPage.class);
		landingPageHashmap.put(Type.PUPIL,PupilLandingPage.class);
	}
	/**
	 * This methode gets requested when the user clicks on "signup".
	 * @return Result page.
	 */
	public static Result signup(){
		//TODO: Delegate to authenticationManager
		setCommonHeaders();
		return ok(register.render("Registration", 
				new ArrayList<Link>(),
				form(Register.class)
		));
	}
	/**
	 * this methode is called when the user submits his/here register information.
	 * @return Result page
	 */
	public static Result register(){
		setCommonHeaders();
		Form<Register> registerForm = form(Register.class).bindFromRequest();
		SecureRandom random = null;
		SecretKeyFactory secretFactory = null;
		byte[] passwordByteString = null;
		String passwordUTF8 = "";
		String saltUTF8 = "";
		Date birtyDay = new Date();
		
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {}

		byte[] salt = new byte[16]; //RSA PKCS5
		
		
		random.nextBytes(salt);
		KeySpec PBKDF2 = new PBEKeySpec(registerForm.get().password.toCharArray(), salt, 1000, 160);



		try{
			secretFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		}catch(Exception e){}


		try {
			passwordByteString = secretFactory.generateSecret(PBKDF2).getEncoded();
		} catch (InvalidKeySpecException e) {}
		try{
			passwordUTF8 = new String(passwordByteString,"UTF-8");
			saltUTF8 = new String(salt,"UTF-8");
			birtyDay = new SimpleDateFormat("yyyy/dd/mm").parse(registerForm.get().bday);
		}catch(Exception e){}
		
		String r = new String();
		r += registerForm.get().bday;
		
		new UserModel(new UserID("id_aamaken"), Type.INDEPENDENT,
				registerForm.get().fname + registerForm.get().lname, 
				birtyDay, 
				new Date(), 
				passwordUTF8, 
				saltUTF8, registerForm.get().email, 
				Gender.valueOf(registerForm.get().gender));
		
		return ok(emptyPage.render("Succes", new ArrayList<Link>(), new Date().toString()));
	}

	public static Result login(){
		//TODO: Delage to authenticationManager
		return null;
	}

	public static Result logout(){
		//TODO: Tell authenticationManager to log a user out.

		setCommonHeaders();
		return null;
	}

	public static Result getLandingPage(String token){ //or whatever the token will be
		//TODO: Delegate to correct UserController object based on token
		/*
		 * The result will come from the landingPageHashMap
		 */
		return null;
	}

	public static class Register{
		public String fname;
		public String lname;
		public String email;
		public String bday;
		public String password;
		public String controle_passwd;
		public String gender;
		public String prefLanguage;
	}

}
