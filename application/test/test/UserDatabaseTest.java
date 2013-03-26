package test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.dbentities.UserModel;
import models.user.Independent;
import models.user.Organizer;
import models.user.Pupil;
import models.user.Teacher;
import models.user.User;
import models.user.UserID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.avaje.ebean.Ebean;

import java.security.SecureRandom;
import java.math.BigInteger;
import javax.persistence.PersistenceException;

import junit.framework.Assert;
import controllers.user.Type;
import static play.test.Helpers.*;

public class UserDatabaseTest extends ContextTest {
	
	private SecureRandom random = new SecureRandom();

	/*
	 * Test IndependentUser database insertion.
	 */
	@Test
	public void makeIndependentUser() {
		String id = "id";
		String name = "Bertrand Russell";
		User userFind = null;
		UserModel mdl  = new UserModel(new UserID(id),Type.INDEPENDENT,name);
		User user = new Independent(mdl);
		
		try{
		user.data.save();
		}catch(PersistenceException e){
			Assert.fail("Could not save the user");
		}
		Assert.assertNotNull(Ebean.find(UserModel.class).where().eq("id",user.data.id).where().eq("type", Type.INDEPENDENT.toString()).findUnique());
		user.data.delete();
	}
	
	/*
	 * Test teacher database insertion.
	 */
	@Test
	public void makeTeacher(){
		
		int numberOfTeachers = 10;
		String teacherID = new BigInteger(130,random).toString();
		String name = "Maya Angelou";
		
		for(int i = 0; i < numberOfTeachers; i++){
			try{
			new Teacher(new UserModel(new UserID(new BigInteger(130,random).toString()), Type.TEACHER, new BigInteger(130,random).toString())).data.save();
			}catch(PersistenceException e){Assert.fail("Could not save the user");}
		}
		
		List<UserModel> teacherList = UserModel.find.where().like("type", "TEACHER").findList();
		Assert.assertTrue(teacherList.size() == numberOfTeachers);

		User teacher = new Teacher(new UserModel(new UserID(teacherID), Type.TEACHER, name));
		teacher.data.save();
		
		Assert.assertNotNull(Ebean.find(UserModel.class).where().eq("id",teacherID).findUnique());
		Assert.assertNotNull(Ebean.find(UserModel.class).where().eq("name", name).findUnique());
		
		teacher.data.delete();
	}
	
	@Test
	public void makeOrganizer(){
		
		int numberOfOrganizer = 10;
		String organizerID = new BigInteger(130,random).toString();
		String name = "Maya Angelou";
		
		for(int i = 0; i < numberOfOrganizer; i++){
			try{
			new Organizer(new UserModel(new UserID(new BigInteger(130,random).toString()), Type.ORGANIZER, new BigInteger(130,random).toString())).data.save();
			}catch(PersistenceException e){Assert.fail("Could not save the user");}
		}
		
		List<UserModel> organizerList = UserModel.find.where().like("type", "ORGANIZER").findList();
		Assert.assertTrue(organizerList.size() == numberOfOrganizer);

		User organizer = new Organizer(new UserModel(new UserID(organizerID), Type.ORGANIZER, name));
		organizer.data.save();
		
		Assert.assertNotNull(Ebean.find(UserModel.class).where().eq("id",organizerID).findUnique());
		Assert.assertNotNull(Ebean.find(UserModel.class).where().eq("name", name).findUnique());
		
		organizer.data.delete();
		
	}

	/**
	 * Test user finder.
	 */
	@Test
	public void findListBasedOnType(){
		int numberOfIndependentUser = 10;
		int numberOfPupils = 5;
		
		//First clear the Users
		for (UserModel um :UserModel.find.all()){
			um.delete();
		}
		
		//generate random IndependentUser and save them.
		for(int i = 0; i < numberOfIndependentUser; i++){
			try{
			Independent ip = new Independent(new UserModel(new UserID(new BigInteger(130,random).toString()),Type.INDEPENDENT,new BigInteger(130,random).toString()));
			ip.data.save();
			
			}catch(PersistenceException e){Assert.fail("Could not save the user");}
		}
		
		//generate random Pupils.
		for(int i = 0; i < numberOfPupils; i++){
			try{
			Pupil pu = new Pupil(new UserModel(new UserID(new BigInteger(130,random).toString()),Type.PUPIL,new BigInteger(130,random).toString()));
			pu.data.save();
			}catch(PersistenceException e){Assert.fail("Could not save the user");}
		}
		
		List<UserModel> allUsers = UserModel.find.all();
		List<UserModel> allIndepententUser = UserModel.find.where().like("type", Type.INDEPENDENT.toString()).findList();
		List<UserModel> allPupils = UserModel.find.where().like("type",Type.PUPIL.toString()).findList();
		System.out.println(allIndepententUser.size());
		System.out.println(allPupils.size());
		Assert.assertTrue(Integer.toString(allUsers.size()),allUsers.size() == numberOfIndependentUser+numberOfPupils);
		Assert.assertTrue("indep",allIndepententUser.size() == numberOfIndependentUser);
		Assert.assertTrue("pup",allPupils.size() == numberOfPupils);
	}
}
